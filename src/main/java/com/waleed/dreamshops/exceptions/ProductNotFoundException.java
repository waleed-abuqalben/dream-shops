package com.waleed.dreamshops.exceptions;

public class ProductNotFoundException extends RuntimeException{
	public ProductNotFoundException(String message) {
		super(message);
	}

}
