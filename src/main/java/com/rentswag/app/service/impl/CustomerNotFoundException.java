package com.rentswag.app.service.impl;

@SuppressWarnings("serial")
public class CustomerNotFoundException extends Exception {
	public CustomerNotFoundException(String message) {
        super(message);
    }

}
