package com.ramkrushna.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.ramkrushna.orderservice.dto.InventoryResponse;
import com.ramkrushna.orderservice.dto.OrderLineItmesDto;
import com.ramkrushna.orderservice.dto.OrderRequest;
import com.ramkrushna.orderservice.event.OrderPlaceEvent;
import com.ramkrushna.orderservice.model.Order;
import com.ramkrushna.orderservice.model.OrderLineItems;
import com.ramkrushna.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlaceEvent> kafkaTemplate;

    public Mono<String> placeOrder(OrderRequest orderRequest) {
        // build Order aggregate
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDto()
                .stream()
                .map(this::mapToEntity)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        // collect SKU codes
        List<String> skuCodes = orderLineItems.stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // call inventory service (non-blocking)
        return webClientBuilder.build()
                .get()
                .uri("lb://inventoryservice/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .map(inventoryResponseArray -> {
                    boolean allInStock = Arrays.stream(inventoryResponseArray)
                            .allMatch(InventoryResponse::isInStock);

                    if (allInStock) {
                        orderRepository.save(order);  // <-- still blocking JPA call
                        kafkaTemplate.send("notificationTopic", new OrderPlaceEvent(order.getOrderNumber()));
                        return "Order placed successfully";
                    } else {
                        throw new IllegalArgumentException("Product is not in stock. Please try later");
                    }
                });
    }

    // helper to map DTO → entity
    private OrderLineItems mapToEntity(OrderLineItmesDto dto) {
        OrderLineItems item = new OrderLineItems();
        item.setPrice(dto.getPrice());
        item.setQuantity(dto.getQuantity());
        item.setSkuCode(dto.getSkuCode());
        return item;
    }
}
