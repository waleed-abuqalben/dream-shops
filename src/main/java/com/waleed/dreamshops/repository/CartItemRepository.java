package com.waleed.dreamshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waleed.dreamshops.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	void deleteAllByCartId(Long cartId);


}
