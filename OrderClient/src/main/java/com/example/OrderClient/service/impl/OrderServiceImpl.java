package com.example.OrderClient.service.impl;

import com.example.OrderClient.DTO.OrderDTO;
import com.example.OrderClient.DTO.ProductDTO;
import com.example.OrderClient.model.ORDER_STATUS;
import com.example.OrderClient.model.Orders;
import com.example.OrderClient.model.OrderItem;
import com.example.OrderClient.model.PAYMENT_STATUS;
import com.example.OrderClient.repository.OrderItemRepository;
import com.example.OrderClient.repository.OrderRepository;
import com.example.OrderClient.repository.PaymentRepository;
import com.example.OrderClient.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final RestClient restClient;
    private final OrderRepository orderRepository;


    OrderServiceImpl(OrderRepository orderRepository, RestClient.Builder builder){
        this.orderRepository = orderRepository;
        this.restClient = builder.baseUrl("http://localhost:9091/v1").build();
    }

    @Override
    public Optional<Orders> findByOrderId(Long orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    public Orders saveOrder(OrderDTO orderDTO) {
        Orders orderPersist = null;
        ProductDTO productDTO = new ProductDTO(orderDTO.productId(), orderDTO.productName(), orderDTO.batchId(), orderDTO.amount(), orderDTO.quantity());
        ResponseEntity<String> response = restClient.post()
                                            .uri("/inventory/update")
                                            .body(productDTO)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .retrieve()
                                            .toEntity(String.class);
        if( response.getStatusCode().equals(HttpStatus.OK)){
            Orders order = Orders.builder()
                    .userId(orderDTO.userId())
                    .userName(orderDTO.userName())
                    .status(ORDER_STATUS.ACTIVE)
                    .paymentStatus(PAYMENT_STATUS.SUCCESS)
                    .totalAmount(new BigDecimal("0.0"))
                    .shippingAddress(orderDTO.shippingAddress())
                    .build();
            orderPersist = orderRepository.save(order);
            OrderItem orderItem = OrderItem.builder()
                    .productName(orderDTO.productName())
                    .productBatchNo(orderDTO.batchId())
                    .quantity(orderDTO.quantity())
                    .price(orderDTO.amount())
                    .build();
            orderPersist.addItems(orderItem);
            orderPersist = orderRepository.save(orderPersist);
        }
        return orderPersist;
    }
}
