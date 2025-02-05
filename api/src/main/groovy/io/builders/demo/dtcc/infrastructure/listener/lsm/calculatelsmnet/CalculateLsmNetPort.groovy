package io.builders.demo.dtcc.infrastructure.listener.lsm.calculatelsmnet

import io.builders.demo.core.event.EventListener
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmBatchStartedEvent

interface CalculateLsmNetPort extends EventListener<LsmBatchStartedEvent> {

}
