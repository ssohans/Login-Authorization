package com.onlinexam.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.onlinexam.domain.User;
import com.onlinexam.repository.UserRepository;

@Service
public class UserSecurityService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String loginmail) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(loginmail);
		
		if(null == user) {
			throw new UsernameNotFoundException("Email not found");
		}
		
		return user;
	}

}
