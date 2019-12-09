package com.klicks.klicks.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

	private UserRepository userRepository;

	public AuthenticationController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping("/user")
	public ApiToken loginUser(@RequestBody Login login) {
		String username = login.getUsername();
		String password = login.getPassword();
		User user1 = userRepository.findByUsername(username);
		String random = user1.retrieveRandom();
		String sha256hex = DigestUtils.sha256Hex(password + random);
		User user = userRepository.findByUsernameAndPassword(username, sha256hex);
		if (user != null) {
			UserDTO userDTO = Converter.userToUserDTO(user);
			String token = Jwts.builder().setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + 864_000_000L))
					.signWith(SignatureAlgorithm.HS256, "123#&*zcvAWEE999").compact();
			ApiToken apiToken = ApiToken.builder().withToken(token).withUserDTO(userDTO).build();
			return apiToken;
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username/Password");
		}
	}

}
