package io.builders.demo.core.query

import java.time.Duration
import java.util.concurrent.CompletableFuture

interface QueryBus {

    def <R, Q extends Query<R>> R executeAndWait(Q query)

    def <R, Q extends Query<R>> R executeAndWait(Q query, String prefix)

    def <R, Q extends Query<R>> R executeAndWait(Q query, Duration timeout, String prefix)

    def <R, Q extends Query<R>> R executeAndWait(Q query, Duration timeout)

    def <R, Q extends Query<R>> CompletableFuture<R> execute(Q query)

    def <R, Q extends Query<R>> CompletableFuture<R> execute(Q query, String prefix)

}
