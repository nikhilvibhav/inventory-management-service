package io.labfwd.task.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Models the exception thrown when the attributes in the item do not match the category
 *
 * @author Nikhil Vibhav
 */
@ResponseStatus(
    code = HttpStatus.INTERNAL_SERVER_ERROR,
    reason = "Invalid attributes in the item for the associated category")
public class InvalidAttributesException extends Exception {

  public InvalidAttributesException(String message) {
    super(message);
  }
}
