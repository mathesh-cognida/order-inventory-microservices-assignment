package com.example.InventoryClient.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = "batches")
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private Long productId;
    private String productName;
    private String productDescription;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<BatchProduct> batches = new ArrayList<>();

    public void addBatches(BatchProduct batchProduct) {
        if(batches ==null){
            batches = new ArrayList<>();
            batches.add(batchProduct);
        }else{
            batches.add(batchProduct);
        }

        batchProduct.setProduct(this);
    }

    public void removeBatchProduct(BatchProduct batchProduct) {
        batches.remove(batchProduct);
        batchProduct.setProduct(null);
    }

}
