package com.capgemini.types;

import java.util.Date;

public abstract class AbstractTO {

	private Long id;
	private Date createdOn;
	private Date updatedOn;

	public AbstractTO(Long id, Date createdOn, Date updatedOn) {
		this.id = id;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updated) {
		this.updatedOn = updated;
	}

	protected static class AbstractTOBuilder <T extends AbstractTOBuilder<T>> {
		
		private T extendingTOBuilder;
		protected Long id;
		protected Date createdOn;
		protected Date updatedOn;
		
		AbstractTOBuilder(){		
		}
		
		public T withId(Long id) {
			this.id = id;
			return extendingTOBuilder;
		}

		public T withCreatedOn(Date createdOn) {
			this.createdOn=createdOn;
			return extendingTOBuilder;
		}

		public T withUpdatedOn(Date updatedOn) {
			this.updatedOn=updatedOn;
			return extendingTOBuilder;
		}
		
		protected  void setExtendingTOBuilder(T extendingTOBuilder){
			this.extendingTOBuilder = extendingTOBuilder;
		}	
	}
}
