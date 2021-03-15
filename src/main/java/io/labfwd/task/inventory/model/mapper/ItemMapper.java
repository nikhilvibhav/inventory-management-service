package io.labfwd.task.inventory.model.mapper;

import io.labfwd.task.inventory.model.entity.AttributeValue;
import io.labfwd.task.inventory.model.entity.Item;
import io.labfwd.task.inventory.model.valueobject.AttributeValueVo;
import io.labfwd.task.inventory.model.valueobject.ItemVo;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapstruct interface for mapping object of {@link Item} and {@link ItemVo}
 *
 * @author Nikhil Vibhav
 */
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ItemMapper {

  /**
   * Maps {@link ItemVo} to {@link Item}
   *
   * @param source {@link ItemVo}
   * @return {@link Item}
   */
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "id", ignore = true)
  Item toItem(final ItemVo source);

  /**
   * Updates the {@link Item} entity with values from the source {@link Item} received in Update
   * request
   *
   * @param source {@link Item} source
   * @param entity {@link Item} entity
   */
  @Mapping(target = "category", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateItemEntity(final Item source, @MappingTarget final Item entity);

  /**
   * Maps {@link Item} to {@link ItemVo}
   *
   * @param source {@link ItemVo}
   * @return {@link Item}
   */
  @Mapping(target = "category", source = "category.name")
  ItemVo toItemVo(final Item source);

  /**
   * Maps {@link AttributeValueVo} to {@link AttributeValue}
   *
   * @param source {@link AttributeValueVo}
   * @return {@link AttributeValue}
   */
  @Mapping(target = "item", ignore = true)
  AttributeValue toAttributeValue(final AttributeValueVo source);

  /**
   * Maps {@link AttributeValue} to {@link AttributeValueVo}
   *
   * @param source {@link AttributeValue}
   * @return {@link AttributeValueVo}
   */
  AttributeValueVo toAttributeValueVo(final AttributeValue source);

  /**
   * Maps list of {@link Item} to list of {@link ItemVo}
   *
   * @param itemList List of {@link Item}
   * @return List of {@link ItemVo}
   */
  List<ItemVo> toItemVoList(final List<Item> itemList);

  /**
   * Custom mapping to implement after mapping of {@link ItemMapper#toItem(ItemVo)} is done
   *
   * @param target {@link Item}
   */
  @AfterMapping
  default void afterMapping(@MappingTarget final Item target) {
    if (target.getAttributeValues() != null && !target.getAttributeValues().isEmpty()) {
      target.getAttributeValues().forEach(attributeValue -> attributeValue.setItem(target));
    }
  }
}
