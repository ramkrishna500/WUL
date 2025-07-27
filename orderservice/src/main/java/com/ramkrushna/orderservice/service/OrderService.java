package com.ramkrushna.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.ramkrushna.orderservice.dto.InventoryResponse;
import com.ramkrushna.orderservice.dto.OrderLineItmesDto;
import com.ramkrushna.orderservice.dto.OrderRequest;
import com.ramkrushna.orderservice.model.Order;
import com.ramkrushna.orderservice.model.OrderLineItems;
import com.ramkrushna.orderservice.repository.OrderRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	
	public void placeOrder(OrderRequest orderRequest) {
		Order order=new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItems> orderLineItems=orderRequest.getOrderLineItemsDto()
			.stream()
			.map(orderLineItemsDto->mapToDto(orderLineItemsDto))
			.toList();
		order.setOrderLineItemsList(orderLineItems);
		
		List<String>skuCodes = order.getOrderLineItemsList()
			.stream()
			.map(OrderLineItems::getSkuCode)
			.toList();
		
		InventoryResponse[] inventoryResponseArray=webClientBuilder.build().get()
						.uri("http://inventory-service/api/inventory",
						uriBuilder->uriBuilder.queryParam("skuCode",skuCodes).build())  
						.retrieve()
						.bodyToMono(InventoryResponse[].class)
						.block();
		
		boolean allProductsInStocks=Arrays.stream(inventoryResponseArray)
									.allMatch(InventoryResponse :: isInStock);
		if(allProductsInStocks) {
			orderRepository.save(order);			
		}else {
			throw new IllegalArgumentException("Product is not in stock.Please try later");
		}
		
	}

	private OrderLineItems mapToDto(OrderLineItmesDto orderLineItemsDto) {
		OrderLineItems orderLineItems=new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}
}
