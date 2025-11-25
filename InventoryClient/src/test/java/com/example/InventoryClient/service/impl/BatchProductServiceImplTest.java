package com.example.InventoryClient.service.impl;

import com.example.InventoryClient.Model.BatchProduct;
import com.example.InventoryClient.Model.Product;
import com.example.InventoryClient.repository.BatchProductRepository;
import com.example.InventoryClient.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BatchProductServiceImpl Tests")
class BatchProductServiceImplTest {

    @Mock
    private BatchProductRepository batchProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private BatchProductServiceImpl batchProductService;

    private BatchProduct batchProduct;
    private Product product;
    private static final long BATCH_ID = 1L;
    private static final long PRODUCT_ID = 100L;
    private static final long BATCH_PRODUCT_ID = 10L;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(PRODUCT_ID)
                .productId(PRODUCT_ID)
                .productName("Test Product")
                .productDescription("Test Description")
                .build();

        batchProduct = BatchProduct.builder()
                .id(BATCH_PRODUCT_ID)
                .batchId(BATCH_ID)
                .batchType("Type A")
                .expiryTime(Instant.now().plusSeconds(86400))
                .price(99.99)
                .quantity(100)
                .product(product)
                .build();
    }

    @Test
    @DisplayName("Should return BatchProduct when getBatchProduct is called with valid batchId")
    void testGetBatchProduct_Success() {
        // Given
        when(batchProductRepository.findByBatchId(BATCH_ID)).thenReturn(Optional.of(batchProduct));

        // When
        Optional<BatchProduct> result = batchProductService.getBatchProduct(BATCH_ID);

        // Then
        assertTrue(result.isPresent());
        assertEquals(BATCH_ID, result.get().getBatchId());
        assertEquals(BATCH_PRODUCT_ID, result.get().getId());
        assertEquals("Type A", result.get().getBatchType());
        assertEquals(100, result.get().getQuantity());
        verify(batchProductRepository, times(1)).findByBatchId(BATCH_ID);
    }

    @Test
    @DisplayName("Should return empty Optional when getBatchProduct is called with non-existent batchId")
    void testGetBatchProduct_NotFound() {
        // Given
        when(batchProductRepository.findByBatchId(BATCH_ID)).thenReturn(Optional.empty());

        // When
        Optional<BatchProduct> result = batchProductService.getBatchProduct(BATCH_ID);

        // Then
        assertFalse(result.isPresent());
        verify(batchProductRepository, times(1)).findByBatchId(BATCH_ID);
    }

    @Test
    @DisplayName("Should save and return BatchProduct when saveBatchProduct is called")
    void testSaveBatchProduct_Success() {
        // Given
        BatchProduct newBatchProduct = BatchProduct.builder()
                .batchId(2L)
                .batchType("Type B")
                .expiryTime(Instant.now().plusSeconds(172800))
                .price(149.99)
                .quantity(50)
                .product(product)
                .build();

        when(batchProductRepository.save(any(BatchProduct.class))).thenReturn(newBatchProduct);

        // When
        BatchProduct result = batchProductService.saveBatchProduct(newBatchProduct);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getBatchId());
        assertEquals("Type B", result.getBatchType());
        assertEquals(50, result.getQuantity());
        assertEquals(149.99, result.getPrice());
        verify(batchProductRepository, times(1)).save(newBatchProduct);
    }

    @Test
    @DisplayName("Should update BatchProduct when updateBatchProduct is called with existing id")
    void testUpdateBatchProduct_Success() {
        // Given
        BatchProduct updatedBatchProduct = BatchProduct.builder()
                .id(BATCH_PRODUCT_ID)
                .batchId(BATCH_ID)
                .quantity(200)
                .build();

        BatchProduct existingBatchProduct = BatchProduct.builder()
                .id(BATCH_PRODUCT_ID)
                .batchId(BATCH_ID)
                .batchType("Type A")
                .expiryTime(Instant.now().plusSeconds(86400))
                .price(99.99)
                .quantity(100)
                .product(product)
                .build();

        when(batchProductRepository.findById(BATCH_PRODUCT_ID)).thenReturn(Optional.of(existingBatchProduct));
        when(batchProductRepository.save(any(BatchProduct.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        BatchProduct result = batchProductService.updateBatchProduct(updatedBatchProduct);

        // Then
        assertNotNull(result);
        assertEquals(BATCH_PRODUCT_ID, result.getId());
        assertEquals(200, result.getQuantity());
        assertEquals("Type A", result.getBatchType()); // Other fields should remain unchanged
        verify(batchProductRepository, times(1)).findById(BATCH_PRODUCT_ID);
        verify(batchProductRepository, times(1)).save(existingBatchProduct);
    }

    @Test
    @DisplayName("Should return null when updateBatchProduct is called with non-existent id")
    void testUpdateBatchProduct_NotFound() {
        // Given
        BatchProduct updatedBatchProduct = BatchProduct.builder()
                .id(999L)
                .batchId(BATCH_ID)
                .quantity(200)
                .build();

        when(batchProductRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        BatchProduct result = batchProductService.updateBatchProduct(updatedBatchProduct);

        // Then
        assertNull(result);
        verify(batchProductRepository, times(1)).findById(999L);
        verify(batchProductRepository, never()).save(any(BatchProduct.class));
    }

    @Test
    @DisplayName("Should return list of BatchProducts when getBatchs is called with valid productId")
    void testGetBatchs_Success() {
        // Given
        BatchProduct batchProduct1 = BatchProduct.builder()
                .id(1L)
                .batchId(101L)
                .batchType("Type A")
                .quantity(100)
                .product(product)
                .build();

        BatchProduct batchProduct2 = BatchProduct.builder()
                .id(2L)
                .batchId(102L)
                .batchType("Type B")
                .quantity(200)
                .product(product)
                .build();

        List<BatchProduct> batchProducts = Arrays.asList(batchProduct1, batchProduct2);

        when(batchProductRepository.findByProductId(PRODUCT_ID)).thenReturn(batchProducts);

        // When
        List<BatchProduct> result = batchProductService.getBatchs(PRODUCT_ID);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(101L, result.get(0).getBatchId());
        assertEquals(102L, result.get(1).getBatchId());
        verify(batchProductRepository, times(1)).findByProductId(PRODUCT_ID);
    }

    @Test
    @DisplayName("Should return empty list when getBatchs is called with productId that has no batches")
    void testGetBatchs_EmptyList() {
        // Given
        when(batchProductRepository.findByProductId(PRODUCT_ID)).thenReturn(Arrays.asList());

        // When
        List<BatchProduct> result = batchProductService.getBatchs(PRODUCT_ID);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(batchProductRepository, times(1)).findByProductId(PRODUCT_ID);
    }

    @Test
    @DisplayName("Should verify repository interactions for saveBatchProduct")
    void testSaveBatchProduct_VerifyRepositoryInteraction() {
        // Given
        when(batchProductRepository.save(any(BatchProduct.class))).thenReturn(batchProduct);

        // When
        batchProductService.saveBatchProduct(batchProduct);

        // Then
        verify(batchProductRepository, times(1)).save(batchProduct);
        verifyNoMoreInteractions(batchProductRepository);
    }

    @Test
    @DisplayName("Should only update quantity field when updateBatchProduct is called")
    void testUpdateBatchProduct_OnlyQuantityUpdated() {
        // Given
        BatchProduct updatedBatchProduct = BatchProduct.builder()
                .id(BATCH_PRODUCT_ID)
                .batchId(BATCH_ID)
                .batchType("New Type")
                .quantity(300)
                .price(199.99)
                .build();

        BatchProduct existingBatchProduct = BatchProduct.builder()
                .id(BATCH_PRODUCT_ID)
                .batchId(BATCH_ID)
                .batchType("Original Type")
                .expiryTime(Instant.now().plusSeconds(86400))
                .price(99.99)
                .quantity(100)
                .product(product)
                .build();

        when(batchProductRepository.findById(BATCH_PRODUCT_ID)).thenReturn(Optional.of(existingBatchProduct));
        when(batchProductRepository.save(any(BatchProduct.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        BatchProduct result = batchProductService.updateBatchProduct(updatedBatchProduct);

        // Then
        assertNotNull(result);
        assertEquals(300, result.getQuantity()); // Quantity should be updated
        assertEquals("Original Type", result.getBatchType()); // Other fields should remain unchanged
        assertEquals(99.99, result.getPrice()); // Price should remain unchanged
        verify(batchProductRepository, times(1)).findById(BATCH_PRODUCT_ID);
        verify(batchProductRepository, times(1)).save(existingBatchProduct);
    }
}

