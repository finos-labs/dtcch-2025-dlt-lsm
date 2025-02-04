package io.builders.demo.core.event

import io.builders.demo.core.reflections.ReflectionsUtils
import io.builders.demo.core.serialize.Serializer
import jakarta.annotation.PostConstruct
import org.reflections.Reflections
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Primary
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

import java.lang.reflect.Modifier

import groovy.util.logging.Slf4j

@Component
@Primary
@Slf4j
class MemoryEventBus implements EventBus {

    @Autowired
    private Reflections reflections

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor

    @Autowired
    private ApplicationContext applicationContext

    private final Map<String, List<Class<EventListener>>> eventListeners = [:]

    @PostConstruct
    void init() {
        Set<Class<EventListener>> subTypes = reflections.getSubTypesOf(EventListener).findAll { clazz ->
            !Modifier.isAbstract(clazz.modifiers) && !clazz.interface
        } as Set<Class<EventListener>>

        subTypes.each { eventListenerClass ->
            Class<Event> eventClass = ReflectionsUtils.findArgument(eventListenerClass, Event)
            if (eventClass) {
                registerListenerForHierarchy(eventClass, eventListenerClass)
            }
        }
    }

    @Override
    @SuppressWarnings(['CatchThrowable', 'PrintStackTrace'])
    <E extends Event> E publish(E event) {
        Set<Class<EventListener>> listenersToNotify = []

        // Traverse class hierarchy to collect listeners
        Class<?> currentClass = event.class
        while (currentClass != null && Event.isAssignableFrom(currentClass)) {
            List<Class<EventListener>> listeners = eventListeners.get(currentClass.simpleName)
            if (listeners != null) {
                listenersToNotify.addAll(listeners)
            }
            currentClass = currentClass.superclass
        }

        // Notify all collected listeners
        listenersToNotify.each { listenerClass ->
            EventListener listener = applicationContext.getBean(listenerClass)
            taskExecutor.execute {
                try {
                    listener.receive(event)
                } catch (Throwable throwable) {
                    throwable.printStackTrace()
                }
            }
        }

        log.info("Event ${event.class.simpleName} published with properties:${Serializer.serializeEvent(event)}")
        event
    }

    /**
     * Registers the listener for the event class and all its superclasses.
     */
    private void registerListenerForHierarchy(Class<Event> eventClass, Class<EventListener> listenerClass) {
        Class<?> currentClass = eventClass
        while (currentClass != null && Event.isAssignableFrom(currentClass)) {
            eventListeners.computeIfAbsent(currentClass.simpleName) { [] }.add(listenerClass)
            currentClass = currentClass.superclass
        }
    }

}
