package io.labfwd.task.inventory.repository;

import io.labfwd.task.inventory.model.entity.Item;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for performing CRUD operations on the {@link Item} entity
 *
 * @author Nikhil Vibhav
 */
@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

  /**
   * JPA method to find the items by the category name
   *
   * @param category - the {@link io.labfwd.task.inventory.model.entity.Category#name}
   * @return a list of items against the category
   */
  List<Item> findByCategory_Name(final String category);
}
