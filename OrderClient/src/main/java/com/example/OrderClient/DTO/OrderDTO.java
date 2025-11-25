package com.example.OrderClient.DTO;

import java.util.List;

public record OrderDTO (
    Long userId,
    String userName,
    String shippingAddress,
    long productId,
    String productName,
    long batchId,
    double amount,
    int quantity){

}
