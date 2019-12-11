package com.klicks.klicks.convertion;

import java.util.ArrayList;
import java.util.List;

import com.klicks.klicks.entities.User;
import com.klicks.klicks.entities.UserDTO;

public class Converter {

	public static UserDTO userToUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setUserName(user.getUsername());
		userDTO.setEmail(user.getEmail());
		return userDTO;

	};

	public static List<UserDTO> usersToUsersDTO(List<User> users) {
		List<UserDTO> usersDTO = new ArrayList<UserDTO>();
		for (User user : users) {
			UserDTO userDTO = userToUserDTO(user);
			usersDTO.add(userDTO);
		}
		return usersDTO;
	}

}
