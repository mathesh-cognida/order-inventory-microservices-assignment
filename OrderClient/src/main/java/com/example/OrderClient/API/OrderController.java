package com.example.OrderClient.API;

import com.example.OrderClient.DTO.OrderDTO;
import com.example.OrderClient.Exception.OrderFailedException;
import com.example.OrderClient.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String greetings(){
        return "Hello";
    }


    @PostMapping("/order")
    public ResponseEntity<String> saveOrders(@RequestBody OrderDTO order){
        try {
            orderService.saveOrder(order);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }catch (Exception e){
            throw  new OrderFailedException("Order is not able to save due to some reason");
        }
    }
}
