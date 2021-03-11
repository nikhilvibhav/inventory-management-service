package io.labfwd.task.inventory.controller;

import io.labfwd.task.inventory.model.vo.CategoryVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nikhil Vibhav
 */
@RestController
@RequestMapping(path = "/labfwd/v1/category", produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
public class CategoryController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryVo> createCategory(@RequestBody final CategoryVo request) {
        log.info("Received request for creating a category: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }
}
