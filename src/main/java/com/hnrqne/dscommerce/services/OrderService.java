package com.hnrqne.dscommerce.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnrqne.dscommerce.dto.OrderDTO;
import com.hnrqne.dscommerce.dto.OrderItemDTO;
import com.hnrqne.dscommerce.entities.Order;
import com.hnrqne.dscommerce.entities.OrderItem;
import com.hnrqne.dscommerce.entities.OrderStatus;
import com.hnrqne.dscommerce.entities.Product;
import com.hnrqne.dscommerce.repositories.OrderItemRepository;
import com.hnrqne.dscommerce.repositories.OrderRepository;
import com.hnrqne.dscommerce.repositories.ProductRepository;
import com.hnrqne.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired OrderItemRepository orderItemRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthService authService;
	
	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {
		
		Order order = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado"));
		authService.validateSelfOrAdmin(order.getClient().getId());
		return new OrderDTO(order);
	}

	@Transactional
	public OrderDTO insert(OrderDTO dto) {
		
		Order order = new Order();
		order.setMoment(Instant.now());
		order.setStatus(OrderStatus.WAITING_PAYMENT);
		
		order.setClient(userService.authenticated());
		
		for (OrderItemDTO itemDto : dto.getItems()) {
			Product product = productRepository.getReferenceById(itemDto.getProductId());
			OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());
			order.getItems().add(item);
		}
		
		repository.save(order);
		orderItemRepository.saveAll(order.getItems());
		
		return new OrderDTO(order);
	}
	

}
