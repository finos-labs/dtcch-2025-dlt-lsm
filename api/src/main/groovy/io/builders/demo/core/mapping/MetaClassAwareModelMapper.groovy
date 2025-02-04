package io.builders.demo.core.mapping

import org.modelmapper.ModelMapper
import org.modelmapper.internal.util.Primitives

import java.lang.reflect.Field

import groovy.transform.CompileDynamic

@CompileDynamic
class MetaClassAwareModelMapper extends ModelMapper {

    @Override
    <D> D map(Object source, Class<D> destinationType) {
        D result = super.map(source, destinationType)
        correctMetaClass(result)
        result
    }

    @Override
    void map(Object source, Object destination) {
        super.map(source, destination)
        correctMetaClass(destination)
    }

    private static void correctMetaClass(Object result, boolean skipCheck = false) {
        if (skipCheck || checkMetaClass(result)) {
            processMetaClass(result)
            processProperties(result)
        }
    }

    private static void processProperties(Object result) {
        Field[] fields = result.class.declaredFields?.findAll { Field field -> !field.synthetic }
        if (fields && fields.size() > 0) {
            fields.each { property ->
                String propertyName = property.name
                if (checkMetaClass(result[propertyName])) {
                    correctMetaClass(result[propertyName], true)
                } else if (checkClass(result[propertyName]) && result[propertyName].class.isAssignableFrom(ArrayList)) {
                    result[propertyName].eachWithIndex { ignored, index ->
                        processMetaClass(result[propertyName][index])
                    }
                }
            }
        }
    }

    private static void processMetaClass(Object result) {
        MetaClass metaClass = new MetaClassImpl(result.class)
        metaClass.initialize()
        result.metaClass = metaClass
    }

    private static boolean checkClass(Object object) {
        object && object.class && !Primitives.isPrimitive(object.class)
    }

    private static boolean checkMetaClass(Object object) {
        checkClass(object)
            && MetaObjectProtocol.isAssignableFrom(object.metaClass.class)
            && object.metaClass.theClass != object.class
    }

}
