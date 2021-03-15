package io.labfwd.task.inventory.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.labfwd.task.inventory.model.entity.Attribute;
import io.labfwd.task.inventory.model.entity.Category;
import io.labfwd.task.inventory.model.mapper.CategoryMapperImpl;
import io.labfwd.task.inventory.model.valueobject.AttributeVo;
import io.labfwd.task.inventory.model.valueobject.CategoryVo;
import io.labfwd.task.inventory.service.CategoryService;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Web MVC tests for {@link CategoryController}
 *
 * @author Nikhil Vibhav
 */
@WebMvcTest(CategoryController.class)
@Import(CategoryMapperImpl.class)
public class CategoryControllerTest {

  private static final String URI = "/labfwd/v1/category";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private CategoryService categoryService;

  @Test
  public void givenRequest_WhenCreateCategory_ThenReturn201_Created() throws Exception {

    // Given
    final CategoryVo request =
        new CategoryVo("Chemicals", List.of(new AttributeVo(null, "Volume")));

    given(categoryService.createCategory(any(Category.class)))
        .willAnswer(
            invocation -> {
              final Category category = invocation.getArgument(0, Category.class);
              //
              IntStream.range(0, category.getAttributes().size())
                  .forEach(
                      i -> {
                        final Attribute attribute1 = category.getAttributes().get(i);
                        attribute1.setId((long) i + 1);
                        category.getAttributes().set(i, attribute1);
                      });
              return category;
            });

    // Then
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(URI)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Chemicals"))
        .andExpect(jsonPath("$.attributes[0].id").value("1"));

    verify(categoryService, times(1)).createCategory(any(Category.class));
  }

  @Test
  public void givenInvalidRequest_WhenCreateCategory_ThenReturn400_BadRequest() throws Exception {

    // Given
    final CategoryVo request = new CategoryVo();

    // Then
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(URI)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors", hasSize(greaterThanOrEqualTo(1))));
  }
}
