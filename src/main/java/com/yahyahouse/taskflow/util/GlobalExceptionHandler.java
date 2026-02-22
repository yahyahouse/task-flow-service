package com.yahyahouse.taskflow.util;

import com.yahyahouse.taskflow.util.transaction.TransactionContext;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TaskFlowException.class)
    public ResponseEntity<ErrorResponse> handleTaskFlowException(
            TaskFlowException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = resolveStatus(ex.getHttpStatusCode());
        return buildErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<String> messages = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": "
                        + (fieldError.getDefaultMessage() == null ? "invalid value" : fieldError.getDefaultMessage()))
                .distinct()
                .toList();

        String errorMessage = messages.isEmpty() ? "Validation failed" : "Validation failed - " + String.join(", ", messages);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid request payload");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse response = new ErrorResponse();
        response.setHttpStatus(status.value());
        response.setErrorMessage(message);
        response.setTransactionId(TransactionContext.currentOrGenerate());
        return ResponseEntity.status(status).body(response);
    }

    private HttpStatus resolveStatus(int statusCode) {
        try {
            return HttpStatusCode.valueOf(statusCode) instanceof HttpStatus httpStatus
                    ? httpStatus
                    : HttpStatus.INTERNAL_SERVER_ERROR;
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
