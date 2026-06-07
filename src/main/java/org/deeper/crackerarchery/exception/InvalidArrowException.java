package org.deeper.crackerarchery.exception;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidArrowException extends RuntimeException {
    public InvalidArrowException(String message) {
        super(message);
    }
}
