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
import io.builders.demo.dtcc.domain.user.UserRepository
import io.builders.demo.dtcc.domain.utils.NetUtils
import io.builders.demo.integration.model.Balance
import io.builders.demo.integration.model.IASettlement
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Slf4j
@SuppressWarnings(['UseCollectMany'])
class ManageAIResponseAppService {

    static Integer counter = 0

    @Autowired
    QueryBus queryBus

    @Autowired
    CommandBus commandBus

    @Autowired
    CheckLsmBatchExistsDomainService checkLsmBatchExistsDomainService

    @Autowired
    UserRepository userRepository

    void execute(@Valid List<String> combinationProposed) {
        if(!combinationProposed.empty) {
            LsmBatch batch = checkLsmBatchExistsDomainService.executeBySettlementId(Integer.valueOf(combinationProposed.first()))
            List<Settlement> settlements = checkLsmBatchExistsDomainService.execute(batch.id).settlements
            if (!settlements.empty) {
                List<String> addresses = settlements.collect { [it.buyer.dltAddress, it.seller.dltAddress] }
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
                    NetUtils.isValidBalancesCombination(
                        settlements.collect { settlement ->
                            new IASettlement(
                                tokenAmount: settlement.securityAmount,
                                cashAmount: settlement.cashAmount,
                                buyer: settlement.buyer.id,
                                seller: settlement.seller.id,
                                id: settlement.id
                            )
                        },
                        balances: balances
                    )
                ) {
                    if (!selectedSettlements.settlements.empty) {
                        commandBus.executeAndWait(
                            new PersistLsmNetCommand(
                                settlementIds: selectedSettlements.settlements*.id,
                                batchId: batch.id,
                                aiOutput: selectedSettlements.aiResult
                            )
                        )
                    }
                    counter = 0
                }
                else {
                    if(counter < 5) {
                        log.info("No valid combination was found, retrying...")
                        //call new AI
                        counter++
                    }
                    else {
                        log.error("No valid combination was found after 5 retries")
                    }
                }


            }
        }

    }

}
