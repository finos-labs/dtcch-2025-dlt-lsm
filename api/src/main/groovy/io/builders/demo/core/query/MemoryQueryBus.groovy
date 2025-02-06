package io.builders.demo.core.query

import static io.builders.demo.core.serialize.Serializer.serializeQuery

import io.builders.demo.core.exception.QueryHandlerNotFoundException
import io.builders.demo.core.exception.QueryTimeoutException
import io.builders.demo.core.reflections.ReflectionsUtils
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
@SuppressWarnings(['MisorderedStaticImports'])
class MemoryQueryBus implements QueryBus {

    @Value('${queryBus.prefix:query}')
    private String queryPrefix

    @Value('${queryBus.defaultTimeout:5s}')
    private Duration defaultTimeout

    @Autowired
    private Reflections reflections

    @Autowired
    private ApplicationContext applicationContext

    private final Map<String, Class<QueryHandler>> queryHandlers = [:]

    @PostConstruct
    void init() {
        Set<Class<QueryHandler>> subTypes = reflections.getSubTypesOf(QueryHandler).findAll { clazz ->
            !Modifier.isAbstract(clazz.modifiers) && !clazz.interface
        } as Set<Class<QueryHandler>>
        subTypes.each { classHandler ->
            Class<Query> queryClass = ReflectionsUtils.findArgument(classHandler, Query)
            if (queryClass) {
                queryHandlers.put(queryClass.name, classHandler)
            }
        }
    }

    @Override
    <R, Q extends Query<R>> R executeAndWait(Q query, String prefix = queryPrefix) {
        Long startTime = System.currentTimeMillis()
        R response = this.executeAndWait(query, defaultTimeout, prefix)
        log.info("""Query ${query.class.simpleName} with properties:${serializeQuery(query)}
                    executed in ${System.currentTimeMillis() - startTime} ms""")
        response
    }

    @Override
    <R, Q extends Query<R>> R executeAndWait(Q query, Duration timeout, String prefix = queryPrefix) {
        try {
            if (!queryHandlers.containsKey(query.class.name)) {
                throw new QueryHandlerNotFoundException(query.class.name)
            }
            return this.execute(query, prefix).orTimeout(timeout.toNanos(), TimeUnit.NANOSECONDS).get()
        } catch (ExecutionException ex) {
            if (TimeoutException.isAssignableFrom(ex.cause.class)) {
                throw new QueryTimeoutException()
            }
            throw ex.cause
        }
    }

    @Override
    @SuppressWarnings('CatchThrowable')
    <R, Q extends Query<R>> CompletableFuture<R> execute(Q query, String prefix = queryPrefix) {
        CompletableFuture<R> future = new CompletableFuture<>()

        try {
            Class<QueryHandler> queryHandlerClass = this.queryHandlers.get(query.class.name)
            QueryHandler queryHandler = applicationContext.getBean(queryHandlerClass)
            R result = queryHandler.handle(query) as R
            future.complete(result)
        } catch (Throwable ex) {
            future.completeExceptionally(ex)
        }

        return future
    }

}
