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
import com.klicks.klicks.entities.User;
import com.klicks.klicks.repositories.ExtraGearRepository;
import com.klicks.klicks.repositories.StandartGearRepository;
import com.klicks.klicks.repositories.UserRepository;
import com.klicks.klicks.validation.Validation;

@RestController
@RequestMapping("/gear")
@CrossOrigin(origins = "*")
public class GearController {

	private ExtraGearRepository extraGearRepository;

	private StandartGearRepository standartGearRepository;

	public GearController(ExtraGearRepository extraGearRepository, StandartGearRepository standartGearRepository) {
		this.extraGearRepository = extraGearRepository;
		this.standartGearRepository = standartGearRepository;
	}

	@GetMapping("all-standart")
	public List<StandartGear> getAllstandartGear() {
		return standartGearRepository.findAll();
	}

	@GetMapping("all-extra")
	public List<ExtraGear> getAllExtraGear() {
		return extraGearRepository.findAll();
	}

	@GetMapping("by-session/{sessionId}/{userId}")
	public List<ExtraGear> getGearBySession(@PathVariable int sessionId, @PathVariable int userId) {
		Validation.authorizeUser(userId);
		return extraGearRepository.findGearBySessions(sessionId);

	}

	@DeleteMapping("delete-standart/{gearId}/{userId}")
	public ResponseEntity reamoveStandartGear(@PathVariable int gearId, @PathVariable int userId) {
		Validation.authorizeUser(userId);
		StandartGear gear = standartGearRepository.findById(gearId);
		standartGearRepository.delete(gear);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@DeleteMapping("delete-extra/{gearId}/{userId}")
	public ResponseEntity reamoveExtratGear(@PathVariable int gearId, @PathVariable int userId) {
		Validation.authorizeUser(userId);
		ExtraGear gear = extraGearRepository.findById(gearId);
		extraGearRepository.delete(gear);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}

	@PostMapping("add-extra/{name}/{price}/{userId}")
	public ExtraGear addExtraGear(@PathVariable String name, @PathVariable double price, @PathVariable int userId,
			@RequestBody String desc) {
		Validation.authorizeUser(userId);
		ExtraGear gear = ExtraGear.builder().withPrice(price).withName(name).withDescription(desc).build();
		extraGearRepository.save(gear);
		ExtraGear newGear = extraGearRepository.findById(gear.getId());
		return newGear;
	}

	@PostMapping("add-standart/{name}/{userId}")
	public StandartGear addStandartGear(@PathVariable String name, @PathVariable int userId, @RequestBody String desc) {
		Validation.authorizeUser(userId);
		StandartGear gear = StandartGear.builder().withName(name).withDescription(desc).build();
		standartGearRepository.save(gear);
		StandartGear newGear = standartGearRepository.findById(gear.getId());
		return newGear;
	}

}
