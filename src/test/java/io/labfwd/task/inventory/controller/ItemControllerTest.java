package io.labfwd.task.inventory.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.labfwd.task.inventory.exception.CategoryNotFoundException;
import io.labfwd.task.inventory.exception.InvalidAttributesException;
import io.labfwd.task.inventory.exception.ItemNotFoundException;
import io.labfwd.task.inventory.model.entity.Attribute;
import io.labfwd.task.inventory.model.entity.AttributeValue;
import io.labfwd.task.inventory.model.entity.Category;
import io.labfwd.task.inventory.model.entity.Item;
import io.labfwd.task.inventory.model.mapper.ItemMapperImpl;
import io.labfwd.task.inventory.model.valueobject.AttributeValueVo;
import io.labfwd.task.inventory.model.valueobject.ItemVo;
import io.labfwd.task.inventory.service.ItemService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Web MVC test for {@link ItemController}
 *
 * @author Nikhil Vibhav
 */
@WebMvcTest(ItemController.class)
@Import({ItemMapperImpl.class})
public class ItemControllerTest {

  private static final String URI = "/labfwd/v1/item";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private ItemService itemService;

  @Test
  public void givenCreateRequest_WhenCreateItem_ThenReturn201_Created() throws Exception {

    // Given
    final AttributeValueVo attributeValue = new AttributeValueVo();
    attributeValue.setName("Volume");
    attributeValue.setValue("1500");
    attributeValue.setUom("mL");

    final ItemVo request = new ItemVo();
    request.setName("Hydrochloric acid");
    request.setCategory("Chemicals");
    request.setAttributeValues(Collections.singletonList(attributeValue));

    given(itemService.createItem(any(Item.class), eq(request.getCategory())))
        .willAnswer(
            invocation -> {
              final Item item = invocation.getArgument(0, Item.class);
              item.setId(1L);
              return item;
            });

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));

    verify(itemService, times(1)).createItem(any(), any());
  }

  @Test
  public void givenInvalidCreateRequest_WhenCreateItem_ThenReturn400_BadRequest() throws Exception {

    // Given
    final ItemVo request = new ItemVo();

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))));
  }

  @Test
  public void givenUpdateRequest_WhenUpdateItem_ThenReturn200_Success() throws Exception {
    // Given
    final AttributeValueVo attributeValue = new AttributeValueVo();
    attributeValue.setName("Volume");
    attributeValue.setValue("1500");
    attributeValue.setUom("mL");

    final ItemVo request = new ItemVo();
    request.setName("Hydrochloric acid");
    request.setCategory("Chemicals");
    request.setAttributeValues(Collections.singletonList(attributeValue));

    given(itemService.updateItem(any(Item.class), eq(request.getCategory())))
        .willAnswer(
            invocation -> {
              final Item item = invocation.getArgument(0, Item.class);
              item.setId(1L);
              return item;
            });

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(URI + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.id").value(1));

    verify(itemService, times(1)).updateItem(any(), any());
  }

  @Test
  public void givenInvalidUpdateRequest_WhenUpdateItem_ThenReturn400_BadRequest() throws Exception {

    // Given
    final ItemVo request = new ItemVo();

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(URI + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))));
  }

  @Test
  public void givenInvalidIdInUpdateRequest_WhenUpdateItem_ThenReturn400_BadRequest()
      throws Exception {

    // Given
    final AttributeValueVo attributeValue = new AttributeValueVo();
    attributeValue.setName("Volume");
    attributeValue.setValue("1500");
    attributeValue.setUom("mL");

    final ItemVo request = new ItemVo();
    request.setName("Hydrochloric acid");
    request.setCategory("Chemicals");
    request.setAttributeValues(Collections.singletonList(attributeValue));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(URI + "/{id}", "-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))))
        .andExpect(
            jsonPath("$.errors")
                .value("ItemController#updateItem.id: must be greater than or equal to 1"));
  }

  @Test
  public void givenUpdateRequest_WhenUpdateItem_ThenReturn404_CategoryNotFound() throws Exception {
    // Given
    final AttributeValueVo attributeValue = new AttributeValueVo();
    attributeValue.setName("Volume");
    attributeValue.setValue("1500");
    attributeValue.setUom("mL");

    final ItemVo request = new ItemVo();
    request.setName("Hydrochloric acid");
    request.setCategory("Chemicals");
    request.setAttributeValues(Collections.singletonList(attributeValue));

    given(itemService.updateItem(any(Item.class), eq(request.getCategory())))
        .willThrow(
            new CategoryNotFoundException("Unable to find category - " + request.getCategory()));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(URI + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors[0]").value("Unable to find category - Chemicals"));

    verify(itemService, times(1)).updateItem(any(), any());
  }

  @Test
  public void givenUpdateRequest_WhenUpdateItem_ThenReturn404_ItemNotFound() throws Exception {
    // Given
    final AttributeValueVo attributeValue = new AttributeValueVo();
    attributeValue.setName("Volume");
    attributeValue.setValue("1500");
    attributeValue.setUom("mL");

    final ItemVo request = new ItemVo();
    request.setName("Hydrochloric acid");
    request.setCategory("Chemicals");
    request.setAttributeValues(Collections.singletonList(attributeValue));

    given(itemService.updateItem(any(Item.class), eq(request.getCategory())))
        .willThrow(new ItemNotFoundException("Unable to find item with the id - 1"));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(URI + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors[0]").value("Unable to find item with the id - 1"));

    verify(itemService, times(1)).updateItem(any(), any());
  }

  @Test
  public void givenUpdateRequest_WhenUpdateItem_ThenReturn500_InvalidAttributes() throws Exception {
    // Given
    final AttributeValueVo attributeValue = new AttributeValueVo();
    attributeValue.setName("Volume");
    attributeValue.setValue("1500");
    attributeValue.setUom("mL");

    final ItemVo request = new ItemVo();
    request.setName("Hydrochloric acid");
    request.setCategory("Chemicals");
    request.setAttributeValues(Collections.singletonList(attributeValue));

    given(itemService.updateItem(any(Item.class), eq(request.getCategory())))
        .willThrow(
            new InvalidAttributesException(
                "Invalid attributes in the item for the associated category"));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(URI + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(
            jsonPath("$.errors[0]")
                .value("Invalid attributes in the item for the associated category"));

    verify(itemService, times(1)).updateItem(any(), any());
  }

  @Test
  public void givenValidCategoryName_WhenGetAllItemsInCategory_ThenReturn200_Success()
      throws Exception {
    final String categoryName = "Chemicals";

    final AttributeValue attributeValue = new AttributeValue();
    attributeValue.setId(1L);
    attributeValue.setName("Volume");
    attributeValue.setValue("1500");
    attributeValue.setUom("mL");

    final Category category = new Category();
    category.setName(categoryName);
    category.setAttributes(Collections.singletonList(new Attribute(1L, "Volume", null)));

    final Item item = new Item();
    item.setId(1L);
    item.setName("Hydrochloric acid");
    item.setCategory(category);
    item.setAttributeValues(Collections.singletonList(attributeValue));

    given(itemService.getItemsInCategory(categoryName)).willReturn(List.of(item));

    mockMvc
        .perform(MockMvcRequestBuilders.get(URI + "/{category}", categoryName))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
  }

  @Test
  public void givenInvalidCategoryName_WhenGetAllItemsInCategory_ThenReturn405_MethodNotAllowed()
      throws Exception {
    final String categoryName = "";

    mockMvc
        .perform(MockMvcRequestBuilders.get(URI + "/{category}", categoryName))
        .andDo(print())
        .andExpect(status().is4xxClientError());
  }
}
