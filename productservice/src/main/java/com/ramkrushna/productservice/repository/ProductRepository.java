package com.ramkrushna.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ramkrushna.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product,String>{

}
