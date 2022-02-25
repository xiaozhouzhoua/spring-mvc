package com.spring.springmvc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Price {
    /**
     * 系数
     */
    private Integer coefficient;

    /**
     * 指数
     */
    private Integer exponent;

    /**
     * 货币
     */
    private String currency;
}
