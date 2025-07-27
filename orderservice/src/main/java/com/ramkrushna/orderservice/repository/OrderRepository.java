package com.ramkrushna.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramkrushna.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
}
