package io.labfwd.task.inventory.service;

import io.labfwd.task.inventory.model.entity.Category;
import io.labfwd.task.inventory.repository.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer to interact with the database for the {@link Category} entity
 *
 * @author Nikhil Vibhav
 */
@Service
@Log4j2
public class CategoryService {

  private final CategoryRepository repository;

  @Autowired
  public CategoryService(final CategoryRepository repository) {
    this.repository = repository;
  }

  /**
   * Saved a {@link Category} entity to the database
   *
   * @param categoryToSave - the {@link Category} received in the request
   * @return the saved entity
   */
  public Category createCategory(final Category categoryToSave) {
    log.debug("Saving Category: {}", categoryToSave);
    return repository.save(categoryToSave);
  }
}
