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
public class ItemVo {
    private Long id;
    private String name;
    private List<AttributeValueVo> attributeValues;
}
