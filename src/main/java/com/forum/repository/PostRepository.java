package com.forum.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.forum.entity.Post;

public interface PostRepository extends CrudRepository<Post, Long> {

	List<Post> findAll();

	List<Post> findAllByTag(String tag);
}
