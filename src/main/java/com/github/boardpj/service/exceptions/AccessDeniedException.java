package com.github.boardpj.service.exceptions;

public class AccessDeniedException extends RuntimeException {
	public AccessDeniedException(String message) {
		super(message);
	}
}
