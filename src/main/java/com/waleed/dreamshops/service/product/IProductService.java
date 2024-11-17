package com.waleed.dreamshops.service.product;

import java.util.List;

import com.waleed.dreamshops.dto.ProductDto;
import com.waleed.dreamshops.model.Product;
import com.waleed.dreamshops.request.AddProductRequest;
import com.waleed.dreamshops.request.ProductUpdateRequest;

public interface IProductService {

	Product addProduct(AddProductRequest product);
	Product getProductById(Long id);
	Product updateProduct(ProductUpdateRequest request, Long productId);
	void deleteProductById(Long id);
	List<Product> getAllProducts();
	List<Product> getProductsByCategoryName(String categoryName);
	List<Product> getProductsByBrand(String brand);
	List<Product> getProductsByCategoryNameAndBrand(String categoryName, String brand);
	List<Product> getProductsByName(String name);
	List<Product> getProductsByBrandAndName(String brand, String name);
	Long countProductsByBrandAndName(String brand, String name);
	ProductDto convertToDto(Product product);
	List<ProductDto> getConvertedProducts(List<Product> products);
	boolean productExistsByBrandAndName(String brand, String name);
	
}
