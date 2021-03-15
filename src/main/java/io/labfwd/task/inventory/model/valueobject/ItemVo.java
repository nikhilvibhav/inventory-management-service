package io.labfwd.task.inventory.model.valueobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Models the {@link io.labfwd.task.inventory.model.entity.Item} value object
 *
 * @author Nikhil Vibhav
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemVo {

  @Schema(description = "Unique ID of the Item", example = "1", accessMode = AccessMode.READ_ONLY)
  private Long id;

  @Schema(description = "Name of the Item", example = "Hydrochloric Acid", required = true)
  @NotNull(message = "name cannot be null")
  @NotEmpty(message = "name cannot be empty")
  private String name;

  @Schema(description = "Category associated with the Item", example = "Chemicals", required = true)
  @NotNull(message = "category cannot be null")
  @NotEmpty(message = "category cannot be empty")
  private String category;

  @Schema(description = "Attribute Values associated with the Item", required = true)
  @NotNull(message = "attributeValues cannot be null")
  @NotEmpty(message = "attributeValues cannot be empty")
  private List<AttributeValueVo> attributeValues;
}
