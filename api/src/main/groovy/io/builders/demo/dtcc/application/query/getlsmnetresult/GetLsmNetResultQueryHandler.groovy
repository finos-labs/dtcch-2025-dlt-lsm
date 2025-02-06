package io.builders.demo.dtcc.application.query.getlsmnetresult

import io.builders.demo.core.query.QueryHandler
import io.builders.demo.dtcc.application.query.common.GetAiCombinationQueryModel
import io.builders.demo.dtcc.domain.utils.NetUtils
import io.builders.demo.integration.gemini.IAPort
import io.builders.demo.integration.model.IARequest
import io.builders.demo.integration.model.IASettlement
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import groovy.util.logging.Slf4j

@Component
@Slf4j
@SuppressWarnings(['UnnecessaryGetter', 'DuplicateNumberLiteral', 'LineLength', 'ParameterReassignment', 'UnnecessaryObjectReferences'])
class GetLsmNetResultQueryHandler implements QueryHandler<GetAiCombinationQueryModel, GetLsmNetResultQuery> {

    private static final Integer MAX_TRIES = 100

    @Autowired
    IAPort iaPort

    String separator = ','

    @Override
    GetAiCombinationQueryModel handle(@Valid GetLsmNetResultQuery query) {
        List<String> combinationProposed = []
        List<IASettlement> settlementProposed = []
        Boolean isValid = false
        Integer tries = MAX_TRIES
        while (!isValid && tries > 0) {
            log.debug("[Try-${MAX_TRIES - tries}] Trying to obtain a valid combination of settlements")
            combinationProposed = iaPort.obtainIACombination(new IARequest(settlements: query.settlements, balances: query.balances))
            settlementProposed = query.settlements.findAll { combinationProposed.contains(it.id.toString()) || combinationProposed.contains(it.id) }
            isValid = NetUtils.isValidBalancesCombination(settlementProposed, query.balances)
            tries--
        }

        if (isValid) {
            log.error("Valid combination found! ${combinationProposed.join(separator)}")
        } else {
            log.error('Error when trying to obtain a valid combination of settlements')
        }
        return new GetAiCombinationQueryModel(settlements: settlementProposed, aiResult: combinationProposed.join(separator))
    }

}
