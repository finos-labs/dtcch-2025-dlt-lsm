package io.builders.demo.dtcc.application.query.getpendingsettlements

import io.builders.demo.core.query.QueryHandler
import io.builders.demo.dtcc.application.query.common.SettlementQueryModel
import io.builders.demo.dtcc.domain.settlement.Settlement
import io.builders.demo.dtcc.domain.settlement.SettlementRepository
import io.builders.demo.dtcc.domain.settlement.SettlementStatus
import jakarta.validation.Valid
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetPendingSettlementsQueryHandler implements QueryHandler<List<SettlementQueryModel>,
    GetPendingSettlementsQuery> {

    @Autowired
    SettlementRepository settlementRepository

    @Autowired
    ModelMapper modelMapper

    @Override
    List<SettlementQueryModel> handle(@Valid GetPendingSettlementsQuery query) {
        List<Settlement> settlements = this.settlementRepository.findAllByStatusIs(SettlementStatus.PENDING)

        settlements.collect {
            this.modelMapper.map(it, SettlementQueryModel)
        }
    }

}
