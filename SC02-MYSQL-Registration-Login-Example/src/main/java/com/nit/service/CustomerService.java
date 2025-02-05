package com.nit.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nit.entity.Customer;
import com.nit.repo.ICustomerRepository;

@Service
public class CustomerService implements UserDetailsService {

	@Autowired
	private ICustomerRepository repo;

	@Autowired
	private BCryptPasswordEncoder encoder;

	// register user
	public Boolean registerCustomer(Customer customer) {
		// encode the customer password
		String encode = encoder.encode(customer.getPwd());
		// update encoded pwd in customer obj
		customer.setPwd(encode);
		// save customer obj
		Customer savedCustomer = repo.save(customer);
		return savedCustomer.getCid() != null;
	}

	// internally called by authentication provider for authentication
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetch record from DB
		Customer savedRecord = repo.findByEmail(username);
		// return user details
		return new User(savedRecord.getEmail(), savedRecord.getPwd(), Collections.emptyList());
	}
}
