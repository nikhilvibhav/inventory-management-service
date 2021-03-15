package io.labfwd.task.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.labfwd.task.inventory.exception.CategoryNotFoundException;
import io.labfwd.task.inventory.exception.InvalidAttributesException;
import io.labfwd.task.inventory.exception.ItemNotFoundException;
import io.labfwd.task.inventory.model.entity.Attribute;
import io.labfwd.task.inventory.model.entity.AttributeValue;
import io.labfwd.task.inventory.model.entity.Category;
import io.labfwd.task.inventory.model.entity.Item;
import io.labfwd.task.inventory.model.mapper.ItemMapper;
import io.labfwd.task.inventory.repository.CategoryRepository;
import io.labfwd.task.inventory.repository.ItemRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link ItemService}
 *
 * @author Nikhil Vibhav
 */
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

  @Mock private ItemRepository itemRepository;
  @Mock private CategoryRepository categoryRepository;
  @Mock private ItemMapper mapper;
  @InjectMocks private ItemService itemService;

  @Test
  public void givenValidItem_WhenCreateItem_ThenSucceed() throws Exception {
    final String categoryStr = "Chemicals";
    final Item itemToCreate = new Item();
    final AttributeValue attributeValue = new AttributeValue(1L, "Volume", "100", "mL", null);
    itemToCreate.setName("Hydrochloric acid");
    itemToCreate.setAttributeValues(Collections.singletonList(attributeValue));

    given(itemRepository.save(itemToCreate)).willReturn(itemToCreate);
    given(categoryRepository.findById(categoryStr))
        .willAnswer(
            invocation -> {
              final Category category = new Category();
              category.setName(categoryStr);
              category.setAttributes(Collections.singletonList(new Attribute(1L, "Volume", null)));
              return Optional.of(category);
            });

    final var createdItem = itemService.createItem(itemToCreate, categoryStr);

    assertEquals(itemToCreate, createdItem);

    verify(itemRepository, times(1)).save(itemToCreate);
    verify(categoryRepository, times(1)).findById(categoryStr);
  }

  @Test
  public void givenValidItem_WhenCreateItem_ThenFail_InvalidAttributes() throws Exception {
    final String categoryStr = "Chemicals";
    final Item itemToCreate = new Item();
    final AttributeValue attributeValue = new AttributeValue(1L, "Volume", "100", "mL", null);
    itemToCreate.setName("Hydrochloric acid");
    itemToCreate.setAttributeValues(Collections.singletonList(attributeValue));

    given(categoryRepository.findById(categoryStr))
        .willAnswer(
            invocation -> {
              final Category category = new Category();
              category.setName(categoryStr);
              category.setAttributes(Collections.singletonList(new Attribute(1L, "Units", null)));
              return Optional.of(category);
            });

    assertThrows(
        InvalidAttributesException.class, () -> itemService.createItem(itemToCreate, categoryStr));

    verify(categoryRepository, times(1)).findById(categoryStr);
  }

  @Test
  public void givenValidItem_WhenCreateItem_ThenFail_CategoryNotFound() throws Exception {
    final String categoryStr = "Chemicals";
    final Item itemToCreate = new Item();
    final AttributeValue attributeValue = new AttributeValue(1L, "Volume", "100", "mL", null);
    itemToCreate.setName("Hydrochloric acid");
    itemToCreate.setAttributeValues(Collections.singletonList(attributeValue));

    given(categoryRepository.findById(categoryStr)).willReturn(Optional.empty());

    assertThrows(
        CategoryNotFoundException.class, () -> itemService.createItem(itemToCreate, categoryStr));

    verify(categoryRepository, times(1)).findById(categoryStr);
  }

  @Test
  public void givenValidCategory_WhenGetItemsInCategory_ThenSucceed() throws Exception {
    final String categoryStr = "Chemicals";
    final Category category = new Category();
    category.setName(categoryStr);
    category.setAttributes(Collections.singletonList(new Attribute(1L, "Volume", category)));

    final Item item = new Item();
    item.setId(1L);
    item.setName("Hydrochloric acid");
    item.setCategory(category);

    final AttributeValue attributeValue = new AttributeValue(1L, "Volume", "100", "mL", item);
    item.setAttributeValues(Collections.singletonList(attributeValue));

    given(itemRepository.findByCategory_Name(categoryStr))
        .willReturn(Collections.singletonList(item));

    final List<Item> itemsInCategory = itemService.getItemsInCategory(categoryStr);
    assertNotNull(itemsInCategory);
    assertFalse(itemsInCategory.isEmpty());
    verify(itemRepository, times(1)).findByCategory_Name(categoryStr);
  }

  @Test
  public void givenInvalidCategory_WhenGetItemsInCategory_ThenSucceed() throws Exception {
    final String categoryStr = "Random";

    given(itemRepository.findByCategory_Name(categoryStr)).willReturn(Collections.emptyList());

    final List<Item> itemsInCategory = itemService.getItemsInCategory(categoryStr);
    assertNotNull(itemsInCategory);
    assertTrue(itemsInCategory.isEmpty());

    verify(itemRepository, times(1)).findByCategory_Name(categoryStr);
  }

  @Test
  public void givenValidItemAndCategoryName_WhenUpdateItem_ThenSucceed() throws Exception {
    final String categoryStr = "Chemicals";
    final Category category = new Category();
    category.setName(categoryStr);
    category.setAttributes(Collections.singletonList(new Attribute(1L, "Volume", category)));

    final Item itemToUpdate = new Item();
    itemToUpdate.setId(1L);
    itemToUpdate.setName("Hydrochloric acid");
    itemToUpdate.setCategory(category);

    final AttributeValue attributeValue =
        new AttributeValue(1L, "Volume", "100", "mL", itemToUpdate);
    itemToUpdate.setAttributeValues(Collections.singletonList(attributeValue));

    given(itemRepository.findById(itemToUpdate.getId())).willReturn(Optional.of(itemToUpdate));
    given(categoryRepository.findById(categoryStr)).willReturn(Optional.of(category));
    given(itemRepository.save(itemToUpdate)).willReturn(itemToUpdate);
    willDoNothing().given(mapper).updateItemEntity(any(Item.class), any(Item.class));

    final Item updatedItem = itemService.updateItem(itemToUpdate, categoryStr);

    assertEquals(itemToUpdate, updatedItem);

    verify(itemRepository, times(1)).findById(itemToUpdate.getId());
    verify(itemRepository, times(1)).save(itemToUpdate);
    verify(categoryRepository, times(1)).findById(categoryStr);
  }

  @Test
  public void givenInvalidItem_WhenUpdateItem_ThenFail_ItemNotFound() throws Exception {
    final String categoryStr = "Chemicals";
    final Category category = new Category();
    category.setName(categoryStr);
    category.setAttributes(Collections.singletonList(new Attribute(1L, "Volume", category)));

    final Item itemToUpdate = new Item();
    itemToUpdate.setId(1L);
    itemToUpdate.setName("Hydrochloric acid");
    itemToUpdate.setCategory(category);

    final AttributeValue attributeValue =
        new AttributeValue(1L, "Volume", "100", "mL", itemToUpdate);
    itemToUpdate.setAttributeValues(Collections.singletonList(attributeValue));

    given(itemRepository.findById(itemToUpdate.getId())).willReturn(Optional.empty());

    assertThrows(
        ItemNotFoundException.class, () -> itemService.updateItem(itemToUpdate, categoryStr));

    verify(itemRepository, times(1)).findById(itemToUpdate.getId());
  }

  @Test
  public void givenInvalidCategory_WhenUpdateItem_ThenFail_CategoryNotFound() throws Exception {
    final String categoryStr = "Chemicals";
    final Category category = new Category();
    category.setName(categoryStr);
    category.setAttributes(Collections.singletonList(new Attribute(1L, "Volume", category)));

    final Item itemToUpdate = new Item();
    itemToUpdate.setId(1L);
    itemToUpdate.setName("Hydrochloric acid");
    itemToUpdate.setCategory(category);

    final AttributeValue attributeValue =
        new AttributeValue(1L, "Volume", "100", "mL", itemToUpdate);
    itemToUpdate.setAttributeValues(Collections.singletonList(attributeValue));

    given(itemRepository.findById(itemToUpdate.getId())).willReturn(Optional.of(itemToUpdate));
    given(categoryRepository.findById(categoryStr)).willReturn(Optional.empty());

    assertThrows(
        CategoryNotFoundException.class, () -> itemService.updateItem(itemToUpdate, categoryStr));

    verify(categoryRepository, times(1)).findById(categoryStr);
  }

  @Test
  public void givenValidItem_WhenUpdateItem_ThenFail_InvalidAttributes() throws Exception {
    final String categoryStr = "Chemicals";
    final Item itemToCreate = new Item();
    final AttributeValue attributeValue = new AttributeValue(1L, "Volume", "100", "mL", null);
    itemToCreate.setName("Hydrochloric acid");
    itemToCreate.setAttributeValues(Collections.singletonList(attributeValue));

    given(categoryRepository.findById(categoryStr))
        .willAnswer(
            invocation -> {
              final Category category = new Category();
              category.setName(categoryStr);
              category.setAttributes(Collections.singletonList(new Attribute(1L, "Units", null)));
              return Optional.of(category);
            });

    assertThrows(
        InvalidAttributesException.class, () -> itemService.createItem(itemToCreate, categoryStr));

    verify(categoryRepository, times(1)).findById(categoryStr);
  }
}
