package com.galapea.belajar.optimisticlockingjpa;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity {

  @Version private Long version;
}
