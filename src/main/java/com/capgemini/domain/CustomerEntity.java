package com.capgemini.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.capgemini.embeddable.Address;
import com.capgemini.embeddable.Name;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners(TimestampListener.class)
@Table(name = "CUSTOMER")
public class CustomerEntity extends AbstractEntity  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Embedded
	private Name name;

	@Column(nullable = false, length = 60)
	private Address address;
	
	@Column(nullable = false, length = 60)
	private String email;
	
	@Column(nullable = false)
	private Date birthDate;
	
	@Column(length = 16)
	private String creditCardNumber;
	
	@Column(length = 12)
	private String phoneNumber;
	
	@ManyToMany(mappedBy = "customerEntities",fetch = FetchType.LAZY)
	private Set<FlatEntity> flatEntities = new HashSet<>();
	
	@OneToMany(mappedBy = "owner",fetch = FetchType.LAZY)
	private Set<OrderEntity> ownedFlats = new HashSet<>();
	
	@ManyToMany(mappedBy = "coOwners",fetch = FetchType.LAZY)
	private Set<OrderEntity> coOwnedFlats = new HashSet<>();

	// for hibernate
	public CustomerEntity() {
		super();
	}
	
	
	public CustomerEntity(Long id, Name name, Address address, String email, Date birthDate,
			String creditCardNumber, String phoneNumber, Set<FlatEntity> flatEntities) {
		super(id);
		this.name = name;
		this.address = address;
		this.email = email;
		this.birthDate = birthDate;
		this.creditCardNumber = creditCardNumber;
		this.phoneNumber = phoneNumber;
		this.flatEntities = flatEntities;
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

	public Set<FlatEntity> getFlatEntities() {
		return flatEntities;
	}

	public void setFlatEntities(Set<FlatEntity> flatEntities) {
		this.flatEntities = flatEntities;
	}
	
	public void addFlatEntity(FlatEntity flatEntity) {
		this.flatEntities.add(flatEntity);
		flatEntity.getCustomerEntities().add(this);
	}

	public void removeFlatEntity(FlatEntity flatEntity) {
		this.flatEntities.remove(flatEntity);
		flatEntity.getCustomerEntities().remove(this);
}
	
	
}
