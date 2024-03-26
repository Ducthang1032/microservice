package ndt.project.authservice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EncodingDataInvalidException extends RuntimeException {

    public EncodingDataInvalidException(final String message) {
        super(message);
    }
}