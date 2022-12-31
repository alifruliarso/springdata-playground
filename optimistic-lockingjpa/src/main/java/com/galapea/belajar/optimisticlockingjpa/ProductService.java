package com.galapea.belajar.optimisticlockingjpa;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void incrementAmount(String id, int amount) {
    Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    product.setAmount(product.getAmount() + amount);
  }
}
