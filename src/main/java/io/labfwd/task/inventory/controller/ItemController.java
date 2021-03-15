package io.labfwd.task.inventory.controller;

import io.labfwd.task.inventory.exception.CategoryNotFoundException;
import io.labfwd.task.inventory.exception.InvalidAttributesException;
import io.labfwd.task.inventory.exception.ItemNotFoundException;
import io.labfwd.task.inventory.model.entity.Item;
import io.labfwd.task.inventory.model.mapper.ItemMapper;
import io.labfwd.task.inventory.model.valueobject.ErrorResponse;
import io.labfwd.task.inventory.model.valueobject.ItemVo;
import io.labfwd.task.inventory.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes APIs to interact with the {@link Item} entity
 *
 * @author Nikhil Vibhav
 */
@RestController
@RequestMapping(path = "/labfwd/v1/item", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "item", description = "Item API")
@Log4j2
@Validated
public class ItemController {

  private final ItemService service;
  private final ItemMapper mapper;

  @Autowired
  public ItemController(final ItemService service, final ItemMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  /**
   * Creates an {@link Item}
   *
   * @param request - the {@link ItemVo} value object
   * @return the saved {@link ItemVo} value object
   * @throws CategoryNotFoundException when the {@link
   *     io.labfwd.task.inventory.model.entity.Category} in the request doesn't exist
   * @throws InvalidAttributesException - when the attributes do not match the new category
   */
  @Operation(summary = "Creates an Item")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Created the item",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ItemVo.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request supplied",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Category not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ItemVo> createItem(@Valid @RequestBody ItemVo request)
      throws CategoryNotFoundException, InvalidAttributesException {

    log.info("Received request to create an item - {}", request);

    final Item itemToSave = mapper.toItem(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(mapper.toItemVo(service.createItem(itemToSave, request.getCategory())));
  }

  /**
   * Updates an item with the given id
   *
   * @param id - the ID of the {@link Item} to update
   * @param request - the {@link ItemVo} update request
   * @return the updated {@link ItemVo} object
   * @throws ItemNotFoundException - when the application cannot find the {@link Item} by the given
   *     ID
   * @throws CategoryNotFoundException - when the application cannot find the {@link
   *     io.labfwd.task.inventory.model.entity.Category} given in the request body
   * @throws InvalidAttributesException - when the attributes do not match the new category
   */
  @Operation(summary = "Updates an Item")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Updated the item",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ItemVo.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request supplied",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Item not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ItemVo> updateItem(
      @PathVariable @Min(1) final Long id, @Valid @RequestBody final ItemVo request)
      throws ItemNotFoundException, CategoryNotFoundException, InvalidAttributesException {

    log.info("Received request to update item with id - {} with values - {}", id, request);

    final Item itemToUpdate = mapper.toItem(request);
    itemToUpdate.setId(id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.toItemVo(service.updateItem(itemToUpdate, request.getCategory())));
  }

  /**
   * Gets a list of {@link Item} in the given category
   *
   * @param category - the given {@link io.labfwd.task.inventory.model.entity.Category#name} to find
   *     items against
   * @return the list of items in that category
   */
  @Operation(summary = "Gets all the Items in a category")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fetched the items",
            content =
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ItemVo.class)))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid category supplied",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
      })
  @GetMapping(path = "/{category}")
  public ResponseEntity<List<ItemVo>> getItemsInACategory(
      @PathVariable @NotNull final String category) {

    log.info("Received request to fetch items of category - {}", category);

    return ResponseEntity.ok(mapper.toItemVoList(service.getItemsInCategory(category)));
  }
}
