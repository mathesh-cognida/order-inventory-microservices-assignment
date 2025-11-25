package com.example.InventoryClient.API;

import com.example.InventoryClient.DTO.ProductDTO;
import com.example.InventoryClient.DTO.ProductOnboardDTO;
import com.example.InventoryClient.Exception.InvalidArgumentException;
import com.example.InventoryClient.Model.BatchProduct;
import com.example.InventoryClient.Model.Product;
import com.example.InventoryClient.service.BatchProductService;
import com.example.InventoryClient.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class ProductController {
    private final ProductService productService;
    private final BatchProductService batchProductService;

    ProductController(ProductService productService, BatchProductService batchProductService){
        this.productService = productService;
        this.batchProductService = batchProductService;
    }

    @GetMapping("/")
    public String greetings(){
        return "hello from inventory";
    }

    @GetMapping("/inventory/{productId}")
    public ResponseEntity<List<BatchProduct>> getProductDetails(@PathVariable long productId){
        try{
            List<BatchProduct> batches =   productService
                    .getProduct(productId)
                    .get()
                    .getBatches()
                    .stream()
                    .sorted(Comparator.comparing(BatchProduct::getExpiryTime).reversed())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(batches, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(new ArrayList<BatchProduct>(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/inventory")
    public ResponseEntity<Product> addProductsToInventory(@RequestBody ProductOnboardDTO productOnboardDTO){
        Product persistProduct = productService.saveProductAndBatchProduct(productOnboardDTO);

        Product result = Product.builder()
                .id(persistProduct.getId())
                .productDescription(productOnboardDTO.productDescription())
                .productName(productOnboardDTO.productName())
                .batches(persistProduct.getBatches())
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/inventory/update")
    public ResponseEntity<String> updateInventory(@RequestBody ProductDTO productDTO){
        BatchProduct batch = batchProductService.getBatchProduct(productDTO.batchId()).get();
        if(batch.getQuantity() - productDTO.quantity() >= 0 && productDTO.quantity()>0 ) {
            batch.setQuantity(batch.getQuantity() - productDTO.quantity());
            batchProductService.saveBatchProduct(batch);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }else{
            throw new InvalidArgumentException("Quantity of the batch is smaller than the available");
        }

    }

}
