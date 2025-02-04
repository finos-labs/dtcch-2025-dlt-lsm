package io.builders.demo.core.exception

class QueryHandlerNotFoundException extends RuntimeException {

    QueryHandlerNotFoundException(String query) {
        super(String.format('Query handler not found for query %s.', query))
    }

}
