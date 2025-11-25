package com.example.OrderClient.Exception;

import java.time.Instant;

public class ExceptionResponse {
        private String message;
        private String errorCode;
        private int status;
        private Instant timestamp;

        public ExceptionResponse(String message, String errorCode, int status) {
            this.message = message;
            this.errorCode = errorCode;
            this.status = status;
            this.timestamp = Instant.now();
        }
}
