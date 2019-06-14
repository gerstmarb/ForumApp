package com.forum.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.forum.entity.Comment;
import com.forum.repository.CommentRepository;
import com.forum.repository.PostRepository;

@Service
public class CommentServiceImpl implements CommentService {

	CommentRepository commentRepository;

	UserService userService;

	PostRepository postRepository;

	@Autowired
	public CommentServiceImpl(CommentRepository commentRepository, UserService userService,
			PostRepository postRepository) {
		this.commentRepository = commentRepository;
		this.userService = userService;
		this.postRepository = postRepository;
	}

	@Override
	public List<Comment> findByPost(Long postId) {
		return postRepository.findById(postId).get().getComment();
	}

	@Override
	public void addComment(Comment comment) {
		comment.setUser(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
		comment.setPosted(new Date());
		commentRepository.save(comment);

	}
}
