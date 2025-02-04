package io.builders.demo.dtcc.application.dlt.query.getbalance

import io.builders.demo.blockchain.ContractLoader
import io.builders.demo.core.query.QueryHandler
import io.builders.demo.dtcc.application.dlt.FormatDecimals
import io.builders.demo.dtcc.application.dlt.common.AccountBalance
import jakarta.validation.Valid
import org.iobuilders.dtcc.contracts.DvpOrchestrator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetBalanceQueryHandler implements QueryHandler<AccountBalancesQueryModel, GetBalanceQuery> {

    @Autowired
    ContractLoader contractLoader

    @Override
    AccountBalancesQueryModel handle(@Valid GetBalanceQuery query) {
        DvpOrchestrator contract = contractLoader.load(DvpOrchestrator)
        List balance = contract.call_getBalances(query.addresses).send()
        new AccountBalancesQueryModel(balances: balance.collect {
            new AccountBalance(
                cashToken: FormatDecimals.toBigDecimal(it.cashToken),
                securityToken: FormatDecimals.toBigDecimal(it.securityToken),
                user: it.user
            )
        })
    }
}
