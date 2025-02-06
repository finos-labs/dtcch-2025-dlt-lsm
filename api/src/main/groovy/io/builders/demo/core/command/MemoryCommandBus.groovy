package io.builders.demo.core.command

import io.builders.demo.core.event.Event
import io.builders.demo.core.exception.CommandHandlerNotFoundException
import io.builders.demo.core.exception.CommandTimeoutException
import io.builders.demo.core.reflections.ReflectionsUtils
import io.builders.demo.core.serialize.Serializer
import jakarta.annotation.PostConstruct
import org.reflections.Reflections
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

import java.lang.reflect.Modifier
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

import groovy.util.logging.Slf4j

@Component
@Primary
@Slf4j
class MemoryCommandBus implements CommandBus {

    @Value('${commandBus.prefix:command}')
    private String commandPrefix

    @Value('${commandBus.defaultTimeout:5s}')
    private Duration defaultTimeout

    @Autowired
    private Reflections reflections

    @Autowired
    private ApplicationContext applicationContext

    private final Map<String, Class<CommandHandler>> commandHandlers = [:]

    @PostConstruct
    void init() {
        Set<Class<CommandHandler>> rawSubTypes = reflections.getSubTypesOf(CommandHandler) as Set<Class<CommandHandler>>
        Set<Class<CommandHandler>> subTypes = rawSubTypes.findAll { clazz ->
            !Modifier.isAbstract(clazz.modifiers) && !clazz.interface
        }.toSet()
        for (commandHandlerClass in subTypes) {
            Class<Command> commandClass = ReflectionsUtils.findArgument(commandHandlerClass, Command)
            if (commandClass) {
                commandHandlers.put(commandClass.name, commandHandlerClass)
            }
        }
    }

    @Override
    <R extends Event, C extends Command<R>> R executeAndWait(C command, String prefix = commandPrefix) {
        Long startTime = System.currentTimeMillis()
        R response = this.executeAndWait(command, defaultTimeout, prefix)
        log.info("""Command ${command.class.simpleName} with properties:${Serializer.serializeCommand(command)}
                    executed in ${System.currentTimeMillis() - startTime} ms""")
        response
    }

    @Override
    <R extends Event, C extends Command<R>> R executeAndWait(
        C command, Duration timeout,
        String prefix = commandPrefix
    ) {
        try {
            if (!commandHandlers.containsKey(command.class.name)) {
                throw new CommandHandlerNotFoundException(command.class.name)
            }
            return this.execute(command, prefix).orTimeout(timeout.toNanos(), TimeUnit.NANOSECONDS).get()
        } catch (ExecutionException ex) {
            if (TimeoutException.isAssignableFrom(ex.cause.class)) {
                throw new CommandTimeoutException()
            }
            throw ex.cause
        }
    }

    @Override
    @SuppressWarnings('CatchThrowable')
    <R extends Event, C extends Command<R>> CompletableFuture<R> execute(C command, String prefix = commandPrefix) {
        CompletableFuture<R> future = new CompletableFuture<>()

        try {
            Class<CommandHandler> commandHandlerClass = this.commandHandlers.get(command.class.name)
            CommandHandler commandHandler = applicationContext.getBean(commandHandlerClass)
            R result = commandHandler.handle(command) as R
            future.complete(result)
        } catch (Throwable ex) {
            future.completeExceptionally(ex)
        }

        return future
    }

}
