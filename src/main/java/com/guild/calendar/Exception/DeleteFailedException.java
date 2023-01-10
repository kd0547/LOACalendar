package com.guild.calendar.Exception;

public class DeleteFailedException extends RuntimeException{
	
	public DeleteFailedException() {}
	public DeleteFailedException(String message) {
		super(message);
	}
}
