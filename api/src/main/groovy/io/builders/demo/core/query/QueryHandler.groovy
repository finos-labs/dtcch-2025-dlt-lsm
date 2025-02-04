package io.builders.demo.core.query

import jakarta.validation.Valid

interface QueryHandler<R, Q extends Query<R>> {

    R handle(@Valid Q query)

}
