package io.labfwd.task.inventory.model.valueobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Models the {@link io.labfwd.task.inventory.model.entity.AttributeValue} value object
 *
 * @author Nikhil Vibhav
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeValueVo {

  @Schema(
      description = "Unique ID of the AttributeValue",
      example = "1",
      accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Schema(description = "Name of the AttributeValue", example = "Volume", required = true)
  @NotNull(message = "name cannot be null")
  @NotEmpty(message = "name cannot be empty")
  private String name;

  @Schema(description = "Value of the AttributeValue", example = "100", required = true)
  @NotNull(message = "value cannot be null")
  @NotEmpty(message = "value cannot be empty")
  private String value;

  @Schema(description = "Unit of measurement for the Attribute value", example = "mL")
  private String uom;
}
