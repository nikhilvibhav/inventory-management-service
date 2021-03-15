package io.labfwd.task.inventory.controller;

import io.labfwd.task.inventory.model.entity.Category;
import io.labfwd.task.inventory.model.mapper.CategoryMapper;
import io.labfwd.task.inventory.model.valueobject.CategoryVo;
import io.labfwd.task.inventory.model.valueobject.ErrorResponse;
import io.labfwd.task.inventory.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes the APIs for interacting with the {@link Category} entity
 *
 * @author Nikhil Vibhav
 */
@RestController
@RequestMapping(path = "/labfwd/v1/category", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "category", description = "Category API")
@Log4j2
public class CategoryController {

  private final CategoryService service;
  private final CategoryMapper mapper;

  @Autowired
  public CategoryController(final CategoryService service, final CategoryMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  /**
   * Creates a {@link Category} record in the db
   *
   * @param request - the {@link CategoryVo} value object
   * @return the saved {@link CategoryVo} value object
   */
  @Operation(summary = "Creates a Category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Created the Category",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CategoryVo.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request supplied",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CategoryVo> createCategory(@Valid @RequestBody final CategoryVo request) {
    log.info("Received request for creating a category: {}", request);

    final Category category = mapper.toCategory(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(mapper.toCategoryVo(service.createCategory(category)));
  }
}
