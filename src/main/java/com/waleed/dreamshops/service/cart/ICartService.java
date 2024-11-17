package com.waleed.dreamshops.service.cart;

import java.math.BigDecimal;

import com.waleed.dreamshops.model.Cart;
import com.waleed.dreamshops.model.User;

public interface ICartService {
	Cart getCart(Long id);
	void clearCart(Long id);
	BigDecimal getTotalPrice(Long id);//TODO: TotalAmount??
	Cart initializeNewCart(User user);
	Cart getCartByUserId(Long userId);
	
}
