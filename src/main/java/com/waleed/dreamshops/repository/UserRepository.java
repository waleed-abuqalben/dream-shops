package com.waleed.dreamshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waleed.dreamshops.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	boolean existsByEmail(String email);

	User findByEmail(String email);

}
