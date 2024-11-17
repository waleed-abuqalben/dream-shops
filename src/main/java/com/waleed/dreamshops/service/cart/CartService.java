package com.waleed.dreamshops.service.cart;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.waleed.dreamshops.exceptions.ResourceNotFoundException;
import com.waleed.dreamshops.model.Cart;
import com.waleed.dreamshops.model.User;
import com.waleed.dreamshops.repository.CartItemRepository;
import com.waleed.dreamshops.repository.CartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	@Override
	public Cart getCart(Long id) {
		Cart cart = cartRepository.findById(id)
			.orElseThrow(() -> 
				new ResourceNotFoundException("Cart not found. id:"+id));
		BigDecimal totalAmount = cart.getTotalAmount();
		//cart.setTotalAmount(totalAmount);
		return cartRepository.save(cart);
	}

	@Transactional
	@Override
	public void clearCart(Long id) {
		Cart cart = getCart(id);
		cartItemRepository.deleteAllByCartId(id);
		cart.getItems().clear();
		cartRepository.deleteById(id);
	}
		
	
	@Override
	public BigDecimal getTotalPrice(Long id) {
		Cart cart = getCart(id);
		return cart.getTotalAmount();

	}
	@Override
	public Cart initializeNewCart(User user) {
		return Optional.ofNullable(getCartByUserId(user.getId()))
				.orElseGet(() -> {
					Cart cart = new Cart();
					cart.setUser(user);
					return cartRepository.save(cart);
				});
	}

	
	@Override
	public Cart getCartByUserId(Long userId) {
		return cartRepository.findByUserId(userId);
				
	}

}
