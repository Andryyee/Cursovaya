package com.example.cursovaya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.cursovaya.model") // Сканирование сущностей
@EnableJpaRepositories(basePackages = "com.example.cursovaya.repository") // Сканирование репозиториев
public class CursovayaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CursovayaApplication.class, args);
	}

}
