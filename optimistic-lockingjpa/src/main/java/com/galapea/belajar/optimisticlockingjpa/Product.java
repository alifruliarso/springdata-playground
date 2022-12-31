package com.galapea.belajar.optimisticlockingjpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Product extends BaseEntity {

  @Id private String id = UUID.randomUUID().toString();
  private int amount;
}
