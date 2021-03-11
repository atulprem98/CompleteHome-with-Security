package com.cognizant.authorizationService.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Service class */
@Service
public class CustomerDetailsService implements UserDetailsService {

	/**
	 * @param String
	 * @return User
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String uid) {

		try {

			return new User("admin", "pwd", new ArrayList<>());
		} catch (Exception e) {
			throw new UsernameNotFoundException("UsernameNotFoundException");
		}

	}

}
