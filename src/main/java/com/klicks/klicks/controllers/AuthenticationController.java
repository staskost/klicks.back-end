package com.klicks.klicks.controllers;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.klicks.klicks.builder.GenericBuilder;
import com.klicks.klicks.convertion.Converter;
import com.klicks.klicks.entities.ApiToken;
import com.klicks.klicks.entities.Login;
import com.klicks.klicks.entities.User;
import com.klicks.klicks.entities.UserDTO;
import com.klicks.klicks.repositories.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class AuthenticationController {

	private static final String SECRET_KEY = "123#&*zcvAWEE999";

	private UserRepository userRepository;

	public AuthenticationController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping("/user")
	public ApiToken loginUser(@RequestBody Login login) {
		String username = login.getUsername();
		String password = login.getPassword();
		User user1 = userRepository.findByUsername(username);
		if (user1 == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username/Password");
		}
		String random = user1.retrieveRandom();
		String hashedPassword = DigestUtils.sha256Hex(password + random);
		User user = userRepository.findByUsernameAndPassword(username, hashedPassword);
		if (user != null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username/Password");
		}
		UserDTO userDTO = Converter.userToUserDTO(user);
		String token = Jwts.builder().setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 864_000_000L))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
		ApiToken apiToken = GenericBuilder.of(ApiToken::new).with(ApiToken::setToken, token)
				.with(ApiToken::setUserDTO, userDTO).build();
		;
		return apiToken;
	}
}
