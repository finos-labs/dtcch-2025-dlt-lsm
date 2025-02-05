package io.builders.demo.dtcc.application.service.calculatelsmnet

import groovy.util.logging.Slf4j
import io.builders.demo.core.command.CommandBus
import io.builders.demo.core.query.QueryBus
import io.builders.demo.dtcc.application.command.persistlsm.PersistLsmNetCommand
import io.builders.demo.dtcc.application.dlt.query.getbalance.AccountBalancesQueryModel
import io.builders.demo.dtcc.application.dlt.query.getbalance.GetBalanceQuery
import io.builders.demo.dtcc.application.query.getlsmnetresult.GetLsmNetResultQuery
import io.builders.demo.dtcc.domain.lsmbatch.service.CheckLsmBatchExistsDomainService
import io.builders.demo.dtcc.domain.settlement.Settlement
import io.builders.demo.dtcc.domain.user.UserRepository
import io.builders.demo.dtcc.domain.user.service.CheckUserExistsDomainService
import io.builders.demo.integration.model.Balance
import io.builders.demo.integration.model.IASettlement
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Slf4j
class CalculateLsmNetAppService {

    @Autowired
    QueryBus queryBus

    @Autowired
    CheckUserExistsDomainService checkUserExistsDomainService

    @Autowired
    CommandBus commandBus

    @Autowired
    CheckLsmBatchExistsDomainService checkLsmBatchExistsDomainService

    @Autowired
    UserRepository userRepository

    void execute(@Valid CalculateLsmNetAppServiceModel model) {
        List<Settlement> settlements = checkLsmBatchExistsDomainService.execute(model.batchId).settlements
        if (!settlements.empty) {
            List<String> addresses = settlements.collect { [it.buyer.dltAddress, it.seller.dltAddress] }.flatten().toSet().toList()
            AccountBalancesQueryModel balancesQueryModel = queryBus.executeAndWait(new GetBalanceQuery(
                    addresses: addresses
            ))
            Map<String, Balance> balances = [:]
            balancesQueryModel.balances.forEach { balance ->
                String userAlias = userRepository.findByDltAddress(balance.userAddress).get().alias
                balances[userAlias] = new Balance(
                        cashAmount: balance.cashToken,
                        tokenAmount: balance.securityToken
                )
            }
            List<IASettlement> selectedSettlements = queryBus.executeAndWait(new GetLsmNetResultQuery(
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
            if (!selectedSettlements.empty) {
                commandBus.executeAndWait(new PersistLsmNetCommand(settlementIds: selectedSettlements*.id, batchId: model.batchId), aiOutput: )
            }
        }
    }

}
