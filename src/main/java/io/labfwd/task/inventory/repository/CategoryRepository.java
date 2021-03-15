package io.labfwd.task.inventory.repository;

import io.labfwd.task.inventory.model.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Repository for performing CRUD operations on the {@link Category} entity
 *
 * @author Nikhil Vibhav
 */
@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {}
