package com.waleed.dreamshops.service.order;

import java.util.List;

import com.waleed.dreamshops.dto.OrderDto;
import com.waleed.dreamshops.model.Order;

public interface IOrderService {

	Order placeOrder(Long userId);
	OrderDto getOrder(Long orderId);
	List<OrderDto> getUserOrders(Long userId);
	OrderDto convertToDto(Order order);
}
