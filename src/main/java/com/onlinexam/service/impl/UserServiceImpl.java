package com.onlinexam.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinexam.domain.User;
import com.onlinexam.domain.security.UserRole;
import com.onlinexam.repository.RoleRepository;
import com.onlinexam.repository.UserRepository;
import com.onlinexam.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepostiory;
	
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	@Override
	public User findByEmail (String email) {
		return userRepository.findByEmail(email);
	}
	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception{
		User localUser = userRepository.findByEmail(user.getEmail());
		if(localUser !=null) {
			throw new Exception("user already exists");
		}
		else {
			for(UserRole ur: userRoles) {
				roleRepostiory.save(ur.getRole());
			}
			user.getUserRoles().addAll(userRoles);
			localUser = userRepository.save(user);
		}
		return localUser;
	}

}