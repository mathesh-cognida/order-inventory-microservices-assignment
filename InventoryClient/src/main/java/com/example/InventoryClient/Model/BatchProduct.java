package com.example.InventoryClient.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = "product")
public class BatchProduct {
    @Id
    @GeneratedValue
    private long id;
    private long batchId;
    private String batchType;
    private Instant expiryTime;
    private double price;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;
}
