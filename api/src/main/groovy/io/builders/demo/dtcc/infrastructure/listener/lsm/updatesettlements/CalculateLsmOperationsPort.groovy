package io.builders.demo.dtcc.infrastructure.listener.lsm.updatesettlements

import io.builders.demo.core.event.EventListener
import io.builders.demo.dtcc.domain.lsmbatch.event.LsmExecutedDltEvent

interface CalculateLsmOperationsPort extends EventListener<LsmExecutedDltEvent> {

}
