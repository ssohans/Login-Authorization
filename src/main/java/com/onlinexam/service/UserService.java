package com.onlinexam.service;

import java.util.Set;

import com.onlinexam.domain.User;
import com.onlinexam.domain.security.UserRole;

public interface UserService {
	
	User findByUsername(String username);
	
	User findByEmail (String email);

	User createUser(User user, Set<UserRole> userRoles) throws Exception;
}