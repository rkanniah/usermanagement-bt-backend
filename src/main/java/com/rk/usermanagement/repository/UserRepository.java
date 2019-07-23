package com.rk.usermanagement.repository;

import org.springframework.data.repository.CrudRepository;

import com.rk.usermanagement.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByEmail(String email);
}
