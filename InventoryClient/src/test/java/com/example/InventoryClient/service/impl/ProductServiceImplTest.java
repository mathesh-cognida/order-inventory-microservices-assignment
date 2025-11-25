package com.example.InventoryClient.service.impl;

import com.example.InventoryClient.DTO.ProductOnboardDTO;
import com.example.InventoryClient.Model.BatchProduct;
import com.example.InventoryClient.Model.Product;
import com.example.InventoryClient.repository.ProductRepository;
import com.example.InventoryClient.service.BatchProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductServiceImpl Tests")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BatchProductService batchProductService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private BatchProduct batchProduct;
    private ProductOnboardDTO productOnboardDTO;
    private static final long PRODUCT_ID = 100L;
    private static final long BATCH_ID = 1L;
    private static final long ENTITY_ID = 10L;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(ENTITY_ID)
                .productId(PRODUCT_ID)
                .productName("Test Product")
                .productDescription("Test Description")
                .build();

        batchProduct = BatchProduct.builder()
                .id(ENTITY_ID)
                .batchId(BATCH_ID)
                .batchType("Type A")
                .expiryTime(Instant.now().plusSeconds(86400))
                .price(99.99)
                .quantity(100)
                .product(product)
                .build();

        productOnboardDTO = new ProductOnboardDTO(
                PRODUCT_ID,
                BATCH_ID,
                "New Product",
                "New Description",
                Instant.now().plusSeconds(86400),
                "Type B",
                200,
                149.99
        );
    }

    @Test
    @DisplayName("Should return Product when getProduct is called with valid productId")
    void testGetProduct_Success() {
        // Given
        when(productRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.of(product));

        // When
        Optional<Product> result = productService.getProduct(PRODUCT_ID);

        // Then
        assertTrue(result.isPresent());
        assertEquals(PRODUCT_ID, result.get().getProductId());
        assertEquals("Test Product", result.get().getProductName());
        assertEquals("Test Description", result.get().getProductDescription());
        verify(productRepository, times(1)).findByProductId(PRODUCT_ID);
    }

    @Test
    @DisplayName("Should return empty Optional when getProduct is called with non-existent productId")
    void testGetProduct_NotFound() {
        // Given
        when(productRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productService.getProduct(PRODUCT_ID);

        // Then
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findByProductId(PRODUCT_ID);
    }

    @Test
    @DisplayName("Should save and return Product when saveProduct is called")
    void testSaveProduct_Success() {
        // Given
        Product newProduct = Product.builder()
                .productId(200L)
                .productName("New Product")
                .productDescription("New Description")
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        // When
        Product result = productService.saveProduct(newProduct);

        // Then
        assertNotNull(result);
        assertEquals(200L, result.getProductId());
        assertEquals("New Product", result.getProductName());
        assertEquals("New Description", result.getProductDescription());
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    @DisplayName("Should update Product description when updateProduct is called with existing id")
    void testUpdateProduct_Success() {
        // Given
        Product updatedProduct = Product.builder()
                .id(ENTITY_ID)
                .productId(PRODUCT_ID)
                .productDescription("Updated Description")
                .build();

        Product existingProduct = Product.builder()
                .id(ENTITY_ID)
                .productId(PRODUCT_ID)
                .productName("Test Product")
                .productDescription("Original Description")
                .build();

        when(productRepository.findById(ENTITY_ID)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Product result = productService.updateProduct(updatedProduct);

        // Then
        assertNotNull(result);
        assertEquals(ENTITY_ID, result.getId());
        assertEquals("Updated Description", result.getProductDescription());
        assertEquals("Test Product", result.getProductName()); // Should remain unchanged
        verify(productRepository, times(1)).findById(ENTITY_ID);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    @DisplayName("Should return null when updateProduct is called with non-existent id")
    void testUpdateProduct_NotFound() {
        // Given
        Product updatedProduct = Product.builder()
                .id(999L)
                .productId(PRODUCT_ID)
                .productDescription("Updated Description")
                .build();

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Product result = productService.updateProduct(updatedProduct);

        // Then
        assertNull(result);
        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should save new Product and new BatchProduct when saveProductAndBatchProduct is called with new product and new batch")
    void testSaveProductAndBatchProduct_NewProduct_NewBatch() {
        // Given
        Product savedProduct = Product.builder()
                .id(ENTITY_ID)
                .productId(PRODUCT_ID)
                .productName("New Product")
                .productDescription("New Description")
                .build();

        when(productRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.empty());
        when(batchProductService.getBatchProduct(BATCH_ID)).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        Product result = productService.saveProductAndBatchProduct(productOnboardDTO);

        // Then
        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.getProductId());
        verify(productRepository, times(2)).findByProductId(PRODUCT_ID); // Once in getProduct, once in saveProductAndBatchProduct
        verify(batchProductService, times(1)).getBatchProduct(BATCH_ID);
        verify(productRepository, times(2)).save(any(Product.class)); // Once for new product, once after adding batch
    }

    @Test
    @DisplayName("Should use existing Product and create new BatchProduct when saveProductAndBatchProduct is called with existing product and new batch")
    void testSaveProductAndBatchProduct_ExistingProduct_NewBatch() {
        // Given
        Product existingProduct = Product.builder()
                .id(ENTITY_ID)
                .productId(PRODUCT_ID)
                .productName("Existing Product")
                .productDescription("Existing Description")
                .build();

        when(productRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.of(existingProduct));
        when(batchProductService.getBatchProduct(BATCH_ID)).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Product result = productService.saveProductAndBatchProduct(productOnboardDTO);

        // Then
        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.getProductId());
        assertEquals("Existing Product", result.getProductName());
        verify(productRepository, times(2)).findByProductId(PRODUCT_ID);
        verify(batchProductService, times(1)).getBatchProduct(BATCH_ID);
        verify(productRepository, times(1)).save(any(Product.class)); // Only once after adding batch
    }

    @Test
    @DisplayName("Should create new Product and update existing BatchProduct when saveProductAndBatchProduct is called with new product and existing batch")
    void testSaveProductAndBatchProduct_NewProduct_ExistingBatch() {
        // Given
        Product savedProduct = Product.builder()
                .id(ENTITY_ID)
                .productId(PRODUCT_ID)
                .productName("New Product")
                .productDescription("New Description")
                .build();

        BatchProduct existingBatchProduct = BatchProduct.builder()
                .id(ENTITY_ID)
                .batchId(BATCH_ID)
                .batchType("Type A")
                .quantity(100)
                .product(product)
                .build();

        when(productRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.empty());
        when(batchProductService.getBatchProduct(BATCH_ID)).thenReturn(Optional.of(existingBatchProduct));
        when(batchProductService.saveBatchProduct(any(BatchProduct.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        Product result = productService.saveProductAndBatchProduct(productOnboardDTO);

        // Then
        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.getProductId());
        verify(productRepository, times(2)).findByProductId(PRODUCT_ID);
        verify(batchProductService, times(1)).getBatchProduct(BATCH_ID);
        verify(batchProductService, times(1)).saveBatchProduct(any(BatchProduct.class));
        assertEquals(200, existingBatchProduct.getQuantity()); // Quantity should be updated
        verify(productRepository, times(2)).save(any(Product.class)); // Once for new product, once after updating batch
    }

    @Test
    @DisplayName("Should use existing Product and update existing BatchProduct when saveProductAndBatchProduct is called with existing product and existing batch")
    void testSaveProductAndBatchProduct_ExistingProduct_ExistingBatch() {
        // Given
        Product existingProduct = Product.builder()
                .id(ENTITY_ID)
                .productId(PRODUCT_ID)
                .productName("Existing Product")
                .productDescription("Existing Description")
                .build();

        BatchProduct existingBatchProduct = BatchProduct.builder()
                .id(ENTITY_ID)
                .batchId(BATCH_ID)
                .batchType("Type A")
                .quantity(100)
                .product(existingProduct)
                .build();

        when(productRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.of(existingProduct));
        when(batchProductService.getBatchProduct(BATCH_ID)).thenReturn(Optional.of(existingBatchProduct));
        when(batchProductService.saveBatchProduct(any(BatchProduct.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Product result = productService.saveProductAndBatchProduct(productOnboardDTO);

        // Then
        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.getProductId());
        assertEquals("Existing Product", result.getProductName());
        verify(productRepository, times(2)).findByProductId(PRODUCT_ID);
        verify(batchProductService, times(1)).getBatchProduct(BATCH_ID);
        verify(batchProductService, times(1)).saveBatchProduct(any(BatchProduct.class));
        assertEquals(200, existingBatchProduct.getQuantity()); // Quantity should be updated
        verify(productRepository, times(1)).save(any(Product.class)); // Only once after updating batch
    }

    @Test
    @DisplayName("Should verify repository interactions for saveProduct")
    void testSaveProduct_VerifyRepositoryInteraction() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        productService.saveProduct(product);

        // Then
        verify(productRepository, times(1)).save(product);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("Should only update productDescription field when updateProduct is called")
    void testUpdateProduct_OnlyDescriptionUpdated() {
        // Given
        Product updatedProduct = Product.builder()
                .id(ENTITY_ID)
                .productId(PRODUCT_ID)
                .productName("New Name")
                .productDescription("Updated Description")
                .build();

        Product existingProduct = Product.builder()
                .id(ENTITY_ID)
                .productId(PRODUCT_ID)
                .productName("Original Name")
                .productDescription("Original Description")
                .build();

        when(productRepository.findById(ENTITY_ID)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Product result = productService.updateProduct(updatedProduct);

        // Then
        assertNotNull(result);
        assertEquals("Updated Description", result.getProductDescription()); // Description should be updated
        assertEquals("Original Name", result.getProductName()); // Name should remain unchanged
        verify(productRepository, times(1)).findById(ENTITY_ID);
        verify(productRepository, times(1)).save(existingProduct);
    }
}

