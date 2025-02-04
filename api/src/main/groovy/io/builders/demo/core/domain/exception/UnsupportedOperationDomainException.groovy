package io.builders.demo.core.domain.exception

class UnsupportedOperationDomainException extends DomainException {

    List<String> messages = []

    UnsupportedOperationDomainException(String message) {
        super(message)
    }

    UnsupportedOperationDomainException(List<String> messages) {
        super("Multiple messages unsupported operation exception: [${messages.join(', ')}]")
        this.messages = messages
    }

}
