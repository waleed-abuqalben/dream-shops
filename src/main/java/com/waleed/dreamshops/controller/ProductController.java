package com.waleed.dreamshops.controller;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.waleed.dreamshops.dto.ProductDto;
import com.waleed.dreamshops.exceptions.AlreadyExistsException;
import com.waleed.dreamshops.exceptions.ResourceNotFoundException;
import com.waleed.dreamshops.model.Product;
import com.waleed.dreamshops.request.AddProductRequest;
import com.waleed.dreamshops.request.ProductUpdateRequest;
import com.waleed.dreamshops.response.ApiResponse;
import com.waleed.dreamshops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor//To inject productService 
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
	private final IProductService productService;
	
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllProducts(){
		List<Product> products = productService.getAllProducts();
		List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
		return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
	}
	
	@GetMapping("/product/{productId}/product")
	public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
		try {
			Product product = productService.getProductById(productId);
			ProductDto productDto = productService.convertToDto(product); 
			return ResponseEntity.ok(new ApiResponse("success", productDto));
		}catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request) {
		try {
			Product savedProduct = productService.addProduct(request);
			return ResponseEntity.ok(new ApiResponse("Add product success", savedProduct));
		}catch (AlreadyExistsException e) {
			return ResponseEntity.status(CONFLICT)
					.body(new ApiResponse(e.getMessage(), null));
		}
		
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("product/{productId}/update")
	public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request,
			@PathVariable Long productId) {
		try {
			Product updatedProduct = productService.updateProduct(request, productId);
			ProductDto productDto = productService.convertToDto(updatedProduct);
			return ResponseEntity.ok(new ApiResponse("Update product success", productDto));
		}catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/product/{productId}/delete")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
		try {
			productService.deleteProductById(productId);
			return ResponseEntity.ok(new ApiResponse("Delete product success", null));
		}catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/products/by/brand-and-name")
	public ResponseEntity<ApiResponse> getProductsByBrandAndName(
			@RequestParam String brandName, @RequestParam String ProductName) {
		try {
			List<Product> products = 
					productService.getProductsByBrandAndName(brandName, ProductName);
			if(products.isEmpty())
				return ResponseEntity.status(NOT_FOUND)
						.body(new ApiResponse("No product found", null));
			
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
		}catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/products/by/category-and-brand")
	public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(
			@RequestParam String category, @RequestParam String brand) {
		try {
			List<Product> products = 
					productService.getProductsByCategoryNameAndBrand(category, brand);
			if(products.isEmpty())
				return ResponseEntity.status(NOT_FOUND)
						.body(new ApiResponse("No product found", null));
			
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
		}catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/products/{name}/products")
	public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
		try {
			List<Product> products =productService.getProductsByName(name);
			if(products.isEmpty())
				return ResponseEntity.status(NOT_FOUND)
						.body(new ApiResponse("No product found", null));
			
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
		}catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	@GetMapping("/product/by-brand")
	public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
		try {
			List<Product> products =productService.getProductsByBrand(brand);
			if(products.isEmpty())
				return ResponseEntity.status(NOT_FOUND)
						.body(new ApiResponse("No product found", null));
			
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
		}catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/product/{category}/all/products")
	public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
		try {
			List<Product> products =productService.getProductsByCategoryName(category);
		if(products.isEmpty())
				return ResponseEntity.status(NOT_FOUND)
						.body(new ApiResponse("No product found", null));
			
		List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
		}catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            var productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
        } catch (Exception e) {
        	return ResponseEntity.status(INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(e.getMessage(), null));
        }
    }
	


	
	
	
	

}
