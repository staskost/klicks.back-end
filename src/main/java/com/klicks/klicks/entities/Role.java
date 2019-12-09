package com.klicks.klicks.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "role")
	private String name;

	@OneToMany
	@JoinColumn(name = "fk-role-id", referencedColumnName = "id")
	@JsonIgnore
	private List<User> users;

	protected Role() {
	}

	private Role(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
	}

	public Role(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private int id;

		private String name;

		public Builder withId(int id) {
			this.id = id;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Role build() {
			return new Role(this);
		}
	}
}
