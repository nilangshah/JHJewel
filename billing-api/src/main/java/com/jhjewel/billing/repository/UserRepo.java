package com.jhjewel.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhjewel.billing.model.User;

/**
 * Spring Data JPA repository to connect our service bean to data
 */
public interface UserRepo extends JpaRepository<User, Integer> {

	public User findByUsernameIgnoreCase(String userName);
}