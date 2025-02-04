package io.builders.demo.surikata.application.event.converter

import io.builders.demo.core.event.DltEvent
import net.consensys.eventeum.dto.event.ContractEventDetails
import org.modelmapper.AbstractConverter

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

abstract class ContractEventDetailsConverter<E extends DltEvent>
    extends AbstractConverter<ContractEventDetails, E> {

    OffsetDateTime convertToOffsetDateTime(BigInteger timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp.longValue())
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC)
    }

    E includeTime(ContractEventDetails contractEventDetails, E converted) {
        converted.timeStamp = contractEventDetails.timestamp ? convertToOffsetDateTime(contractEventDetails.timestamp) : null
        converted.blockTimestamp = contractEventDetails.blockTimestamp ? convertToOffsetDateTime(contractEventDetails.blockTimestamp) : null
        converted
    }

}
