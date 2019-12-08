package com.klicks.klicks.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.klicks.klicks.entities.ExtraGear;
import com.klicks.klicks.entities.StandartGear;
import com.klicks.klicks.entities.StudioSessions;
import com.klicks.klicks.entities.User;
import com.klicks.klicks.repositories.UserRepository;

public class Validation {

	private static UserRepository userRepository;

	public Validation(UserRepository userRepository) {
		super();
		Validation.userRepository = userRepository;
	}

	public static void authorizeUser(int userId) {
		User user = userRepository.findById(userId);
		if ((user.getRole().getId() != 1 || user == null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not Authorized");
		}
	}

	public static void authorizeAdmin(int userId) {
		User user = userRepository.findById(userId);
		if ((user.getRole().getId() != 2 || user == null)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not Authorized");
		}
	}

	public static void validateUser(User user) {
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
		}
	}

	public static void validateStandartgear(StandartGear gear) {
		if (gear == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gear Not Found");
		}
	}

	public static void validateExtragear(ExtraGear gear) {
		if (gear == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gear Not Found");
		}
	}

	public static void validateSession(StudioSessions session) {
		if (session == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session Not Found");
		}
	}

	public static void validatePageAndSize(int page, int size) {
		if ((page < 0) || (size < 0) || (size > 50)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pagination request");
		}
	}

}
