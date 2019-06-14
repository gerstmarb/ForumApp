package com.forum.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.forum.entity.Post;
import com.forum.repository.PostRepository;

@Service
public class PostServiceImpl implements PostService {

	PostRepository postRepository;

	UserService userService;

	@Autowired
	public PostServiceImpl(PostRepository postRepository, UserService userService) {
		this.postRepository = postRepository;
		this.userService = userService;
	}

	@Override
	public List<Post> getPosts() {
		return postRepository.findAll();
	}

	@Override
	public void addPost(Post postToAdd) {
		postToAdd.setUser(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
		postToAdd.setPosted(new Date());
		postRepository.save(postToAdd);
	}

	@Override
	public Post getPost(Long id) {
		return postRepository.findById(id).get();
	}

	@Override
	public List<Post> findAllByTag(String tag) {
		return postRepository.findAllByTag(tag);
	}
}
