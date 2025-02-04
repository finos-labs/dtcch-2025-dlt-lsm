package io.builders.demo.core.event

abstract class ProcessTransitionCommonEvent extends ProcessCommonEvent {

    String previousStatus
    String currentStatus

}
