package io.builders.demo.dtcc.application.service.manageairesponse

import groovy.util.logging.Slf4j
import io.builders.demo.core.command.CommandBus
import io.builders.demo.core.query.QueryBus
import io.builders.demo.dtcc.application.command.persistlsm.PersistLsmNetCommand
import io.builders.demo.dtcc.application.dlt.query.getbalance.AccountBalancesQueryModel
import io.builders.demo.dtcc.application.dlt.query.getbalance.GetBalanceQuery
import io.builders.demo.dtcc.domain.lsmbatch.LsmBatch
import io.builders.demo.dtcc.domain.lsmbatch.service.CheckLsmBatchExistsDomainService
import io.builders.demo.dtcc.domain.settlement.Settlement
import io.builders.demo.dtcc.domain.settlement.SettlementRepository
import io.builders.demo.dtcc.domain.user.UserRepository
import io.builders.demo.dtcc.domain.utils.NetUtils
import io.builders.demo.integration.model.Balance
import io.builders.demo.integration.model.IASettlement
import io.builders.demo.integration.sagemaker.SMBalance
import io.builders.demo.integration.sagemaker.SMPort
import io.builders.demo.integration.sagemaker.SMRequest
import io.builders.demo.integration.sagemaker.SMSettlement
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.concurrent.atomic.AtomicInteger

@Service
@Slf4j
@SuppressWarnings(['UseCollectMany'])
class ManageAIResponseAppService {

    private final AtomicInteger counter = new AtomicInteger(0)

    @Autowired
    QueryBus queryBus

    @Autowired
    CommandBus commandBus

    @Autowired
    CheckLsmBatchExistsDomainService checkLsmBatchExistsDomainService

    @Autowired
    SettlementRepository settlementRepository

    @Autowired
    NetUtils netUtils

    @Autowired
    UserRepository userRepository

    @Autowired
    SMPort smPort

    void execute(@Valid List<Integer> combinationProposed) {
        if(!combinationProposed.empty) {
            LsmBatch batch = settlementRepository.findById(combinationProposed.first()).get().lsmBatch
            List<Settlement> settlements = checkLsmBatchExistsDomainService.execute(batch.id).settlements
            List<Settlement> proposedSettlements = settlementRepository.findAllById(combinationProposed)
            if (!settlements.empty) {
                List<String> addresses = proposedSettlements.collect { [it.buyer.dltAddress, it.seller.dltAddress] }
                    .flatten().toSet().toList()
                AccountBalancesQueryModel balancesQueryModel = queryBus.executeAndWait(new GetBalanceQuery(
                    addresses: addresses
                ))
                Map<String, Balance> balances = [:]
                balancesQueryModel.balances.forEach { balance ->
                    String userId = userRepository.findByDltAddress(balance.userAddress).get().id
                    balances[userId] = new Balance(
                        cashAmount: balance.cashToken,
                        tokenAmount: balance.securityToken
                    )
                }

                if(
                    netUtils.isValidBalancesCombination(
                        proposedSettlements.collect { settlement ->
                            new IASettlement(
                                tokenAmount: settlement.securityAmount,
                                cashAmount: settlement.cashAmount,
                                buyer: settlement.buyer.id,
                                seller: settlement.seller.id,
                                id: settlement.id
                            )
                        },
                         balances
                    )
                ) {
                    commandBus.executeAndWait(
                        new PersistLsmNetCommand(
                            settlementIds: proposedSettlements*.id,
                            batchId: batch.id,
                            aiOutput: combinationProposed.join(", ")
                        )
                    )
                    counter.set(0)
                }
                else {
                    if(counter.get() < 5) {
                        Integer counter3 = counter.get()
                        log.info("No valid combination was found, retrying...")
                        smPort.makeSMRequest(new SMRequest(
                            balances: balances.collect { clientId, balance ->
                                new SMBalance(client: clientId, cashAmount: balance.cashAmount, tokenAmount: balance.tokenAmount)
                            },
                            settlements: settlements.collect { settlement ->
                                new SMSettlement(
                                    tokenAmount: settlement.securityAmount,
                                    cashAmount: settlement.cashAmount,
                                    buyer: settlement.buyer.id,
                                    seller: settlement.seller.id,
                                    id: settlement.id
                                )
                            }
                        ))
                        counter.incrementAndGet()
                    }
                    else {
                        log.error('No valid combination was found after 5 retries')
                    }
                }


            }
        }

    }

}
