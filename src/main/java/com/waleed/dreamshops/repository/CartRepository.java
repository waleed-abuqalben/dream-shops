package com.waleed.dreamshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waleed.dreamshops.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
	Cart findByUserId(Long userId);

}
