package io.builders.demo.core.command

import io.builders.demo.core.event.Event
import io.builders.demo.core.event.Publishable

import java.time.Instant

abstract class Command<R extends Event> implements Publishable {

    Long timestamp = Instant.now().toEpochMilli()
    final String commandType = this.class.simpleName

}
