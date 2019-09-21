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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klicks.klicks.entities.ExtraGear;
import com.klicks.klicks.entities.StandartGear;
import com.klicks.klicks.entities.Token;
import com.klicks.klicks.repositories.ExtraGearRepository;
import com.klicks.klicks.repositories.StandartGearRepository;
import com.klicks.klicks.repositories.TokenRepository;
import com.klicks.klicks.validation.Validation;

@RestController
@RequestMapping("/gear")
@CrossOrigin(origins = "*")
public class GearController {

	private ExtraGearRepository extraGearRepository;

	private StandartGearRepository standartGearRepository;

	private TokenRepository tokenRepository;

	public GearController(ExtraGearRepository extraGearRepository, StandartGearRepository standartGearRepository,
			TokenRepository tokenRepository) {
		this.extraGearRepository = extraGearRepository;
		this.standartGearRepository = standartGearRepository;
		this.tokenRepository = tokenRepository;
	}

	@GetMapping("all-standart")
	public List<StandartGear> getAllstandartGear() {
		return standartGearRepository.findAll();
	}

	@GetMapping("all-extra")
	public List<ExtraGear> getAllExtraGear() {
		return extraGearRepository.findAll();
	}

	@GetMapping("by-session/{sessionId}")
	public List<ExtraGear> getGearBySession(@RequestHeader(value = "X-KLICKS-AUTH") String alphanumeric,
			@PathVariable int sessionId) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validation.validateToken(token);
		return extraGearRepository.findGearBySessions(sessionId);

	}

	@DeleteMapping("delete-standart/{gearId}")
	public ResponseEntity reamoveStandartGear(@RequestHeader(value = "X-KLICKS-AUTH") String alphanumeric,
			@PathVariable int gearId) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validation.validateToken(token);
		StandartGear gear = standartGearRepository.findById(gearId);
		standartGearRepository.delete(gear);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@DeleteMapping("delete-extra/{gearId}")
	public ResponseEntity reamoveExtratGear(@RequestHeader(value = "X-KLICKS-AUTH") String alphanumeric,
			@PathVariable int gearId) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validation.validateToken(token);
		ExtraGear gear = extraGearRepository.findById(gearId);
		extraGearRepository.delete(gear);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}

	@PostMapping("add-extra/{name}/{price}")
	public ExtraGear addExtraGear(@RequestHeader(value = "X-KLICKS-AUTH") String alphanumeric,
			@PathVariable String name, @PathVariable double price, @RequestBody String desc) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validation.validateToken(token);
		ExtraGear gear = new ExtraGear(price, desc, name);
		extraGearRepository.save(gear);
		ExtraGear newGear = extraGearRepository.findById(gear.getId());
		return newGear;
	}

	@PostMapping("add-standart/{name}")
	public StandartGear addStandartGear(@RequestHeader(value = "X-KLICKS-AUTH") String alphanumeric,
			@PathVariable String name, @RequestBody String desc) {
		Token token = tokenRepository.findByAlphanumeric(alphanumeric);
		Validation.validateToken(token);
		StandartGear gear = new StandartGear(name, desc);
		standartGearRepository.save(gear);
		StandartGear newGear = standartGearRepository.findById(gear.getId());
		return newGear;
	}

}
