package com.example.OrderClient.Exception;

public class OrderFailedException extends RuntimeException {
    public OrderFailedException(String s) {
        super(s);
    }
}
