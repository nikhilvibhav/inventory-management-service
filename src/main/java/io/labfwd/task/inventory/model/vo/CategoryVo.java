package io.labfwd.task.inventory.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Nikhil Vibhav
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {
    private Long id;
    private String name;
    private List<AttributeVo> attributes;
    private List<ItemVo> items;
}
