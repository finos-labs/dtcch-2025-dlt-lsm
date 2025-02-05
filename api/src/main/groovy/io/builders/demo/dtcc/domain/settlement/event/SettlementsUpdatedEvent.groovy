package io.builders.demo.dtcc.domain.settlement.event

import io.builders.demo.core.event.Event

class SettlementsUpdatedEvent implements Event {

    List<Integer> settlementIds

}
