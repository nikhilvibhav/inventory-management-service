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
import lombok.NoArgsConstructor;

/**
 * Models the attributes in a {@link Category}
 *
 * <p>e.g. a category, chemicals, can have attributes like volume and uom (mL, L etc.)
 *
 * @author Nikhil Vibhav
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "attribute")
public class Attribute {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;
}
