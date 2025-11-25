package com.example.OrderClient.service;

import com.example.OrderClient.DTO.OrderDTO;
import com.example.OrderClient.model.ORDER_STATUS;
import com.example.OrderClient.model.Orders;
import com.example.OrderClient.model.PAYMENT_STATUS;
import com.example.OrderClient.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testFindByProductId(){
//        List listMock = mock(List.class);
//        when(listMock.add(anyString())).thenReturn(false);
        OrderDTO orderDTO = new OrderDTO(
                101l,
                "Mathesh",
                "GRT",
                103l,
                "LUX",
                202l,
                20.2,
                120
        );
        Orders order = Orders.builder()
                .id(1l)
                .userId(101l)
                .userName("Mathesh")
                .status(ORDER_STATUS.ACTIVE)
                .paymentStatus(PAYMENT_STATUS.SUCCESS)
                .shippingAddress("GRT")
                .totalAmount(new BigDecimal("102")).build();
        when(orderRepository.save(any(Orders.class))).thenReturn(order);
        Orders orderPersist = orderService.saveOrder(orderDTO);
        assertEquals("Mathesh", order.getUserName());

    }
}
