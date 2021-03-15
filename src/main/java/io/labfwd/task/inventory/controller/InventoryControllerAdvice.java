package io.labfwd.task.inventory.controller;

import io.labfwd.task.inventory.exception.EntityNotFoundException;
import io.labfwd.task.inventory.model.valueobject.ErrorResponse;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * REST Controller Advice for handling exception scenarios
 *
 * @author Nikhil Vibhav
 */
@RestControllerAdvice
@Log4j2
public class InventoryControllerAdvice extends ResponseEntityExceptionHandler {

  /**
   * Handles the {@link MethodArgumentNotValidException}
   *
   * @param ex - instance of {@link MethodArgumentNotValidException}
   * @param headers - the HTTP headers
   * @param status - the HTTP status
   * @param request - the incoming request
   * @return the {@link ResponseEntity} containing the error response @param headers
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {

    log.info("Handling MethodArgumentNotValidException", ex);

    final List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());

    final ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "The request failed validation checks. Error count - " + errors.size(),
            errors);

    return new ResponseEntity<>(errorResponse, headers, status);
  }

  /**
   * Handles the {@link ConstraintViolationException}
   *
   * @param ex - instance of {@link ConstraintViolationException}
   * @return the {@link ResponseEntity} containing the error response
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(
      final ConstraintViolationException ex) {

    log.info("Handling ConstraintViolationException", ex);

    final List<String> errors =
        ex.getConstraintViolations().stream()
            .map(
                violation ->
                    violation.getRootBeanClass().getSimpleName()
                        + "#"
                        + violation.getPropertyPath()
                        + ": "
                        + violation.getMessage())
            .collect(Collectors.toList());

    final ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "The request failed validation checks. Error count - " + errors.size(),
            errors);

    return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
  }

  /**
   * Handles the {@link EntityNotFoundException}
   *
   * @param ex - instance of {@link EntityNotFoundException}
   * @return the {@link ResponseEntity} containing the error response
   */
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
      final EntityNotFoundException ex) {

    log.info("Handling EntityNotFound exception", ex);

    final ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "The required entity was not found",
            Collections.singletonList(ex.getMessage()));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  /**
   * Handles all the uncaught exceptions
   *
   * @param exception - instance of {@link Exception}
   * @return the {@link ResponseEntity} containing the error response
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleAllUncaughtException(final Exception exception) {

    log.error("Handling unknown error", exception);

    final ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unknown error occurred",
            Collections.singletonList(exception.getMessage()));

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  /**
   * Builds the error response model
   *
   * @param status - the Http Status
   * @param message - The error response message
   * @param errors - the exception message
   * @return an instance of {@link ErrorResponse}
   */
  private ErrorResponse buildErrorResponse(HttpStatus status, String message, List<String> errors) {

    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(status.value());
    errorResponse.setTimestamp(new Date());
    errorResponse.setMessage(message);
    errorResponse.setErrors(errors);

    return errorResponse;
  }
}
