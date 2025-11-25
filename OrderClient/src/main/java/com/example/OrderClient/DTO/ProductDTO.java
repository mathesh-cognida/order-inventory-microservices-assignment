package com.example.OrderClient.DTO;

public record ProductDTO(
        long productId,
        String productName,
        long batchId,
        double amount,
        int quantity ) {
}
