package io.builders.demo.core.event

interface EventBus {

    <E extends Event> E publish(E event)

}
