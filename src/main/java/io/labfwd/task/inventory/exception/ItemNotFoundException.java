package io.labfwd.task.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception models the error thrown when the application cannot find the given {@link
 * io.labfwd.task.inventory.model.entity.Item} entity in the database
 *
 * @author Nikhil Vibhav
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Unable to find item with the given id")
public class ItemNotFoundException extends EntityNotFoundException {

  public ItemNotFoundException(String message) {
    super(message);
  }
}
