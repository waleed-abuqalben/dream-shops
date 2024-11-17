package com.waleed.dreamshops.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.waleed.dreamshops.enums.OrderStatus;

import lombok.Data;

@Data
public class OrderDto {
	private Long orderId;
	private LocalDate orderDate;
	private BigDecimal totalAmount;
	private OrderStatus orderStatus;
	private Set<OrdetItemDto> items = new HashSet<>();
}
