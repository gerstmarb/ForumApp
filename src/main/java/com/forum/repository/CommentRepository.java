package com.forum.repository;

import org.springframework.data.repository.CrudRepository;

import com.forum.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
