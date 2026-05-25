package com.kodnest.App.ecommerce.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodnest.App.ecommerce.Entitys.Category;
import com.kodnest.App.ecommerce.Entitys.Product;
import com.kodnest.App.ecommerce.Entitys.ProductImage;
import com.kodnest.App.ecommerce.Repositories.CategoryRepository;
import com.kodnest.App.ecommerce.Repositories.ProductImageRepository;
import com.kodnest.App.ecommerce.Repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductImageRepository productImageRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	
	
	public List<Product> getProductsByCategory(String categoryName) {
		if(categoryName != null && !categoryName.isEmpty()) {
			Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);
			
			if(categoryOpt.isPresent()) {
				Category category = categoryOpt.get();
				return productRepository.findByCategory_CategoryId(category.getCategoryId());
			} else {
				throw new RuntimeException("Category not found");
			}
			
		} else {
			return productRepository.findAll();
		}
	}
	
	
	public List<String> getProductImages(Integer productId) {
		List<ProductImage> productImages = productImageRepository.findByProduct_ProductId(productId);
		List<String> imageUrls = new ArrayList<>();
		for (ProductImage image : productImages) {
			imageUrls.add(image.getImageUrl());
		}
		return imageUrls;
	}
}
