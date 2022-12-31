package com.galapea.belajar.optimisticlockingjpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class InventoryServiceTest {
  @Autowired private InventoryService inventoryService;

  @Autowired private ProductRepository productRepository;

  @SpyBean private ProductService productService;

  private final List<Integer> itemAmounts = Arrays.asList(5, 8);

  @Test
  void shouldIncrementProductAmount_withoutConcurrency() {
    final Product product = productRepository.save(new Product());
    assertEquals(0, product.getVersion());

    for (final int amount : itemAmounts) {
      inventoryService.incrementProductAmount(product.getId(), amount);
    }

    final Product resultProduct =
        productRepository
            .findById(product.getId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    assertAll(
        () -> assertEquals(2, resultProduct.getVersion()),
        () -> assertEquals(13, resultProduct.getAmount()),
        () -> verify(productService, times(2)).incrementAmount(anyString(), anyInt()));
  }

  @Test
  void shouldIncrementItemAmount_withOptimisticLockingHandling() throws InterruptedException {

    final Product srcProduct = productRepository.save(new Product());
    assertEquals(0, srcProduct.getVersion());

    final ExecutorService executor = Executors.newFixedThreadPool(itemAmounts.size());

    for (final int amount : itemAmounts) {
      executor.execute(() -> inventoryService.incrementProductAmount(srcProduct.getId(), amount));
    }

    executor.shutdown();
    assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));

    final Product item =
        productRepository
            .findById(srcProduct.getId())
            .orElseThrow(() -> new IllegalArgumentException("No product found!"));

    assertAll(
        () -> assertEquals(2, item.getVersion()),
        () -> assertEquals(13, item.getAmount()),
        () -> verify(productService, times(3)).incrementAmount(anyString(), anyInt()));
  }
}
