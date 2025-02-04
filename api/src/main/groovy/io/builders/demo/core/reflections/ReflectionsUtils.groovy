package io.builders.demo.core.reflections

import org.springframework.core.ResolvableType

class ReflectionsUtils {

    static <C> Class<C> findArgument(Class<?> clazz, Class<C> argumentClass) {
        ResolvableType resolvableType = ResolvableType.forClass(clazz)

        findMatchingGenericType(resolvableType, argumentClass)
    }

    static <C> Class<C> findMatchingGenericType(ResolvableType resolvableType, Class<C> argumentClass) {
        if (resolvableType.hasGenerics()) {
            for (ResolvableType generic : resolvableType.generics) {
                if (argumentClass.isAssignableFrom(generic.resolve())) {
                    return generic.resolve() as Class<C>
                }
            }
        }

        for (ResolvableType interfaceType : resolvableType.interfaces) {
            Class<C> result = findMatchingGenericType(interfaceType, argumentClass) as Class<C>
            if (result != null) {
                return result
            }
        }

        if (resolvableType.superType != ResolvableType.NONE) {
            return findMatchingGenericType(resolvableType.superType, argumentClass) as Class<C>
        }

        null
    }

}
