package com.forum.repository;

import org.springframework.data.repository.CrudRepository;

import com.forum.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);

	User findByActivation(String code);
}
