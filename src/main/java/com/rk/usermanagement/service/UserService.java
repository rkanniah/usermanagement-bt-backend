package com.rk.usermanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rk.usermanagement.exception.ResourceDatabaseOperationException;
import com.rk.usermanagement.exception.ResourceNotFoundException;
import com.rk.usermanagement.model.User;
import com.rk.usermanagement.repository.UserRepository;

@Service
@Transactional
public class UserService {

	@Autowired
	public UserRepository userRepository;

	@Async("myExecutor")
	public Future<User> addUser(User user) {

		try {
			return CompletableFuture.completedFuture(userRepository.save(user));
		} catch (DataAccessException dae) {
			throw new ResourceDatabaseOperationException("error creating user", dae);
		}
	}

	@Async("myExecutor")
	public Future<User> updateUser(User userToUpdate) {

		User user = userRepository.findByEmail(userToUpdate.getEmail());
		if (user == null) {
			throw new ResourceNotFoundException("user to update not found");
		}

		try {
			return CompletableFuture.completedFuture(userRepository.save(userToUpdate));
		} catch (DataAccessException dae) {
			throw new ResourceDatabaseOperationException("error updating user", dae);
		}
	}

	@Async("myExecutor")
	public void deleteUser(User user) {

		try {
			userRepository.delete(user);
		} catch (DataAccessException dae) {
			throw new ResourceDatabaseOperationException("error deleting user", dae);
		}
	}

	@Async("myExecutor")
	public Future<List<User>> getAllUsers() {

		try {
			List<User> users = new ArrayList<>();
			Iterable<User> iterable = userRepository.findAll();
			iterable.forEach(users::add);
			return CompletableFuture.completedFuture(users);

		} catch (DataAccessException dae) {
			throw new ResourceDatabaseOperationException("error retrieving all users", dae);
		}
	}

	@Async("myExecutor")
	public Future<User> findUser(String email) {

		try {
			return CompletableFuture.completedFuture(userRepository.findByEmail(email));
		} catch (DataAccessException dae) {
			throw new ResourceDatabaseOperationException("error retrieving user by email", dae);
		}
	}
}
