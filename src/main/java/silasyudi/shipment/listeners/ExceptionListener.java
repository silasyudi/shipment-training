package silasyudi.shipment.listeners;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import silasyudi.shipment.dtos.system.ApiErrorDto;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionListener {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorDto> handleRulesExceptions(ResponseStatusException exception) {
        return buildResponse(
                HttpStatus.valueOf(exception.getStatusCode().value()),
                Objects.requireNonNullElse(exception.getReason(), exception.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleValidationsExceptions(MethodArgumentNotValidException exception) {
        String message = exception.getAllErrors().stream()
                .filter(FieldError.class::isInstance)
                .map(FieldError.class::cast)
                .sorted(Comparator.comparing(FieldError::getField))
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleGenericExceptions(Exception exception) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ResponseEntity<ApiErrorDto> buildResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(
                new ApiErrorDto(status.value(), status.getReasonPhrase(), message),
                status
        );
    }
}
