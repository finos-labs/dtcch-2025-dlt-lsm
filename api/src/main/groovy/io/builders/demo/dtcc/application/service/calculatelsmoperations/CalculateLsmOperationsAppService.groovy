package io.builders.demo.dtcc.application.service.calculatelsmoperations

import io.builders.demo.core.command.CommandBus
import io.builders.demo.dtcc.application.dlt.command.executelsm.OrderExecuteLsmCommand
import io.builders.demo.dtcc.application.dlt.common.Transaction
import io.builders.demo.dtcc.domain.lsmbatch.LsmBatch
import io.builders.demo.dtcc.domain.lsmbatch.service.CheckLsmBatchExistsDomainService
import io.builders.demo.dtcc.domain.settlement.Settlement
import io.builders.demo.dtcc.infrastructure.configuration.DtccConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CalculateLsmOperationsAppService {

    @Autowired
    CheckLsmBatchExistsDomainService checkLsmBatchExistsDomainService

    @Autowired
    CommandBus commandBus

    @Autowired
    DtccConfigurationProperties dtccConfigurationProperties

    void calculate(Integer lsmId) {
        LsmBatch lsmBatch = checkLsmBatchExistsDomainService.getLsmBatch(lsmId)
        Map<String, Map<String, BigDecimal>> netBalances = calculateNetBalances(lsmBatch.settlements)

        List<Transaction> transactions = transformNetToTransactions(netBalances.security, netBalances.cash)

        commandBus.executeAndWait(new OrderExecuteLsmCommand(transactions: transactions))
    }

    Map<String, Map<String, BigDecimal>> calculateNetBalances(List<Settlement> settlements) {
        Map<String, BigDecimal> netSecurity = [:].withDefault { BigDecimal.ZERO }
        Map<String, BigDecimal> netCash = [:].withDefault { BigDecimal.ZERO }

        settlements.each { settlement ->
            netSecurity[settlement.seller.dltAddress] -= settlement.securityAmount
            netSecurity[settlement.buyer.dltAddress] += settlement.securityAmount

            netCash[settlement.seller.dltAddress] += settlement.cashAmount
            netCash[settlement.buyer.dltAddress] -= settlement.cashAmount
        }

        return [security: netSecurity, cash: netCash]
    }

    List<Transaction> transformNetToTransactions(
        Map<String, BigDecimal> securityBalances,
        Map<String, BigDecimal> cashBalances
    ) {
        List<Transaction> toOmnibus = []
        List<Transaction> fromOmnibus = []

        securityBalances.each { user, amount ->
            if (amount < BigDecimal.ZERO) {
                toOmnibus << new Transaction(
                    fromAddress: user,
                    toAddress: dtccConfigurationProperties.omnibusAccount,
                    amount: amount.abs(),
                    tokenAddress: dtccConfigurationProperties.securityToken
                )
            } else if (amount > BigDecimal.ZERO) {
                fromOmnibus << new Transaction(
                    fromAddress: dtccConfigurationProperties.omnibusAccount,
                    toAddress: user,
                    amount: amount.abs(),
                    tokenAddress: dtccConfigurationProperties.securityToken
                )
            }
        }

        cashBalances.each { user, amount ->
            if (amount < BigDecimal.ZERO) {
                toOmnibus << new Transaction(
                    fromAddress: user,
                    toAddress: dtccConfigurationProperties.omnibusAccount,
                    amount: amount.abs(),
                    tokenAddress: dtccConfigurationProperties.cashToken
                )
            } else if (amount > BigDecimal.ZERO) {
                fromOmnibus << new Transaction(
                    fromAddress: dtccConfigurationProperties.omnibusAccount,
                    toAddress: user,
                    amount: amount.abs(),
                    tokenAddress: dtccConfigurationProperties.cashToken
                )
            }
        }

        return toOmnibus + fromOmnibus
    }

}
