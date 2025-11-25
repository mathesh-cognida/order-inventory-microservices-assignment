package com.example.InventoryClient.repository;

import com.example.InventoryClient.Model.BatchProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatchProductRepository extends JpaRepository<BatchProduct, Long> {
    List<BatchProduct> findByProductId(long productId);
    Optional<BatchProduct> findByBatchId(long batchId);
//    BatchProduct findByBatchId(long batchId);
}
