package com.accenture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LocationDeVehiculeApplication implements CommandLineRunner {

	@Autowired
	PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(LocationDeVehiculeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		String passwordChiffre = passwordEncoder.encode("Password123&");
//		System.out.println(passwordChiffre);
	}
}
