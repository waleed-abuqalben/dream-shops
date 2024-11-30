package com.waleed.dreamshops.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.waleed.dreamshops.dto.OrderDto;
import com.waleed.dreamshops.exceptions.ResourceNotFoundException;
import com.waleed.dreamshops.model.Order;
import com.waleed.dreamshops.model.User;
import com.waleed.dreamshops.response.ApiResponse;
import com.waleed.dreamshops.service.order.IOrderService;
import com.waleed.dreamshops.service.user.IUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

	private final IOrderService orderService;
	private final IUserService userService;
	
	@PostMapping("/order")
	public ResponseEntity<ApiResponse> createOrder() {
		try {
			User user = userService.getAuthenticatedUser(); 
			Order order = orderService.placeOrder(user.getId());
			OrderDto orderDto = orderService.convertToDto(order);
			return ResponseEntity.ok(new ApiResponse("Item Order Success!", orderDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR)
				.body(new ApiResponse("Error Occuerd", e.getMessage()));
		}
	}
	
	@GetMapping("/{orderId}/order")
	public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
		try {
			OrderDto order = orderService.getOrder(orderId);
			return ResponseEntity.ok(new ApiResponse("success!", order));
		}catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse("Opps!", e.getMessage()));
		}
	}
	
	@GetMapping("/{userId}/orders")
	public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
		try {
			List<OrderDto> orders = orderService.getUserOrders(userId);
			return ResponseEntity.ok(new ApiResponse("success!", orders));
		}catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse("Opps!", e.getMessage()));
		}
	}
	
	
}
