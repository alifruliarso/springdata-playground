package com.galapea.belajar.optimisticlockingjpa;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {}
