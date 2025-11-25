package com.example.InventoryClient.service.impl;

import com.example.InventoryClient.Model.BatchProduct;
import com.example.InventoryClient.Model.Product;
import com.example.InventoryClient.repository.BatchProductRepository;
import com.example.InventoryClient.repository.ProductRepository;
import com.example.InventoryClient.service.BatchProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatchProductServiceImpl implements BatchProductService {
    private final BatchProductRepository batchProductRepository;

    @Autowired
    private ProductRepository productRepository;

    BatchProductServiceImpl(BatchProductRepository batchProductRepository){
        this.batchProductRepository = batchProductRepository;
    }
    @Override
    public Optional<BatchProduct> getBatchProduct(long id) {
        return batchProductRepository.findByBatchId(id);
    }

    @Override
    public BatchProduct saveBatchProduct(BatchProduct batchProduct) {
        return batchProductRepository.save(batchProduct);
    }

    @Override
    public BatchProduct updateBatchProduct(BatchProduct batchProduct) {
        Optional<BatchProduct> productToBeUpdated = batchProductRepository.findById(batchProduct.getId());
        BatchProduct update = null;
        if(productToBeUpdated.isPresent()){
            update = productToBeUpdated.get();
            update.setQuantity(batchProduct.getQuantity());
            batchProductRepository.save(update);
        }
        return update;
    }

    @Override
    public List<BatchProduct> getBatchs(long productId) {
        return batchProductRepository.findByProductId(productId);
    }
}
