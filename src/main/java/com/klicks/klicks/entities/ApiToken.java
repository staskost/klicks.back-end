package com.klicks.klicks.entities;

import java.io.Serializable;

public class ApiToken implements Serializable {

	private static final long serialVersionUID = 1L;

	private String token;

	private UserDTO userDTO;

	private ApiToken() {
	}

	private ApiToken(Builder builder) {
		this.token = builder.token;
		this.userDTO = builder.userDTO;
	}

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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String token;

		private UserDTO userDTO;

		public Builder withToken(String token) {
			this.token = token;
			return this;
		}

		public Builder withUserDTO(UserDTO userDTO) {
			this.userDTO = userDTO;
			return this;
		}

		public ApiToken build() {
			return new ApiToken(this);
		}
	}
}
