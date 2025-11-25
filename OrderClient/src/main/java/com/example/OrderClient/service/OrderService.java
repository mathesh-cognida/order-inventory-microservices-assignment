package com.example.OrderClient.service;

import com.example.OrderClient.DTO.OrderDTO;
import com.example.OrderClient.model.Orders;

import java.util.Optional;

public interface OrderService {
    public Optional<Orders> findByOrderId(Long orderId);
    public Orders saveOrder(OrderDTO order);
}
