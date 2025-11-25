package com.example.OrderClient.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "items")
public class Orders {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String userName;
    private ORDER_STATUS status;
    private PAYMENT_STATUS paymentStatus;
    private BigDecimal totalAmount;
    private String shippingAddress;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    public void addItems(OrderItem item){
        if(this.items == null){
            items = new ArrayList<>();
            items.add(item);
        }else{
            this.items.add(item);
        }
        item.setOrder(this);
    }

}
