package com.programmingtechie.order_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {
     private String skuCode;
    private BigDecimal price;
    private Integer quantity;
    private Long id;

}
