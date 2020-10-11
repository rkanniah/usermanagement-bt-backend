package com.rk.usermanagement.controller;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.rk.usermanagement.model.User;
import com.rk.usermanagement.repository.UserRepository;
import com.rk.usermanagement.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTestMock {

	private static final Logger LOGGER = Logger.getLogger(UserControllerTestMock.class.getName());

	private static final String MEDIA_CHARSET = "application/json;charset=utf-8";
	private static final String CONTENT_TYPE = "Content-Type";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@SpyBean
	private UserService userService;

	@Test
	public void getAllUsers() throws Exception {

		Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<User>());

		Mockito.when(userService.getAllUsers().get()).thenReturn(new ArrayList<User>());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/displayAll")).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("No users were found")));
	}

	@Test
	public void findUser() throws Exception {

		User user = new User();
		user.setId(1L);
		user.setName("sam");
		user.setEmail("sam@opec.com");

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add(CONTENT_TYPE, MEDIA_CHARSET);

		Mockito.when(userService.findUser("sam@opec.com").get()).thenReturn(user);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/find").headers(requestHeaders).param("email", "sam@opec.com"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("sam")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.role", Matchers.is(Lists.emptyList())));
	}

	@Test
	public void addUser() throws Exception {

		User user = new User();
		user.setId(1L);
		user.setName("Dagon");
		user.setEmail("dagon@opec.com");
		ArrayList<String> roles = new ArrayList<>();
		roles.add("user");
		roles.add("superuser");

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add(CONTENT_TYPE, MEDIA_CHARSET);

		Mockito.when(userService.addUser(user).get()).thenReturn(user);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/add").headers(requestHeaders)
						.content("{\"email\":\"dagon@opec.com\",\"name\":\"Dagon\",\"role\":[\"user\",\"superuser\"]}"))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	public void updateUser() throws Exception {

		User user = new User();
		user.setId(1L);
		user.setName("Dagon");
		user.setEmail("dagon@opec.com");
		ArrayList<String> roles = new ArrayList<>();
		roles.add("user");
		roles.add("operations");

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add(CONTENT_TYPE, MEDIA_CHARSET);

		Mockito.when(userRepository.findByEmail("dagon@opec.com")).thenReturn(user);

		Mockito.when(userService.updateUser(user).get()).thenReturn(user);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/update").headers(requestHeaders).content(
				"{\"id\":1,\"email\":\"dagon@opec.com\",\"name\":\"Dagon\",\"role\":[\"user\",\"operations\"]}"))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isAccepted());
	}

	@Test
	public void deleteUser() throws Exception {

		User user = new User();
		user.setId(1L);
		user.setName("Dagon");
		user.setEmail("dagon@opec.com");
		ArrayList<String> roles = new ArrayList<>();
		roles.add("user");
		roles.add("operations");

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add(CONTENT_TYPE, MEDIA_CHARSET);

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/delete").headers(requestHeaders).content(
				"{\"id\":1,\"email\":\"dagon@opec.com\",\"name\":\"Dagon\",\"role\":[\"user\",\"operations\"]}"))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isAccepted());
	}
}
