package io.builders.demo.dtcc.infrastructure.listener.lsm.calculateoperations

import io.builders.demo.core.event.EventListener
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmNetCalculatedEvent

interface CalculateLsmOperationsPort extends EventListener<LsmNetCalculatedEvent> {

}
