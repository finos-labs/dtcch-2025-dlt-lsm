package io.builders.demo.dtcc.application.service.calculatelsmnet

import io.builders.demo.core.command.CommandBus
import io.builders.demo.core.query.QueryBus
import io.builders.demo.dtcc.application.command.persistlsm.PersistLsmNetCommand
import io.builders.demo.dtcc.application.dlt.query.getbalance.AccountBalancesQueryModel
import io.builders.demo.dtcc.application.dlt.query.getbalance.GetBalanceQuery
import io.builders.demo.dtcc.application.query.common.GetAiCombinationQueryModel
import io.builders.demo.dtcc.application.query.getlsmnetresult.GetLsmNetResultQuery
import io.builders.demo.dtcc.domain.lsmbatch.service.CheckLsmBatchExistsDomainService
import io.builders.demo.dtcc.domain.settlement.Settlement
import io.builders.demo.dtcc.domain.user.UserRepository
import io.builders.demo.integration.model.Balance
import io.builders.demo.integration.model.IASettlement
import io.builders.demo.integration.sagemaker.SMBalance
import io.builders.demo.integration.sagemaker.SMPort
import io.builders.demo.integration.sagemaker.SMRequest
import io.builders.demo.integration.sagemaker.SMSettlement
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import groovy.util.logging.Slf4j

@Service
@Slf4j
@SuppressWarnings(['UseCollectMany'])
class CalculateLsmNetAppService {

    @Autowired
    QueryBus queryBus

    @Autowired
    CommandBus commandBus

    @Autowired
    CheckLsmBatchExistsDomainService checkLsmBatchExistsDomainService

    @Autowired
    UserRepository userRepository

    @Autowired
    SMPort smPort

    void execute(@Valid CalculateLsmNetAppServiceModel model) {
        List<Settlement> settlements = checkLsmBatchExistsDomainService.execute(model.batchId).settlements
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
            if (true) {
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
            }
            else {
                GetAiCombinationQueryModel selectedSettlements = queryBus.executeAndWait(new GetLsmNetResultQuery(
                settlements: settlements.collect { settlement ->
                    new IASettlement(
                        tokenAmount: settlement.securityAmount,
                        cashAmount: settlement.cashAmount,
                        buyer: settlement.buyer.id,
                        seller: settlement.seller.id,
                        id: settlement.id
                    )
                },
                balances: balances
            ))
            if (!selectedSettlements.settlements.empty) {
                commandBus.executeAndWait(
                    new PersistLsmNetCommand(
                        settlementIds: selectedSettlements.settlements*.id,
                        batchId: model.batchId,
                        aiOutput: selectedSettlements.aiResult
                    )
                )
            }
            }
            //TODO: Add FT for async AI call
//            GetAiCombinationQueryModel selectedSettlements = queryBus.executeAndWait(new GetLsmNetResultQuery(
//                settlements: settlements.collect { settlement ->
//                    new IASettlement(
//                        tokenAmount: settlement.securityAmount,
//                        cashAmount: settlement.cashAmount,
//                        buyer: settlement.buyer.id,
//                        seller: settlement.seller.id,
//                        id: settlement.id
//                    )
//                },
//                balances: balances
//            ))
//            if (!selectedSettlements.settlements.empty) {
//                commandBus.executeAndWait(
//                    new PersistLsmNetCommand(
//                        settlementIds: selectedSettlements.settlements*.id,
//                        batchId: model.batchId,
//                        aiOutput: selectedSettlements.aiResult
//                    )
//                )
//            }
        }
    }

}
