package com.nit.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nit.config.JwtUtil;
import com.nit.entity.Customer;
import com.nit.service.CustomerService;

@RestController
public class CustomerRestController {

	@Autowired
	private CustomerService service;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        // Fetch the username from the authentication context (set by JwtAuthenticationFilter)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // Return a welcome message with the username (authenticated user)
        return new ResponseEntity<>("Welcome to KPIT, " + username + "!", HttpStatus.OK);
    }

	 // Endpoint for refreshing access token
    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody String refreshToken) {
        // Validate refresh token and generate new access token
        String newAccessToken = jwtUtil.refreshToken(refreshToken);

        if (newAccessToken != null) {
            // Return new access token
            return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
        } else {
            // Invalid or expired refresh token
            return new ResponseEntity<>("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
        }
    }
	
	// Login method
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Customer loginRequest) {
		// Authenticate the user
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
				loginRequest.getPwd());
		Authentication authenticate = authManager.authenticate(token);

		if (authenticate.isAuthenticated()) {
			// Generate JWT token on successful authentication
			String jwtToken = jwtUtil.generateToken(loginRequest.getEmail());
			return ResponseEntity.ok("Bearer " + jwtToken);
		} else {
			return new ResponseEntity<>("LOGIN FAILED", HttpStatus.UNAUTHORIZED);
		}
	}
	/*
	 * @PostMapping("/login") public ResponseEntity<String> login(@RequestBody
	 * Customer loginRequest) { // create UsernamePasswordAuthenticationToken with
	 * username and password. UsernamePasswordAuthenticationToken token = new
	 * UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
	 * loginRequest.getPwd());
	 * 
	 * // AUTHENTICATE TOKEN Authentication authenticate =
	 * authManager.authenticate(token); // get auth status boolean status =
	 * authenticate.isAuthenticated(); if (status) { return new
	 * ResponseEntity<String>("LOGIN SUCCESSFUL", HttpStatus.OK); } else { return
	 * new ResponseEntity<String>("LOGIN UN-SUCCESSFUL", HttpStatus.BAD_REQUEST); }
	 * }
	 */

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
