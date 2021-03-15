package io.labfwd.task.inventory.model.valueobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Models the {@link io.labfwd.task.inventory.model.entity.Category} value object
 *
 * @author Nikhil Vibhav
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryVo {

  @Schema(description = "Name of the Category", example = "Chemicals", required = true)
  @NotNull(message = "category name cannot be null")
  @NotEmpty(message = "category name cannot be empty")
  private String name;

  @Schema(description = "Attributes associated with a Category", required = true)
  @NotNull(message = "category attributes cannot be null")
  @NotEmpty(message = "category attributes cannot be empty")
  @Valid
  private List<AttributeVo> attributes;
}
