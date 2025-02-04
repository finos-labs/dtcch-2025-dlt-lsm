package io.builders.demo.core.serialize

class Serializer {

    static Map<Object, Object> serializeEvent(Object event) {
        serialize(event)
    }

    static Map<Object, Object> serializeCommand(Object command) {
        serialize(command)
    }

    static Map<Object, Object> serializeQuery(Object query) {
        serialize(query)
    }

    private static Map<Object, Object> serialize(Object obj) {
        Map props = obj.properties
        props.findAll { Map.Entry entry -> entry.key != 'class' }
    }

}
