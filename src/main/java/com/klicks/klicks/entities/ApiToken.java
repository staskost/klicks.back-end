package com.klicks.klicks.entities;

import java.io.Serializable;

public class ApiToken implements Serializable {

	private static final long serialVersionUID = 1L;

	private String token;

	private ApiToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
