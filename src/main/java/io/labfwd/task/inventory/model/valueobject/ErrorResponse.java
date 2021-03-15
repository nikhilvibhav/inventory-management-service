package io.labfwd.task.inventory.model.valueobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Models an error response
 *
 * @author Nikhil Vibhav
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  @Schema(description = "Http Status of the Error Response", example = "500")
  private Integer status;

  @Schema(description = "Timestamp when the error occurred", example = "2021-03-12T18:15:30.435Z")
  private Date timestamp;

  @Schema(description = "Message of the Error Response", example = "An unknown error occurred")
  private String message;

  @Schema(description = "Array of error messages", example = "[\"An unknown error occurred\"]")
  private List<String> errors;
}
