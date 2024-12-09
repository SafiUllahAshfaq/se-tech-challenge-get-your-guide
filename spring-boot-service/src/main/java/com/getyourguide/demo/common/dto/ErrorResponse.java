package com.getyourguide.demo.common.dto;

import lombok.Value;

@Value
public class ErrorResponse {
    String code;
    String message;
}