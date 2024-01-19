package com.hnrqne.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hnrqne.dscommerce.entities.OrderItem;
import com.hnrqne.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK>{
	
}

