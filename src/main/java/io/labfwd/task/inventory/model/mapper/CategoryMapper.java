package io.labfwd.task.inventory.model.mapper;

import io.labfwd.task.inventory.model.entity.Attribute;
import io.labfwd.task.inventory.model.entity.Category;
import io.labfwd.task.inventory.model.valueobject.AttributeVo;
import io.labfwd.task.inventory.model.valueobject.CategoryVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

/**
 * Mapstruct interface for mapping between {@link Category} and {@link CategoryVo}
 *
 * @author Nikhil Vibhav
 */
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CategoryMapper {

  /**
   * Maps {@link CategoryVo} to {@link Category}
   *
   * @param source {@link CategoryVo}
   * @return {@link Category}
   */
  @Mapping(target = "items", ignore = true)
  Category toCategory(final CategoryVo source);

  /**
   * Maps {@link Category} to {@link CategoryVo}
   *
   * @param source {@link Category}
   * @return {@link CategoryVo}
   */
  CategoryVo toCategoryVo(final Category source);

  /**
   * Maps {@link AttributeVo} to {@link Attribute}
   *
   * @param source {@link AttributeVo}
   * @return {@link Attribute}
   */
  @Mapping(target = "category", ignore = true)
  Attribute toAttribute(final AttributeVo source);

  /**
   * Maps {@link AttributeVo} to {@link Attribute}
   *
   * @param source {@link AttributeVo}
   * @return {@link Attribute}
   */
  AttributeVo toAttributeVo(final Attribute source);

  /**
   * Custom mapping to implement after mapping of {@link CategoryMapper#toCategory(CategoryVo)} is
   * done
   *
   * @param target {@link Category}
   */
  @AfterMapping
  default void afterMapping(@MappingTarget final Category target) {
    if (target.getAttributes() != null && !target.getAttributes().isEmpty()) {
      target.getAttributes().forEach(attribute -> attribute.setCategory(target));
    }
  }
}
