package io.builders.demo.api.domain.lsmbatch.event

import io.builders.demo.core.event.DltEvent

class LsmExecutedDltEvent extends DltEvent {

    String transactionHash
    String contractAddress
    LsmTransaction[] transactions

}
