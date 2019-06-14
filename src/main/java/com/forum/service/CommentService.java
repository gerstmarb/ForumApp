package com.forum.service;

import java.util.List;

import com.forum.entity.Comment;

public interface CommentService {

	public List<Comment> findByPost(Long postId);

	public void addComment(Comment comment);
}
