package com.ramkrushna.inventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ramkrushna.inventoryservice.model.Inventory;
import com.ramkrushna.inventoryservice.repository.InventoryRepository;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
		
	}
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args ->{
			Inventory inventory=new Inventory();
			inventory.setSkuCode("iphone_17");
			inventory.setQuantity(10);
			
			Inventory inventory1=new Inventory();
			inventory1.setSkuCode("iphone_17_red");
			inventory1.setQuantity(0);
			
			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}

}
