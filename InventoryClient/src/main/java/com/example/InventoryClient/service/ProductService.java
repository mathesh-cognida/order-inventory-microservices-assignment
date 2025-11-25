package com.example.InventoryClient.service;

import com.example.InventoryClient.DTO.ProductOnboardDTO;
import com.example.InventoryClient.Model.Product;

import java.util.Optional;

public interface ProductService {
    public Optional<Product> getProduct(long id);
    public Product saveProduct(Product product);
    public Product updateProduct(Product product);
    public Product saveProductAndBatchProduct(ProductOnboardDTO productOnboardDTO);

}
