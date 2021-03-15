package io.labfwd.task.inventory;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.labfwd.task.inventory.model.valueobject.AttributeValueVo;
import io.labfwd.task.inventory.model.valueobject.AttributeVo;
import io.labfwd.task.inventory.model.valueobject.CategoryVo;
import io.labfwd.task.inventory.model.valueobject.ItemVo;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Integration tests for the Inventory management service
 *
 * @author Nikhil Vibhav
 */
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = InventoryManagementServiceApplication.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = InventoryManagementServiceApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryManagementServiceApplicationTests {

  private static final String CATEGORY_URI = "/labfwd/v1/category";
  private static final String ITEM_URI = "/labfwd/v1/item";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper mapper;

  @Test
  @Order(1)
  public void givenRequest_WhenCreateCategory_ThenReturn201_Created() throws Exception {

    // Given
    final CategoryVo categoryVo =
        new CategoryVo("Chemicals", List.of(new AttributeVo(null, "Volume")));
    final String request = mapper.writeValueAsString(categoryVo);

    // Then
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(CATEGORY_URI)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @Test
  @Order(2)
  public void givenRequest_WhenCreateItem_ThenReturn201_Created() throws Exception {

    // Given
    final ItemVo itemVo =
        new ItemVo(
            null,
            "Hydrochloric Acid",
            "Chemicals",
            List.of(new AttributeValueVo(null, "Volume", "1500", "mL")));
    final String request = mapper.writeValueAsString(itemVo);

    // Then
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(ITEM_URI)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @Test
  @Order(3)
  public void givenRequest_WhenUpdateItem_ThenReturn200_Successful() throws Exception {

    // Given
    final ItemVo itemVo =
        new ItemVo(
            null,
            "Hydrochloric Acid",
            "Chemicals",
            List.of(new AttributeValueVo(null, "Volume", "2000", "mL")));
    final String request = mapper.writeValueAsString(itemVo);

    // Then
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(ITEM_URI + "/{id}", "1")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.attributeValues[0].id").value("2"))
        .andExpect(
            jsonPath("$.attributeValues[0].value")
                .value(itemVo.getAttributeValues().get(0).getValue()));
  }

  @Test
  @Order(4)
  public void givenRequest_WhenGetItemsInCategory_ThenReturn200_Successful() throws Exception {

    // Given
    final String category = "Chemicals";

    // Then
    mockMvc
        .perform(MockMvcRequestBuilders.get(ITEM_URI + "/{category}", category))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
  }
}
