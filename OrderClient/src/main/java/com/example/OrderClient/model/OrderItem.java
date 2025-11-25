package com.example.OrderClient.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "order")
public class OrderItem {
    @Id
    @GeneratedValue
    private Long id;
//    private Long order_id;
    private Long productId;
    private String productName;
    private long productBatchNo;
    private int quantity;
    private double price;
    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    private Orders order;
}
