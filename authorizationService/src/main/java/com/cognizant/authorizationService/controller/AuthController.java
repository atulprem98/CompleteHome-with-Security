package com.cognizant.authorizationService.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.authorizationService.model.AuthResponse;
import com.cognizant.authorizationService.model.UserData;
import com.cognizant.authorizationService.service.CustomerDetailsService;
import com.cognizant.authorizationService.service.JwtUtil;

@RestController
public class AuthController {

	@Autowired
	private JwtUtil jwtutil;
	@Autowired
	private CustomerDetailsService custdetailservice;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserData userlogincredentials) {
		// Generates token for login
		final UserDetails userdetails = custdetailservice.loadUserByUsername(userlogincredentials.getUserid());
		String uid = "";
		String generateToken = "";
		if (userdetails.getPassword().equals(userlogincredentials.getUpassword())) {
			uid = userlogincredentials.getUserid();
			generateToken = jwtutil.generateToken(userdetails);
			return new ResponseEntity<>(new UserData(uid, null, null, generateToken), HttpStatus.OK);
		} else {
			LOGGER.info("At Login : ");
			LOGGER.error("Not Accesible");
			return new ResponseEntity<>("Not Accesible", HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/validate")
	public ResponseEntity<?> getValidity(@RequestHeader("Authorization") String token) {
		// Returns response after Validating received token
		AuthResponse res = new AuthResponse();
		if (token == null) {
			res.setValid(false);
			return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
		} else {
			String token1 = token.substring(7);
			if (jwtutil.validateToken(token1)) {
				res.setUid(jwtutil.extractUsername(token1));
				res.setValid(true);
				res.setName("admin");
			} else {
				res.setValid(false);
				LOGGER.info("At Validity : ");
				LOGGER.error("Token Has Expired");
				return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);

			}
		}
		return new ResponseEntity<>(res, HttpStatus.OK);

	}

}
