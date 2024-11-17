package com.waleed.dreamshops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waleed.dreamshops.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	List<Product> findByCategoryName(String categoryName);

	List<Product> findByBrand(String brand);

	List<Product> findByCategoryNameAndBrand(String categoryName, String brand);

	List<Product> findByName(String name);

	List<Product> findByBrandAndName(String brand, String name);

	Long countByBrandAndName(String brand, String name);

	boolean existsByBrandAndName(String brand, String name);


}
