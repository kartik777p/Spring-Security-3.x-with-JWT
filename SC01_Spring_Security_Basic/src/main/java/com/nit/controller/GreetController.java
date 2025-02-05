package com.nit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetController {

	@GetMapping("/greet")
	public String greet() {
		return "Welcome to KPIT";
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "+91 8000765678";
	}
	
	
}
