package io.builders.demo.core.command

import io.builders.demo.core.event.Event
import jakarta.validation.Valid

interface CommandHandler<R extends Event, C extends Command<R>> {

    R handle(@Valid C command)

}
