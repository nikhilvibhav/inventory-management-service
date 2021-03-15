package io.labfwd.task.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.labfwd.task.inventory.model.entity.Attribute;
import io.labfwd.task.inventory.model.entity.Category;
import io.labfwd.task.inventory.repository.CategoryRepository;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link CategoryService}
 *
 * @author Nikhil Vibhav
 */
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @Mock private CategoryRepository categoryRepository;
  @InjectMocks private CategoryService categoryService;

  @Test
  public void givenValidCategory_WhenCreateCategory_ThenSucceed() throws Exception {
    final Category category = new Category();
    final Attribute attribute = new Attribute();

    attribute.setName("Volume");
    category.setName("Chemicals");
    category.setAttributes(Collections.singletonList(attribute));

    given(categoryRepository.save(category)).willReturn(category);

    final Category savedCategory = categoryService.createCategory(category);
    assertEquals(category, savedCategory);

    verify(categoryRepository, times(1)).save(category);
  }
}
