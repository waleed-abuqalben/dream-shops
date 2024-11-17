package com.waleed.dreamshops.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.waleed.dreamshops.dto.ImageDto;
import com.waleed.dreamshops.dto.ProductDto;
import com.waleed.dreamshops.exceptions.AlreadyExistsException;
import com.waleed.dreamshops.exceptions.ResourceNotFoundException;
import com.waleed.dreamshops.model.Category;
import com.waleed.dreamshops.model.Image;
import com.waleed.dreamshops.model.Product;
import com.waleed.dreamshops.repository.CategoryRepository;
import com.waleed.dreamshops.repository.ImageRepository;
import com.waleed.dreamshops.repository.ProductRepository;
import com.waleed.dreamshops.request.AddProductRequest;
import com.waleed.dreamshops.request.ProductUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
	
	//final required for @RequiredArgsConstructor to inject Repositories
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ImageRepository imageRepository;
	
	//initialized at dreamshops.config.ShopConfig.modelMapper
	private final ModelMapper modelMapper;
	
	@Override
	public Product addProduct(AddProductRequest request) {
		
		//Check if product already exists
		if(productExistsByBrandAndName(request.getBrand(), request.getName()))
			throw new AlreadyExistsException(request.getBrand() +" "+request.getName()
			+" already exists. You may update this product instead!");
	
		// Check if the category is found in the DB
		// If Yes, set it as the new product category
		// If No, then save it as a new category
		// Then set as the new product category
		Category category = 
				Optional.ofNullable(
						categoryRepository.findByName(request.getCategory().getName()))
				.orElseGet(()-> {
					Category newCategory = new Category(request.getCategory().getName());
					return categoryRepository.save(newCategory);
				});
		request.setCategory(category);
		return productRepository.save(createProduct(request, category));
		
	}
	private Product createProduct(AddProductRequest request, Category category) {
		return new Product(
				request.getName(),
				request.getBrand(),
				request.getPrice(),
				request.getInventory(),
				request.getDescription(),
				category
		);
	}

	@Override
	public Product getProductById(Long id) {
		return productRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException(
					String.format("Product with id:%d not found", id)));
	}

	@Override
	public void deleteProductById(Long id) {
		productRepository.findById(id)
			.ifPresentOrElse(productRepository::delete,
					()-> {throw new ResourceNotFoundException(
							String.format("Product with id:%d not found", id));});
	}
	
	@Override
	public Product updateProduct(ProductUpdateRequest requst, Long productId) {
		
       return productRepository.findById(productId)
    		  .map(existingProduct -> updateExistingProduct(existingProduct, requst))
    		  .map(productRepository :: save)
    		  .orElseThrow(() -> new ResourceNotFoundException(
  					String.format("Product with id:%d not found", requst.getId())));
	}
	private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest requst) {
		existingProduct.setName(requst.getName());
		existingProduct.setBrand(requst.getBrand());
		existingProduct.setPrice(requst.getPrice());
		existingProduct.setInventory(requst.getInventory());
		existingProduct.setDescription(requst.getDescription());
		
		Category category = categoryRepository.findByName(requst.getCategory().getName());
		existingProduct.setCategory(category);
		return existingProduct;
	}


	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public List<Product> getProductsByCategoryName(String categoryName) {
		return productRepository.findByCategoryName(categoryName);
	}

	@Override
	public List<Product> getProductsByBrand(String brand) {
		return productRepository.findByBrand(brand);
	}

	@Override
	public List<Product> getProductsByCategoryNameAndBrand(String categoryName, String brand) {
		return productRepository.findByCategoryNameAndBrand(categoryName, brand);
	}

	@Override
	public List<Product> getProductsByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> getProductsByBrandAndName(String brand, String name) {
		return productRepository.findByBrandAndName(brand, name);
	}

	@Override
	public Long countProductsByBrandAndName(String brand, String name) {
		return productRepository.countByBrandAndName(brand, name);
	}
	
	//TODO: we can Just have it private in the ProductService only!.
	@Override
	public boolean productExistsByBrandAndName(String brand, String name) {
		return productRepository.existsByBrandAndName(brand, name);
	}
	
	@Override
	public List<ProductDto> getConvertedProducts(List<Product> products) {
		return products.stream()
				.map(this::convertToDto).toList();
	}
	
	//To resolve return images issue.
	@Override
	public ProductDto convertToDto(Product product) {
		ProductDto productDto = modelMapper.map(product, ProductDto.class);
		List<Image> images = imageRepository.findByProductId(product.getId());
		List<ImageDto> imageDtos = images.stream()
					.map(image -> modelMapper.map(image, ImageDto.class))
					.toList();
		productDto.setImages(imageDtos);
		return productDto;
	}

}
