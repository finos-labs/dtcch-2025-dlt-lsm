package io.builders.demo.core.event

import java.time.Instant

trait Event {

    Long timestamp = Instant.now().toEpochMilli()
    final String eventType = this.class.simpleName

}
