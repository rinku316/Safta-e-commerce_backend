package com.kodnest.App.ecommerce.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodnest.App.ecommerce.Entitys.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User>findByEmail(String email);
	
	Optional<User> findByUsername(String username);
}