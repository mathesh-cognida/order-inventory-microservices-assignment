package com.example.InventoryClient.DTO;

public record ProductDTO(
         long productId,
         String productName,
         long batchId,
         double amount,
         int quantity ) {
}
