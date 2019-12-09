package com.klicks.klicks.entities;

import java.io.Serializable;

public class ApiToken implements Serializable {

	private static final long serialVersionUID = 1L;

	private String token;

	private UserDTO userDTO;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

}
