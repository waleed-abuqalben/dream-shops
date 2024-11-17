package com.waleed.dreamshops.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.waleed.dreamshops.exceptions.AlreadyExistsException;
import com.waleed.dreamshops.exceptions.ResourceNotFoundException;
import com.waleed.dreamshops.model.Category;
import com.waleed.dreamshops.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

	private final CategoryRepository categoryRepository;
	@Override
	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id)
			.orElseThrow(() -> 
				new ResourceNotFoundException(
					String.format("Category with id:%d not found",id)
				));
	}

	@Override
	public Category getCategoryByName(String name) {
		return categoryRepository.findByName(name);
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category addCategory(Category category) {
		return Optional.of(category)
				.filter(e -> !categoryRepository.existsByName(e.getName()))
				.map(categoryRepository :: save)
				.orElseThrow(() -> new AlreadyExistsException(category.getName()+" already exists"));
	}

	@Override
	public Category updateCategory(Category category, Long id) {
		return Optional.ofNullable(getCategoryById(id))
				.map(oldCategory -> {
					oldCategory.setName(category.getName());
					return categoryRepository.save(oldCategory);
				})
				.orElseThrow(() -> new ResourceNotFoundException(
				     String.format("Category with id:%d not found",id)));
	}


	@Override
	public void deleteCategoryById(Long id) {
		categoryRepository.findById(id)
		.ifPresentOrElse(categoryRepository :: delete, 
			() -> {
				throw new ResourceNotFoundException(
		           String.format("Category with id:%d not found",id));
			});
		
	}

}
