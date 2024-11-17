package com.waleed.dreamshops.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.waleed.dreamshops.exceptions.ResourceNotFoundException;
import com.waleed.dreamshops.model.Cart;
import com.waleed.dreamshops.model.CartItem;
import com.waleed.dreamshops.model.Product;
import com.waleed.dreamshops.repository.CartItemRepository;
import com.waleed.dreamshops.repository.CartRepository;
import com.waleed.dreamshops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{

	private final CartItemRepository cartItemRepository;
	private final CartRepository cartRepository;
	private final IProductService productService;
	private final ICartService cartService;
	
	@Override
	public void addItemToCart(Long cartId, Long productId, int quantity) {
		//1. Get the cart.
		//2. Get the product.
		//3. Check if the product already in the cart.
		//4. If Yes, then increase the quantity with requested quantity.
		//5. If No,  then initiate a new CartItem entry.
		Cart cart  = cartService.getCart(cartId);
		Product product = productService.getProductById(productId);
		CartItem cartItem = cart.getItems().stream()
				.filter(item -> item.getProduct().getId() == product.getId())
				.findFirst().orElse(new CartItem());
		if(cartItem.getId() == null) {//TODO: == 0 ??
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			cartItem.setUnitPrice(product.getPrice());
		} else {
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		}
		cartItem.setTotalPrice();
		cart.addItem(cartItem);
		cartItemRepository.save(cartItem);
		cartRepository.save(cart);
	}

	@Override
	public void removeItemFromCart(Long cartId, Long productId) {
		Cart cart  = cartService.getCart(cartId);
		CartItem itemToRemove = getCartItem(cartId, productId);
		cart.removeItem(itemToRemove);
		cartRepository.save(cart);
		
	}
	
	@Override
	public void updateItemQuantity(Long cartId, Long productId, int quantity) {
		Cart cart  = cartService.getCart(cartId);
		cart.getItems().stream()
			.filter(item -> item.getProduct().getId().equals(productId))
			.findFirst()
			.ifPresent(item -> {
				item.setQuantity(quantity);
				item.setUnitPrice(item.getProduct().getPrice());
				item.setTotalPrice();
			});
		BigDecimal totalAmount = cart.getItems().stream()
				.map(CartItem :: getTotalPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		cart.setTotalAmount(totalAmount);
		cartRepository.save(cart);
	}
	@Override
	public CartItem getCartItem(Long cartId, Long productId) {
		Cart cart  = cartService.getCart(cartId);
		return  cart.getItems().stream()
				.filter(item -> item.getProduct().getId().equals(productId))
				.findFirst().orElseThrow(
					() -> new ResourceNotFoundException(
							"item not found. productId: "+productId));
	}

	
	
	

}
