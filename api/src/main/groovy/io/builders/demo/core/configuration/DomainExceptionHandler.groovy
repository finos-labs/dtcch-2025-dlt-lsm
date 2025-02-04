package io.builders.demo.core.configuration

import io.builders.demo.core.domain.exception.EntityNotFoundDomainException
import io.builders.demo.core.domain.exception.UnsupportedOperationDomainException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

import groovy.util.logging.Slf4j

@Slf4j
@ControllerAdvice
class DomainExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = [RuntimeException])
    protected ResponseEntity<Object> handle(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR
        String detail = 'Unexpected error'
        switch (true) {
            case EntityNotFoundDomainException.isAssignableFrom(ex.class):
                detail = ex.message
                status = HttpStatus.NOT_FOUND
                break
            case UnsupportedOperationDomainException.isAssignableFrom(ex.class):
                detail = ex.message
                status = HttpStatus.CONFLICT
                break
            default:
                log.error(detail, ex)
                break
        }
        DomainExceptionResponse response = new DomainExceptionResponse(
            detail: detail,
            status: status.value(),
            title: status.reasonPhrase,
            instance: (request as ServletWebRequest).request.requestURI
        )
        handleExceptionInternal(ex, response, new HttpHeaders(), status, request)
    }

}
