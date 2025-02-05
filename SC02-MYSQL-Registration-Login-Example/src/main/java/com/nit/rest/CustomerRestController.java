package com.nit.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nit.entity.Customer;
import com.nit.service.CustomerService;

@RestController
public class CustomerRestController {

	@Autowired
	private CustomerService service;

	@Autowired
	private AuthenticationManager authManager;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Customer loginRequest) {
		// create UsernamePasswordAuthenticationToken with username and password.
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
				loginRequest.getPwd());

		// AUTHENTICATE TOKEN
		Authentication authenticate = authManager.authenticate(token);
		// get auth status
		boolean status = authenticate.isAuthenticated();
		if (status) {
			return new ResponseEntity<String>("LOGIN SUCCESSFUL", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("LOGIN UN-SUCCESSFUL", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
		Boolean registerCustomer = service.registerCustomer(customer);
		if (registerCustomer) {
			return new ResponseEntity<String>("SUCCESS", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
