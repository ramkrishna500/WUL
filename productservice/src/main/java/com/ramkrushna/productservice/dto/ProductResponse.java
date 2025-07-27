package com.ramkrushna.productservice.dto;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value="product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductResponse {
	private String id;
	private String description;
	private String name;
	private BigDecimal price;
}
