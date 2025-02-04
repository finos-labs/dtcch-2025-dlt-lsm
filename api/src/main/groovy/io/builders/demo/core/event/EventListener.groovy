package io.builders.demo.core.event

interface EventListener<E extends Event> {

    void receive(E event)

}
