package com.capgemini.embeddable;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false, length = 30)
	private String firstName;
	@Column(nullable = false, length = 30)
	private String lastName;
	
	//for hibernate
	public Name() {
	}
	
	public Name(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Name name = (Name) obj;
		return	Objects.equals(firstName, name.firstName) 
				&& Objects.equals(lastName,name.lastName);
	}
}
