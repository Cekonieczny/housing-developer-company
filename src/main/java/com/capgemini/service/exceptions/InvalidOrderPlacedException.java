package com.capgemini.service.exceptions;

public class InvalidOrderPlacedException extends RuntimeException {

	private static final long serialVersionUID = -2113221962604044421L;

	public InvalidOrderPlacedException() {
		super("Requested order cannot be placed");
	}

	public InvalidOrderPlacedException(String message) {
		super("Requested order cannot be placed :" + message);
	}
}
