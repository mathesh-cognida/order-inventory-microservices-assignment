package com.example.InventoryClient.service.impl;

import com.example.InventoryClient.DTO.ProductOnboardDTO;
import com.example.InventoryClient.Model.BatchProduct;
import com.example.InventoryClient.Model.Product;
import com.example.InventoryClient.repository.ProductRepository;
import com.example.InventoryClient.service.BatchProductService;
import com.example.InventoryClient.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private  final BatchProductService batchProductService;

    ProductServiceImpl(ProductRepository productRepository, BatchProductService batchProductService){
        this.productRepository = productRepository;
        this.batchProductService = batchProductService;
    }

    @Override
    public Optional<Product> getProduct(long id) {
        return this.productRepository.findByProductId(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Optional<Product> productToBeUpdated = this.productRepository.findById(product.getId());
        Product update = null;
        if(productToBeUpdated.isPresent()){
            update = productToBeUpdated.get();
            update.setProductDescription(product.getProductDescription());
            this.productRepository.save(update);
        }
        return update;
    }

    @Override
    public Product saveProductAndBatchProduct(ProductOnboardDTO productOnboardDTO) {

        Product persistProduct = null;
        BatchProduct persistBatchProduct = null;
        if(!this.getProduct(productOnboardDTO.productId()).isPresent()) {
            Product product = Product.builder()
                    .productId(productOnboardDTO.productId())
                    .productName(productOnboardDTO.productDescription())
                    .productName(productOnboardDTO.productName())
                    .build();
            persistProduct = this.saveProduct(product);
        }else{
            persistProduct = this.getProduct(productOnboardDTO.productId()).get();
        }
        if(!batchProductService.getBatchProduct(productOnboardDTO.batchId()).isPresent()){
            BatchProduct batchProduct = BatchProduct.builder()
                    .batchId(productOnboardDTO.batchId())
                    .batchType(productOnboardDTO.batchType())
                    .price(productOnboardDTO.price())
                    .quantity(productOnboardDTO.quantity())
                    .expiryTime(productOnboardDTO.expiryDate())
                    .product(persistProduct)
                    .build();

            persistProduct.addBatches(batchProduct);
            persistProduct = this.saveProduct(persistProduct);
        }else{
            BatchProduct batchProduct = batchProductService.getBatchProduct(productOnboardDTO.batchId()).get();
            batchProduct.setQuantity(productOnboardDTO.quantity());
            persistBatchProduct = batchProductService.saveBatchProduct(batchProduct);
            persistProduct = this.saveProduct(persistProduct);
        }

        return persistProduct;
    }
}
