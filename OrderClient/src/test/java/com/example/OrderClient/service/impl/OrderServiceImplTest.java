package com.example.OrderClient.service.impl;

import com.example.OrderClient.DTO.OrderDTO;
import com.example.OrderClient.model.ORDER_STATUS;
import com.example.OrderClient.model.Orders;
import com.example.OrderClient.model.PAYMENT_STATUS;
import com.example.OrderClient.repository.OrderRepository;
import com.example.OrderClient.service.OrderService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestClient restClient;

    @InjectMocks
    OrderServiceImpl orderService;

    private OrderDTO orderDTO;
    private Orders order;
    RestClient.RequestHeadersUriSpec<?> uriSpec;
    RestClient.RequestHeadersSpec<?> headerSpec;
    RestClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        orderDTO = new OrderDTO(
                101l,
                "Mathesh",
                "GRT",
                103l,
                "LUX",
                202l,
                20.2,
                120
        );
        order = Orders.builder()
                .id(1l)
                .userId(101l)
                .userName("Mathesh")
                .status(ORDER_STATUS.ACTIVE)
                .paymentStatus(PAYMENT_STATUS.SUCCESS)
                .shippingAddress("GRT")
                .totalAmount(new BigDecimal("102")).build();


        uriSpec = Mockito.mock(RestClient.RequestHeadersUriSpec.class);
        headerSpec = Mockito.mock(RestClient.RequestHeadersSpec.class);
        responseSpec = Mockito.mock(RestClient.ResponseSpec.class);
    }

    @Test
    void testFindByOrderId() {
        when(OrderServiceImplTest.this.orderRepository.findByOrderId(1l)).thenReturn(Optional.of(order));
        Orders result = orderService.findByOrderId(1l).get();
        assertEquals("Mathesh", result.getUserName());
        verify(OrderServiceImplTest.this.orderRepository, times(1)).findByOrderId(1l);
    }

    @Test
    void testSaveOrder() {
//        when(restClient.get()).thenReturn(uriSpec);
//        OngoingStubbing<?> success = when(uriSpec.uri("/inventory/update")).thenReturn("Success");
//        when(headerSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.body(String.class)).thenReturn("Sunny");
//        String forecast = weatherService.getForecast("Delhi");
//        assertEquals("Sunny", forecast);
    }
}