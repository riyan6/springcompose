package org.spring.compose.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestMethodEnum implements BaseEnum {

    GET("get", "get请求"),
    POST("POST", "post请求"),
    DELETE("DELETE", "delete请求"),
    PUT("PUT", "put请求");


    private final String value;
    private final String name;

}
