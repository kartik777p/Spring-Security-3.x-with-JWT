package com.nit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.entity.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Integer> {

	public Customer findByEmail(String email);
}
