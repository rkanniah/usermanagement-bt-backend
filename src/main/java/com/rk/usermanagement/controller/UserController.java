package com.rk.usermanagement.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.rk.usermanagement.exception.ResourceDatabaseOperationException;
import com.rk.usermanagement.exception.ResourceNotFoundException;
import com.rk.usermanagement.model.User;
import com.rk.usermanagement.service.UserService;

@RestController
public class UserController {

	private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addUser(@RequestBody User user) {

		LOGGER.info("Adding a user..." + user);

		if (user != null && CollectionUtils.isEmpty(user.getRole())) {
			return new ResponseEntity<>("role cannot be empty or null!", HttpStatus.BAD_REQUEST);
		}

		try {
			Future<User> newUser = userService.addUser(user);
			return new ResponseEntity<>(newUser.get(), HttpStatus.CREATED);

		} catch (ResourceDatabaseOperationException | InterruptedException | ExecutionException dbe) {
			LOGGER.log(Level.SEVERE, "Create DB operation error...", dbe);
			return new ResponseEntity<>("Create DB operation error...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@RequestBody User user) {

		LOGGER.info("Updating user...");

		if (user != null && CollectionUtils.isEmpty(user.getRole())) {
			return new ResponseEntity<>("role cannot be empty or null for update!", HttpStatus.BAD_REQUEST);
		}

		try {
			Future<User> updatedUser = userService.updateUser(user);
			return new ResponseEntity<>(updatedUser.get(), HttpStatus.ACCEPTED);

		} catch (ResourceNotFoundException nfe) {
			return new ResponseEntity<>("User not found for update: " + user, HttpStatus.NOT_MODIFIED);

		} catch (ResourceDatabaseOperationException | InterruptedException | ExecutionException dbe) {
			LOGGER.log(Level.SEVERE, "Update DB operation error...", dbe);
			return new ResponseEntity<>("Update DB operation error...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@RequestBody User user) {

		LOGGER.info("Deleting user..." + user);

		try {
			userService.deleteUser(user);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);

		} catch (ResourceDatabaseOperationException dbe) {
			LOGGER.log(Level.SEVERE, "Delete DB operation error...", dbe);
			return new ResponseEntity<>("Delete DB operation error...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/displayAll", method = RequestMethod.GET)
	public ResponseEntity<?> getAllUsers() {

		LOGGER.info("Retrieving all users...");

		try {
			Future<List<User>> users = userService.getAllUsers();

			if (CollectionUtils.isEmpty(users.get())) {
				return new ResponseEntity<>("No users were found", HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(new Gson().toJson(users.get()), HttpStatus.OK);

		} catch (ResourceDatabaseOperationException | InterruptedException | ExecutionException dbe) {
			LOGGER.log(Level.SEVERE, "Retrieve all DB operation error...", dbe);
			return new ResponseEntity<>("Retrieve all DB operation error...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public ResponseEntity<?> findUser(@RequestParam(name = "email") String email) {

		LOGGER.info("Retrieving a single user...");

		try {
			Future<User> user = userService.findUser(email);
			if (user == null) {
				return new ResponseEntity<>("User not found for email: " + email, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(user.get(), HttpStatus.OK);

		} catch (ResourceDatabaseOperationException | InterruptedException | ExecutionException dbe) {
			LOGGER.log(Level.SEVERE, "Retrieve DB operation error...", dbe);
			return new ResponseEntity<>("Retrieve DB operation error...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
