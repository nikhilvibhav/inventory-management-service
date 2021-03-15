package io.labfwd.task.inventory.exception;

/**
 * Models the error thrown by the service layer when the entity is not found
 *
 * @author Nikhil Vibhav
 */
public class EntityNotFoundException extends Exception {

  public EntityNotFoundException(String message) {
    super(message);
  }
}
