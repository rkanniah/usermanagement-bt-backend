package com.rk.usermanagement.controller;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import com.rk.usermanagement.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	private void addUser() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		User user = new User() {
			{
				setEmail("dagon@opec.com");
				setName("Dagon");
				setRole(Arrays.asList("user", "superuser"));
			}
		};

		HttpEntity<User> request = new HttpEntity<>(user, headers);
		ResponseEntity<String> response = testRestTemplate.postForEntity("http://localhost:" + port + "/add", request,
				String.class);

		then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	private void findUserAfterAddUser() throws Exception {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/find")
				.queryParam("email", "dagon@opec.com");

		ResponseEntity<User> response = testRestTemplate.getForEntity(builder.toUriString(), User.class);

		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assert.assertTrue(response.getBody().getName().equals("Dagon"));
		Assert.assertFalse(String.join(",", response.getBody().getRole()).equals("user,network"));
		Assert.assertTrue(String.join(",", response.getBody().getRole()).equals("user,superuser"));
	}

	private void updateUser() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		User user = new User() {
			{
				setId(4L);
				setEmail("dagon@opec.com");
				setName("Dagon");
				setRole(Arrays.asList("oss", "superuser"));
			}
		};

		HttpEntity<User> request = new HttpEntity<>(user, headers);
		ResponseEntity<User> response = testRestTemplate.exchange("http://localhost:" + port + "/update",
				HttpMethod.PUT, request, User.class);

		then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		Assert.assertFalse(String.join(",", response.getBody().getRole()).equals("user,superuser"));
		Assert.assertTrue(String.join(",", response.getBody().getRole()).equals("oss,superuser"));
	}

	private void findUserAfterUpdateUser() throws Exception {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/find")
				.queryParam("email", "dagon@opec.com");

		ResponseEntity<User> response = testRestTemplate.getForEntity(builder.toUriString(), User.class);

		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assert.assertTrue(response.getBody().getName().equals("Dagon"));
		Assert.assertTrue(String.join(",", response.getBody().getRole()).equals("oss,superuser"));
	}

	private void deleteUser() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		User user = new User() {
			{
				setId(4L);
				setEmail("dagon@opec.com");
				setName("Dagon");
				setRole(Arrays.asList("oss", "superuser"));
			}
		};

		HttpEntity<User> request = new HttpEntity<>(user, headers);
		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + port + "/delete",
				HttpMethod.DELETE, request, String.class);

		then(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
	}

	private void findUserAfterDeleteUser() throws Exception {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/find")
				.queryParam("email", "dagon@opec.com");

		ResponseEntity<String> response = testRestTemplate.getForEntity(builder.toUriString(), String.class);

		then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	private void getAllUsers() throws Exception {

		@SuppressWarnings("rawtypes")
		ResponseEntity<List> response = testRestTemplate.getForEntity("http://localhost:" + port + "/displayAll",
				List.class);

		then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assert.assertFalse(response.getBody().isEmpty());
	}

	@Test
	public void mainTest() throws Exception {

		addUser();
		findUserAfterAddUser();
		updateUser();
		findUserAfterUpdateUser();
		deleteUser();
		getAllUsers();
	}
}
