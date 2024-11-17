package com.waleed.dreamshops.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrdetItemDto {

	private Long productId;
	private String productName;
	private String productBrand;
	private int quantity;
	private BigDecimal price;
}
