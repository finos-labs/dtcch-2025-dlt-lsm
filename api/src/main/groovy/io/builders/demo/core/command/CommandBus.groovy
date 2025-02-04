package io.builders.demo.core.command

import io.builders.demo.core.event.Event

import java.time.Duration
import java.util.concurrent.CompletableFuture

interface CommandBus {

    def <R extends Event, C extends Command<R>> R executeAndWait(C command)

    def <R extends Event, C extends Command<R>> R executeAndWait(C command, String prefix)

    def <R extends Event, C extends Command<R>> R executeAndWait(C command, Duration timeout)

    def <R extends Event, C extends Command<R>> R executeAndWait(C command, Duration timeout, String prefix)

    def <R extends Event, C extends Command<R>> CompletableFuture<R> execute(C command, String prefix)

    def <R extends Event, C extends Command<R>> CompletableFuture<R> execute(C command)

}
