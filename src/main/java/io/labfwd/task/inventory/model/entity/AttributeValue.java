package io.labfwd.task.inventory.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Models the {@link Attribute}'s values
 *
 * @author Nikhil Vibhav
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "attribute_value")
public class AttributeValue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String value;

  private String uom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Item item;
}
