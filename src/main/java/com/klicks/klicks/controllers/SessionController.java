package com.klicks.klicks.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klicks.klicks.entities.ExtraGear;
import com.klicks.klicks.entities.StudioSessions;
import com.klicks.klicks.entities.User;
import com.klicks.klicks.repositories.SessionRepository;
import com.klicks.klicks.repositories.UserRepository;
import com.klicks.klicks.validation.Validation;

@RestController
@RequestMapping("/sessions")
@CrossOrigin(origins = "*")
public class SessionController {

	private SessionRepository sessionRepository;

	
	private UserRepository userRepository;

	public SessionController(SessionRepository sessionRepository,UserRepository userRepository) {
		this.sessionRepository = sessionRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("between/{date}/{date2}")
	public List<StudioSessions> findbetween(@PathVariable String date, @PathVariable String date2) {
		return sessionRepository.findByDateBetween(date, date2);
	}

//	@PostMapping("book/{date}/{price}")
//	public void bookSession(@RequestHeader(value = "X-KLICKS-AUTH") String alphanumeric, @PathVariable String date, @PathVariable double price) {
//		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
//		User user = token.getUser();
//		StudioSessions session = new StudioSessions(user, date, price);
//		sessionRepository.save(session);
//	}

	@GetMapping("by-date/{date}/{userId}")
	public StudioSessions getSessionsByDate(@PathVariable String date, @PathVariable int userId) {
		Validation.authorizeUser(userId);
		return sessionRepository.findByDate(date);
	}

	@GetMapping("before/{date}/{userId}")
	public List<StudioSessions> getSessionsBefore(@PathVariable String date, @PathVariable int userId) {
		Validation.authorizeAdmin(userId);
		return sessionRepository.findByDateBefore(date);
	}

	@GetMapping("after/{date}/{userId}")
	public List<StudioSessions> getSessionsAfter(@PathVariable String date, @PathVariable int userId) {
		Validation.authorizeAdmin(userId);
		return sessionRepository.findByDateAfter(date);
	}

	@GetMapping("by-user-before/{userId}")
	public List<StudioSessions> getMySessionsBefore(@PathVariable String date, @PathVariable int userId) {
		Validation.authorizeUser(userId);
		User user = userRepository.findById(userId);
		return sessionRepository.findByUserAndDateBefore(user, date);
	}

	@GetMapping("by-user-after/{userId}")
	public List<StudioSessions> getMySessionsAfter(@PathVariable String date, @PathVariable int userId) {
		Validation.authorizeUser(userId);
		User user = userRepository.findById(userId);
		return sessionRepository.findByUserAndDateAfter(user, date);
	}

	@PostMapping("book2/{date}/{price}/{userId}")
	public void book(@PathVariable String date, @PathVariable double price, @PathVariable int userId,
			@RequestBody List<ExtraGear> extras) {
		Validation.authorizeUser(userId);
		User user = userRepository.findById(userId);;
		StudioSessions session = new StudioSessions(user, date, price);
		double sum = price;
		for (ExtraGear extra : extras) {
			sum += extra.getPrice();
		}
		session.setTotalPrice(sum);
		sessionRepository.save(session);
		for (ExtraGear extra : extras) {
			sessionRepository.addExtraGear(extra.getId(), session.getId());
		}
	}

	@GetMapping("for-user/{userId}")
	public List<StudioSessions> usersSessions(@PathVariable int userId) {
		Validation.authorizeUser(userId);
		User user = userRepository.findById(userId);;
		return sessionRepository.findByUser(user);
	}

	@DeleteMapping("/delete/{sessionId}/{userId}")
	public ResponseEntity deleteMsg(@PathVariable int sessionId, @PathVariable int userId) {
		Validation.authorizeUser(userId);
		StudioSessions session = sessionRepository.findById(sessionId);
		Validation.validateSession(session);
		sessionRepository.delete(session);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

}
