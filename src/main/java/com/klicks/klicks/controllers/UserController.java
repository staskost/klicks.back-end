package com.klicks.klicks.controllers;

import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.klicks.klicks.database.DatabaseHelper;
import com.klicks.klicks.entities.Result;
import com.klicks.klicks.entities.Role;
import com.klicks.klicks.entities.User;
import com.klicks.klicks.repositories.UserRepository;
import com.klicks.klicks.validation.Validation;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

	private UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@GetMapping("all/{userId}")
	public Result<User> getAllUsers(@PathVariable int userId, @RequestParam int page, @RequestParam int size) {
		Validation.authorizeAdmin(userId);
		Validation.validatePageAndSize(page, size);
		Role role = new Role(1, "USER");
		int count = DatabaseHelper.getSimpleUsersCount();
		List<User> users = userRepository.findByRole(role, PageRequest.of(page, size));
		return new Result<User>(count, users);
	}

	@GetMapping("/{userId}")
	public User getUserById(@PathVariable int userId) {
		Validation.authorizeAdmin(userId);
		User user = userRepository.findById(userId);
		Validation.validateUser(user);
		return user;

	}

	@PostMapping("/user")
	public void registerUser(@RequestBody User user) {
		User user2 = userRepository.findByUsername(user.getUsername());
		User user3 = userRepository.findByEmail(user.getEmail());
		if ((user2 == null) && (user3 == null)) {
			String password = user.retrievePassword();
			String random = UUID.randomUUID().toString();
			user.setRandom(random);
			String sha256hex = DigestUtils.sha256Hex(password + random);
			user.setPassword(sha256hex);
			userRepository.save(user);
		} else if ((user2 != null) && (user3 == null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username Already Exists");
		} else if ((user2 == null) && (user3 != null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already Exists");
		} else if ((user2 != null) && (user3 != null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and Username Already Exist");
		}

	}

	@PutMapping("/update/{userId}")
	public void updateUser(@PathVariable int userId, @RequestBody User user) {
		User user2 = userRepository.findById(userId);
		Validation.validateUser(user2);
		User user3 = userRepository.findByEmail(user.getEmail());
		if (user3 == null) {
			if (user2.retrievePassword().equals(user.retrievePassword())) {
				userRepository.save(user);
			} else {
				String password = user.retrievePassword();
				String random = user2.retrieveRandom();
				user.setRandom(random);
				String sha256hex = DigestUtils.sha256Hex(password + random);
				user.setPassword(sha256hex);
				userRepository.save(user);

			}
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already Exists");
		}
	}

	@DeleteMapping("delete/{adminId}/{userId}")
	public ResponseEntity deleteUser(@PathVariable int adminId, @PathVariable int userId) {
		Validation.authorizeAdmin(adminId);
		User user = userRepository.findById(userId);
		Validation.validateUser(user);
		userRepository.delete(user);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}

}
