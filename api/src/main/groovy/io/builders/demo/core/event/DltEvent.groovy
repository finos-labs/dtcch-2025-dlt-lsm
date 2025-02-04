package io.builders.demo.core.event

import java.time.OffsetDateTime

abstract class DltEvent implements Event {

    String transactionHash
    String contractAddress
    OffsetDateTime blockTimestamp

}
