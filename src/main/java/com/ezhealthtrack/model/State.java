package com.ezhealthtrack.model;


public class State {

	int id;
	String name;

	// constructors
	public State() {

	}

	public State(String name) {
		this.name = name;
	}

	public State(int id, String name) {
		this.id = id;
		this.name = name;
	}

	// setter
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	// getter
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
}
