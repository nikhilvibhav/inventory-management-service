package io.labfwd.task.inventory.model.valueobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Models the {@link io.labfwd.task.inventory.model.entity.Attribute} value object
 *
 * @author Nikhil Vibhav
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeVo {

  @Schema(
      description = "Unique ID of the Attribute",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Schema(description = "Name of the Attribute", example = "Volume", required = true)
  @NotNull(message = "attribute name cannot be null")
  @NotEmpty(message = "attribute name cannot be empty")
  private String name;
}
