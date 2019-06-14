package com.forum.service;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.forum.entity.Role;
import com.forum.entity.User;
import com.forum.repository.RoleRepository;
import com.forum.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private UserRepository userRepository;

	private RoleRepository roleRepository;

	private EmailService emailService;

	private BCryptPasswordEncoder passwordEncoder;

	private final String USER_ROLE = "USER";

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService,
			BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.emailService = emailService;
		this.passwordEncoder = passwordEncoder;

	}

	@Override
	public List<User> findAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailsImpl(user);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public void registerUser(User userToRegister) {
		Role userRole = roleRepository.findByRole(USER_ROLE);
		if (userRole != null) {
			userToRegister.getRoles().add(userRole);
		} else {
			userToRegister.addRoles(USER_ROLE);
		}
		String key = generateKey();
		userToRegister.setPassword(passwordEncoder.encode(userToRegister.getPassword()));
		userToRegister.setEnabled(false);
		userToRegister.setActivation(key);
		userRepository.save(userToRegister);
		emailService.sendMessage(userToRegister.getEmail(), userToRegister.getUsername(), key);
	}

	@Override
	public User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		return findByUsername(username);
	}

	private String generateKey() {
		Random rand = new Random();
		char[] word = new char[16];
		for (int j = 0; j < word.length; j++) {
			word[j] = (char) ('a' + rand.nextInt(26));
		}
		String toReturn = new String(word);
		logger.debug("the generated random: " + toReturn);
		return toReturn;
	}

	@Override
	public void userActivation(String code) throws UsernameNotFoundException {
		User user = userRepository.findByActivation(code);
		
		user.setEnabled(true);
		user.setActivation("");
		userRepository.save(user);
		
	}

	@Override
	public void editUserEnable(Long id) {
		User u = userRepository.findById(id).get();
		if (u.getEnabled() == false) {
			u.setEnabled(true);
		} else {
			u.setEnabled(false);
		}
		userRepository.save(u);

	}

}
