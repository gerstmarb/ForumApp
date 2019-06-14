package com.forum.service;

import java.util.List;

import com.forum.entity.Post;

public interface PostService  {

	public List<Post> getPosts();
	
	public void addPost(Post post);
	
	public Post getPost(Long id);
	
	public List<Post> findAllByTag(String tag);
	
}
