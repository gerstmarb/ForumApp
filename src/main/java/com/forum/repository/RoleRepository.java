package com.forum.repository;

import org.springframework.data.repository.CrudRepository;

import com.forum.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByRole(String role);
}
