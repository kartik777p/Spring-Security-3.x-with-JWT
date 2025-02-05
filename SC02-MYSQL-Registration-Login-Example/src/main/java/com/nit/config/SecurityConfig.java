package com.nit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nit.service.CustomerService;

import lombok.SneakyThrows;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomerService customerService;

	// for Encypt the password
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	//TO LOAD DATA FROM DB AND GIVE TO AUTH MANAGER FOR AUTHENTICATION.
	@Bean
	public AuthenticationProvider authProvider() {
		// create Auth provider obj
		DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();

		// set encoder() and userDetails obj
		daoAuthProvider.setPasswordEncoder(encoder()); // to compare password
		daoAuthProvider.setUserDetailsService(customerService); // loadUserByUsername load
		// return
		return daoAuthProvider;
	}
	
	
	// RESPONSIBLE TO CHECK LOGIN TO CHECK LOGIN CREDENTIALS ARE VALID OR NOT
  @Bean
  @SneakyThrows
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
		return config.getAuthenticationManager();
	}

	// NOTE :- WHILE WORKING WITH POST REQUEST WE HAVE TO DESABLE CSRF() OTHERWISE
	// IT WILL GIVE  FORBIDDEN ISSUE
	// approach 1
	@Bean
	@SneakyThrows
	public SecurityFilterChain configure(HttpSecurity http) {
		http.csrf().disable()
		.authorizeHttpRequests((request) -> {
			request.requestMatchers("/register","/login").permitAll()
			.anyRequest().authenticated();
		});

		return http.csrf().disable().build();
	}

	/*
	 * //approach 2
	 * 
	 * @Bean
	 * 
	 * @SneakyThrows public SecurityFilterChain configure(HttpSecurity http) {
	 * http.authorizeHttpRequests((request) -> {
	 * request.requestMatchers("/register").permitAll().anyRequest().authenticated()
	 * ; });
	 * 
	 * return http.csrf().disable().build(); }
	 */

}
