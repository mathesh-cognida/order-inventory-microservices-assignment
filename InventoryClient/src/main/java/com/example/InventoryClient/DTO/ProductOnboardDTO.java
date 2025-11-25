package com.example.InventoryClient.DTO;

import java.time.Instant;

public record ProductOnboardDTO(long productId,
                                long batchId,
                                String productName,
                                String productDescription,
                                Instant expiryDate,
                                String batchType,
                                int quantity,
                                double price) {
}
