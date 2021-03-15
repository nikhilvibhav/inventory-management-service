package io.labfwd.task.inventory.model.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Models the Category
 *
 * <p>e.g. Chemicals, beakers, flasks etc.
 *
 * @author Nikhil Vibhav
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "category")
public class Category {

  @Id
  @Column(nullable = false, unique = true, updatable = false)
  private String name;

  @OneToMany(
      mappedBy = "category",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @Fetch(FetchMode.SUBSELECT)
  private List<Attribute> attributes;

  @OneToMany(
      mappedBy = "category",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @Fetch(FetchMode.SUBSELECT)
  private List<Item> items;
}
