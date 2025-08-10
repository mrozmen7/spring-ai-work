package com.ozmenyavuz;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.ozmenyavuz")
@EntityScan(basePackages = "com.ozmenyavuz")
@SpringBootApplication
public class SpringAlStudioApplication {

	public static void main(String[] args) {
//		// ✅ .env dosyasını yükle
//		Dotenv dotenv = Dotenv.load();
//
//		// ✅ JVM ortam değişkeni olarak ata
//		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(SpringAlStudioApplication.class, args);
	}
}