package com.cognizant.authorizationService.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Model class for the business details */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
	/**
	 * Response message
	 */
	String message;
	/**
	 * Response date
	 */
	LocalDateTime date;

	public MessageResponse(String message, LocalDateTime date) {
		super();
		this.message = message;
		this.date = date;
	}

}
