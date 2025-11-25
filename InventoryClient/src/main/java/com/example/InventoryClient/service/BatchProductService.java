package com.example.InventoryClient.service;

import com.example.InventoryClient.Model.BatchProduct;

import java.util.List;
import java.util.Optional;

public interface BatchProductService {
    public Optional<BatchProduct> getBatchProduct(long id);
    public BatchProduct saveBatchProduct(BatchProduct batchProduct);
    public BatchProduct updateBatchProduct(BatchProduct batchProduct);
    public List<BatchProduct> getBatchs(long productId);
}
