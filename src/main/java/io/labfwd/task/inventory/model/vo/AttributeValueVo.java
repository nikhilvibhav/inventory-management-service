package io.labfwd.task.inventory.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nikhil Vibhav
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeValueVo {
    private Long id;
    private String name;
    private String value;
}
