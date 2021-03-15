package io.labfwd.task.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception models the error thrown when the application cannot find the given {@link
 * io.labfwd.task.inventory.model.entity.Category} entity in the database
 *
 * @author Nikhil Vibhav
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Unable to find the given category")
public class CategoryNotFoundException extends EntityNotFoundException {

  public CategoryNotFoundException(String message) {
    super(message);
  }
}
