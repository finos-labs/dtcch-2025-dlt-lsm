package io.builders.demo.dtcc.application.query.getlsmnetresult

import groovy.util.logging.Slf4j
import io.builders.demo.core.query.QueryHandler
import io.builders.demo.dtcc.application.query.common.GetAiCombinationQueryModel
import io.builders.demo.integration.IAPort
import io.builders.demo.integration.model.Balance
import io.builders.demo.integration.model.IARequest
import io.builders.demo.integration.model.IASettlement
import jakarta.validation.Valid
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@Slf4j
class GetLsmNetResultQueryHandler implements QueryHandler<GetAiCombinationQueryModel, GetLsmNetResultQuery> {

    @Autowired
    IAPort iaPort

    @Autowired
    ModelMapper modelMapper

    @Override
    GetAiCombinationQueryModel handle(@Valid GetLsmNetResultQuery query) {
        List<String> combinationProposed = []
        List<IASettlement> settlementProposed = []
        Boolean isValid = false
        Integer tries = 5
        while (!isValid && tries > 0) {
            log.debug("[Try-${5 - tries}] Trying to obtain a valid combination of settlements")
            combinationProposed = iaPort.obtainIACombination(new IARequest(settlements: query.settlements, balances: query.balances))
            settlementProposed = query.settlements.findAll { combinationProposed.contains(it.id.toString()) }
            isValid = isValidBalancesCombination(settlementProposed, query.balances)
            tries--
        }
        if (!isValid) {
            log.error('Error when trying to obtain a valid combination of settlements')
        } else {
            log.error("Valid combination found! ${combinationProposed.join(',')}")
        }
        return new GetAiCombinationQueryModel(settlements: settlementProposed, aiResult: combinationProposed.join(','))
    }

    private static Map<String, BigDecimal> calculateFinalBalances(Map<String, BigDecimal> balances, Map<String, BigDecimal> amounts) {
        def totalSet = (balances + amounts).collectEntries { key, value ->
            if (balances.containsKey(key) && amounts.containsKey(key)) {
                [(key): balances[key] + amounts[key]]
            } else {
                [(key): value]
            }
        }
        return totalSet
    }

    private static Map<String, BigDecimal> filterNegative(Map<String, BigDecimal> finalCashBalances) {
        return finalCashBalances.findAll { key, value -> value < 0 }
    }

    private static Boolean isValidBalancesCombination(List<IASettlement> attemptToValidate, Map<String, Balance> balancesMap) {
        balancesMap = balancesMap.sort { it.key }
        Set<String> clients = [attemptToValidate*.buyer, attemptToValidate*.seller].flatten() as Set<String>
        clients = clients.sort { it }
        Map<String, BigDecimal> requiredCashAttempt = [:]
        Map<String, BigDecimal> requiredTokenAttempt = [:]
        clients.each { String client ->
            requiredCashAttempt[client] = attemptToValidate.sum { it.cashAmountByClient(client) }
            requiredTokenAttempt[client] = attemptToValidate.sum { it.tokenAmountByClient(client) }
        }

        requiredCashAttempt = requiredCashAttempt.sort { it.key }
        requiredTokenAttempt = requiredTokenAttempt.sort { it.key }
        def availableCashBalances = balancesMap.collectEntries { key, value -> [(key): value.cashAmount] }
        def availableTokenBalances = balancesMap.collectEntries { key, value -> [(key): value.tokenAmount] }
        def finalCashBalances = calculateFinalBalances(availableCashBalances, requiredCashAttempt)
        def finalTokenBalances = calculateFinalBalances(availableTokenBalances, requiredTokenAttempt)

        if (filterNegative(finalCashBalances).isEmpty() && filterNegative(finalTokenBalances).isEmpty()) {
            log.debug("Available Cash and Token Balances for ALL clients")
            log.debug("RequiredCash ${requiredCashAttempt}")
            log.debug("AvailableCashBalances ${availableCashBalances}")
            log.debug("FinalCashBalances ${finalCashBalances}")
            log.debug("RequiredToken ${requiredTokenAttempt}")
            log.debug("AvailableTokenBalances ${availableTokenBalances}")
            log.debug("FinalTokenBalances ${finalTokenBalances}")

            return Boolean.TRUE
        } else {
            log.debug("clients ${clients}")
            if (!filterNegative(finalCashBalances).isEmpty()) {
                log.debug("Not available CashBalances for ${filterNegative(finalCashBalances)}")
                log.debug("RequiredCash ${requiredCashAttempt}")
                log.debug("AvailableCashBalances ${availableCashBalances}")
                log.debug("FinalCashBalances ${finalCashBalances}")
            }
            if (!filterNegative(finalTokenBalances).isEmpty()) {
                log.debug("Not available TokenBalances for ${filterNegative(finalTokenBalances)}")
                log.debug("RequiredToken ${requiredTokenAttempt}")
                log.debug("AvailableTokenBalances ${availableTokenBalances}")
                log.debug("FinalTokenBalances ${finalTokenBalances}")
            }
            return Boolean.FALSE
        }

    }

}
