package com.kodnest.App.ecommerce.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodnest.App.ecommerce.Entitys.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByCategoryName(String categoryName);
}