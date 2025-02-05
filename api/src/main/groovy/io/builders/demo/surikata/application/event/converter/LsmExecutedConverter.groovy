package io.builders.demo.surikata.application.event.converter

import io.builders.demo.dtcc.domain.lsmbatch.event.LsmExecutedDltEvent
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmTransaction
import net.consensys.eventeum.dto.event.ContractEventDetails
import net.consensys.eventeum.dto.event.parameter.EventParameter
import net.consensys.eventeum.dto.event.parameter.NumberParameter
import org.springframework.stereotype.Component

@Component
class LsmExecutedConverter extends ContractEventDetailsConverter<LsmExecutedDltEvent> {

    @Override
    LsmExecutedDltEvent convert(ContractEventDetails contractEventDetails) {
        List<EventParameter> transactions = (List<EventParameter>) contractEventDetails.nonIndexedParameters[1].value
        LsmExecutedDltEvent event = new LsmExecutedDltEvent(
            transactionHash: contractEventDetails.transactionHash,
            contractAddress: contractEventDetails.address,
            batchId: (contractEventDetails.nonIndexedParameters[0] as NumberParameter).value,
            transactions: transactions.collect { tx ->
                List<EventParameter> values = (List<EventParameter>) tx.value
                new LsmTransaction(
                    from: values[0].value,
                    to: values[1].value,
                    amount: (values[2] as NumberParameter).value,
                    token: values[3].value
                )
            }
        )
        return includeTime(
            contractEventDetails,
            event
        )
    }

}
