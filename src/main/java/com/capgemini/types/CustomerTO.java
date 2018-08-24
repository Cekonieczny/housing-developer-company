package com.capgemini.types;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.capgemini.embeddable.Address;
import com.capgemini.embeddable.Name;

public class CustomerTO extends AbstractTO {

	private Name name;
	private Address address;
	private String email;
	private Date birthDate;
	private String creditCardNumber;
	private String phoneNumber;
	private Set<Long> flatIds = new HashSet<>();

	public CustomerTO(Long id, Date created, Date updated, Name name, Address address, String email, Date birthDate,
			String creditCardNumber, String phoneNumber, Set<Long> flatIds) {
		super(id, created, updated);
		this.name = name;
		this.address = address;
		this.email = email;
		this.birthDate = birthDate;
		this.creditCardNumber = creditCardNumber;
		this.phoneNumber = phoneNumber;
		this.flatIds = flatIds;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getCreatedOn(), getUpdatedOn(), name, address, email, birthDate, creditCardNumber,
				phoneNumber, flatIds);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerTO customerTO = (CustomerTO) obj;
		return Objects.equals(getId(), customerTO.getId()) 
				&& Objects.equals(getCreatedOn(), customerTO.getCreatedOn())
				&& Objects.equals(getUpdatedOn(), customerTO.getUpdatedOn()) 
				&& Objects.equals(name, customerTO.name)
				&& Objects.equals(address, customerTO.address)
				&& Objects.equals(email, customerTO.email)
				&& Objects.equals(birthDate, customerTO.birthDate)
				&& Objects.equals(creditCardNumber, customerTO.creditCardNumber)
				&& Objects.equals(phoneNumber, customerTO.phoneNumber)
				&& Objects.equals(flatIds, customerTO.flatIds);
	}

	public Set<Long> getFlatIds() {
		return flatIds;
	}

	public void setFlatIds(Set<Long> flatIds) {
		this.flatIds = flatIds;
	}

	public static class CustomerTOBuilder extends AbstractTOBuilder<CustomerTOBuilder> {

		private Name name;
		private Address address;
		private String email;
		private Date birthDate;
		private String creditCardNumber;
		private String phoneNumber;
		private Set<Long> flatIds = new HashSet<>();

		public CustomerTOBuilder() {
			super();
			super.setExtendingTOBuilder(this);
		}

		public CustomerTOBuilder withName(Name name) {
			this.name = name;
			return this;
		}

		public CustomerTOBuilder withAddress(Address address) {
			this.address = address;
			return this;
		}

		public CustomerTOBuilder withEmail(String email) {
			this.email = email;
			return this;
		}

		public CustomerTOBuilder withBirthDate(Date birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		public CustomerTOBuilder withCreditCardNumber(String creditCardNumber) {
			this.creditCardNumber = creditCardNumber;
			return this;
		}

		public CustomerTOBuilder withPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		public CustomerTOBuilder withFlatIds(Set<Long> flatIds) {
			this.flatIds = flatIds;
			return this;
		}

		public CustomerTO build() {
			checkBeforeBuild(name, address, email, birthDate, creditCardNumber, phoneNumber);
			return new CustomerTO(id, createdOn, updatedOn, name, address, email, birthDate, creditCardNumber,
					phoneNumber, flatIds);
		}

		private void checkBeforeBuild(Name name, Address address, String email, Date birthDate, String creditCardNumber,
				String phoneNumber) {
			if (name == null || address == null || email == null || email.isEmpty() || creditCardNumber == null
					|| creditCardNumber.isEmpty() || phoneNumber == null || phoneNumber.isEmpty()
					|| birthDate == null) {
				throw new RuntimeException("Incorrect Customer transfer object to be created");
			}

		}

	}

}
