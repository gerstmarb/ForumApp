package com.forum.service;

import java.util.List;

import com.forum.entity.User;

public interface UserService {

	public void registerUser(User user);
	
	public User findByUsername(String username);

	public void userActivation(String code);
	
	public User getCurrentUser();
	
	public List<User> findAllUsers();
	
	public void editUserEnable(Long id);
	
}
