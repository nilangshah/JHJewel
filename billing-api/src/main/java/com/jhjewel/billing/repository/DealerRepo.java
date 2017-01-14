package com.jhjewel.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhjewel.billing.model.Dealer;

/**
 * Spring Data JPA repository to connect our service bean to data
 */
public interface DealerRepo extends JpaRepository<Dealer, Integer> {

}