package io.builders.demo.core.query

import io.builders.demo.core.event.Publishable

import java.time.Instant

abstract class Query<R> implements Publishable {

    Long timestamp = Instant.now().toEpochMilli()
    final String queryType = this.class.simpleName

}
