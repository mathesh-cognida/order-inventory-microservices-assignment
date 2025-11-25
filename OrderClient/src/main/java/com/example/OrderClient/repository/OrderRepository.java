package com.example.OrderClient.repository;

import com.example.OrderClient.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByOrderId(long orderId);
}
