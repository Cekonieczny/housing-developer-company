package com.capgemini.domain;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class TimestampListener {

	@PrePersist
	protected void onCreate(AbstractEntity entity) {
			entity.setCreatedOn(new Date());
			entity.setUpdatedOn(entity.getCreatedOn());
	}

	@PreUpdate
    protected void onUpdate(AbstractEntity entity) {
            entity.setUpdatedOn(new Date());
	}
}