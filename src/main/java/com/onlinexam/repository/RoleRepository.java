package com.onlinexam.repository;

import org.springframework.data.repository.CrudRepository;

import com.onlinexam.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role,Long> {
	Role findByname(String name);
}
