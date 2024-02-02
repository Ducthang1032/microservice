package ndt.project.discovery.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@ControllerAdvice
@Slf4j
public class ExceptionTranslator {

    @ExceptionHandler
    public void handleException(Exception ex) {
        log.warn("Error", ex);
    }

}
