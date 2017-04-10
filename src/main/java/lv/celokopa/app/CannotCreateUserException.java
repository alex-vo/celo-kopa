package lv.celokopa.app;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by alex on 16.10.10.
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class CannotCreateUserException extends RuntimeException {
    private String errorMessage;
    public CannotCreateUserException(String em){
        this.errorMessage = em;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
