package io.builders.demo.dtcc.domain.lsmbatch.event

import io.builders.demo.core.event.Event

class LsmBatchStartedEvent implements Event {

    Integer id
    List<Integer> settlements

}
