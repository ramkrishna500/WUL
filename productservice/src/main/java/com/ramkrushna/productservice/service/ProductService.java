package com.ramkrushna.productservice.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.ramkrushna.productservice.dto.ProductRequest;
import com.ramkrushna.productservice.dto.ProductResponse;
import com.ramkrushna.productservice.model.Product;
import com.ramkrushna.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
	private final ProductRepository productRepository;
	
	public void createProduct(ProductRequest productRequest) {
		Product product=Product.builder()
				.name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.build();
		productRepository.save(product);
		log.info("Product {} is saved",product.getId());
	}

	public List<ProductResponse> getAllProducts() {
		List<Product>products=productRepository.findAll();
		
		return products.stream().map(product->mapToProductResponse(product)).toList();
		
	}

	private ProductResponse mapToProductResponse(Product product) {	
		return ProductResponse.builder()
				.description(product.getDescription())
				.price(product.getPrice())
				.id(product.getId())
				.name(product.getName())
				.build();
	}
}
