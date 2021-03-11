package io.labfwd.task.inventory.controller;

import io.labfwd.task.inventory.model.vo.ItemVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Nikhil Vibhav
 */
@RestController
@RequestMapping(path = "/labfwd/v1/item", produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
public class ItemController {

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemVo> createItem(@RequestBody ItemVo request) {
        log.info("Received request to create an item: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateItem(@PathVariable final Long id) {
        log.info("Received request to update item with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ItemVo>> getItemsInACategory(@RequestParam final String category) {
        log.info("Received request to fetch items of category {}", category);
        return ResponseEntity.ok(Collections.singletonList(new ItemVo()));
    }

}
