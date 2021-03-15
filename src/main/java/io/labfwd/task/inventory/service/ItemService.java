package io.labfwd.task.inventory.service;

import io.labfwd.task.inventory.exception.CategoryNotFoundException;
import io.labfwd.task.inventory.exception.InvalidAttributesException;
import io.labfwd.task.inventory.exception.ItemNotFoundException;
import io.labfwd.task.inventory.model.entity.Attribute;
import io.labfwd.task.inventory.model.entity.Category;
import io.labfwd.task.inventory.model.entity.Item;
import io.labfwd.task.inventory.model.mapper.ItemMapper;
import io.labfwd.task.inventory.repository.CategoryRepository;
import io.labfwd.task.inventory.repository.ItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer to interact with the database for {@link Item} entity
 *
 * @author Nikhil Vibhav
 */
@Service
@Log4j2
public class ItemService {

  private final ItemRepository itemRepository;
  private final CategoryRepository categoryRepository;
  private final ItemMapper mapper;

  @Autowired
  public ItemService(
      final ItemRepository itemRepository,
      final CategoryRepository categoryRepository,
      final ItemMapper mapper) {
    this.itemRepository = itemRepository;
    this.categoryRepository = categoryRepository;
    this.mapper = mapper;
  }

  /**
   * Creates an {@link Item} in the database
   *
   * @param itemToSave - the {@link Item} to save
   * @param categoryName - the {@link Category#name} to save the item against
   * @return the saved entity
   * @throws CategoryNotFoundException when the application cannot find the required category
   */
  public Item createItem(final Item itemToSave, final String categoryName)
      throws CategoryNotFoundException, InvalidAttributesException {

    log.debug("Saving Item - {}", itemToSave);

    final Category category = getCategory(categoryName);
    itemToSave.setCategory(category);

    if (isAttributeNotMatchForCategory(itemToSave, category)) {
      throw new InvalidAttributesException(
          "Invalid attributes associated with item for the " + categoryName + " category");
    }

    return itemRepository.save(itemToSave);
  }

  /**
   * Fetches a list of {@link Item} in a certain category
   *
   * @param category - the category name against which to fetch the items
   * @return a list of items
   */
  public List<Item> getItemsInCategory(final String category) {

    log.debug("Fetching items within category - {}", category);

    return itemRepository.findByCategory_Name(category);
  }

  /**
   * Updates the {@link Item} based on the new request received
   *
   * @param item - the new {@link Item} request received
   * @param newCategory - the new {@link Category#name}
   * @return the updated entity
   * @throws ItemNotFoundException - when the application cannot find the item to update
   * @throws CategoryNotFoundException - when the application cannot find the new category
   * @throws InvalidAttributesException - when the attributes do not match the new category
   */
  @Transactional
  public Item updateItem(final Item item, final String newCategory)
      throws ItemNotFoundException, CategoryNotFoundException, InvalidAttributesException {

    log.debug("Updating item with id - {}", item.getId());

    final Item itemToUpdate = getItem(item.getId());
    final Category category = getCategory(newCategory);
    item.setCategory(category);

    if (isAttributeNotMatchForCategory(item, category)) {
      throw new InvalidAttributesException(
          "Invalid attributes associated with item for the " + itemToUpdate + " category");
    }

    mapper.updateItemEntity(item, itemToUpdate);

    return itemRepository.save(itemToUpdate);
  }

  /**
   * Fetches the item by its ID
   *
   * @param id - the given id
   * @return the {@link Item} if present
   * @throws ItemNotFoundException when the application cannot find the item
   */
  private Item getItem(final Long id) throws ItemNotFoundException {

    log.debug("Getting item with id - {}", id);

    return itemRepository
        .findById(id)
        .orElseThrow(() -> new ItemNotFoundException("Unable to find item with the id - " + id));
  }

  /**
   * Fetches the category by the given name
   *
   * @param category - the given category name to find
   * @return {@link Category} if present
   * @throws CategoryNotFoundException when the application cannot find the category
   */
  private Category getCategory(String category) throws CategoryNotFoundException {

    log.debug("Getting category with name - {}", category);

    return categoryRepository
        .findById(category)
        .orElseThrow(
            () -> new CategoryNotFoundException("Unable to find the category - " + category));
  }

  /**
   * Checks whether the attributes in the {@link Item} match those defined in the {@link Category}
   *
   * @param itemToSave - the item to save
   * @param category - the category for which the item is being saved
   * @return true/false
   */
  private boolean isAttributeNotMatchForCategory(Item itemToSave, Category category) {
    final List<String> attributes =
        category.getAttributes().stream().map(Attribute::getName).collect(Collectors.toList());

    return itemToSave.getAttributeValues().stream()
        .noneMatch(attributeValue -> attributes.contains(attributeValue.getName()));
  }
}
