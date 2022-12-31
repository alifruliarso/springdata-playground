package com.galapea.belajar.optimisticlockingjpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class InventoryService {

  private final ProductService productService;

  @Transactional(readOnly = true)
  public void incrementProductAmount(String productId, int amount) {
    try {
      productService.incrementAmount(productId, amount);
    } catch (ObjectOptimisticLockingFailureException e) {
      log.warn(
          "Somebody has already updated the amount for product:{} in concurrent transaction. Will try again...",
          productId);
      productService.incrementAmount(productId, amount);
    }
  }
}
