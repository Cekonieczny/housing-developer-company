package com.capgemini.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners(TimestampListener.class)
@Table(name = "ORDER")
public class OrderEntity {
	
	@OneToOne
	private FlatEntity flatOrdered;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private CustomerEntity owner;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<CustomerEntity> subOwners = new HashSet<>();
	
	
	

}
