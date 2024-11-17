package com.waleed.dreamshops.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.waleed.dreamshops.exceptions.ResourceNotFoundException;
import com.waleed.dreamshops.model.Cart;
import com.waleed.dreamshops.model.User;
import com.waleed.dreamshops.repository.UserRepository;
import com.waleed.dreamshops.response.ApiResponse;
import com.waleed.dreamshops.service.cart.ICartItemService;
import com.waleed.dreamshops.service.cart.ICartService;
import com.waleed.dreamshops.service.user.IUserService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

	private final ICartItemService cartItemService;
	private final ICartService cartService;
	private final IUserService userService;
	@PostMapping("/item/add")
	public ResponseEntity<ApiResponse> addItemToCart(
			@RequestParam Long productId, @RequestParam Integer quantity){
		try {
			
			User user = userService.getAuthenticatedUser(); 
			Cart cart = cartService.initializeNewCart(user);
			
			cartItemService.addItemToCart(cart.getId(), productId, quantity);
			return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		} catch (JwtException e) {
			return ResponseEntity.status(UNAUTHORIZED)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	@DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
	public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
			@PathVariable Long itemId) {
		try {
			cartItemService.removeItemFromCart(cartId, itemId);
			return ResponseEntity.ok(new ApiResponse("Remove Item Success", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	@PutMapping("/cart/{cartId}/item/{itemId}/update")
	public ResponseEntity<ApiResponse> updateItemQuantity(
			@PathVariable Long cartId,
			@PathVariable Long itemId, @RequestParam Integer quantity) {
		try {
			cartItemService.updateItemQuantity(cartId, itemId, quantity);
			return ResponseEntity.ok(new ApiResponse("Update Item quantity Success", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	
}
