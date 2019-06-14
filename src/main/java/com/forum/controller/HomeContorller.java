package com.forum.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.forum.entity.Comment;
import com.forum.entity.Post;
import com.forum.entity.User;
import com.forum.service.CommentService;
import com.forum.service.PostService;
import com.forum.service.UserService;

@Controller
public class HomeContorller {

	private UserService userService;

	private PostService postService;

	private CommentService commentService;

	@Autowired
	public HomeContorller(UserService userService, PostService postService, CommentService commentService) {
		this.userService = userService;
		this.postService = postService;
		this.commentService = commentService;
	}

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("posts", postService.getPosts());
		return "index";
	}

	@GetMapping(path = "/tag/{givenTag}")
	public String topic(@PathVariable("givenTag") String tag, Model model) {
		model.addAttribute("posts", postService.findAllByTag(tag));
		return "tag";
	}

	@GetMapping("/registration")
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", new User());
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@RequestMapping("/newpost")
	public String newPost(Model model) {
		model.addAttribute("post", new Post());
		return "newPost";
	}

	@RequestMapping("/users")
	public String users(Model model) {
		model.addAttribute("users", userService.findAllUsers());
		return "users";
	}

	@GetMapping(path = "/edituser/{id}")
	public String delUser(@PathVariable("id") Long id, Model model) {
		userService.editUserEnable(id);
		model.addAttribute("users", userService.findAllUsers());
		return "users";
	}

	@PostMapping("/registration")
	public ModelAndView registration(@Valid User user, BindingResult bindingResult, ModelMap modelMap) {
		ModelAndView modelAndView = new ModelAndView();
		boolean err = bindingResult.hasErrors();
		if (err) {
			modelAndView.addObject("successMessage", "Please correct the errors in the form");
			modelMap.addAttribute("bindingResult", bindingResult);

		} else {
			userService.registerUser(user);
			modelAndView.addObject("successMessage",
					"Registration is successful! Please check your mailbox for the activation link");
			modelMap.addAttribute("err", err);
		}

		modelAndView.addObject("user", new User());
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@GetMapping(path = "/post/{id}")
	public String postDetailsGet(@PathVariable("id") Long id, Model model) {
		model.addAttribute("post", postService.getPost(id));
		model.addAttribute("comments", commentService.findByPost(id));
		return "post";
	}

	@PostMapping(path = "/post/{id}")
	public String postDetailsPost(@PathVariable("id") Long id, Model model, @ModelAttribute Comment comment) {
		comment.setUser(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
		comment.setPost(postService.getPost(id));
		commentService.addComment(comment);
		model.addAttribute("post", postService.getPost(id));
		model.addAttribute("comments", commentService.findByPost(id));
		return "post";
	}

	@PostMapping("/posting")
	public String posting(@ModelAttribute Post post, Model model) {
		postService.addPost(post);
		model.addAttribute("posts", postService.getPosts());
		return "index";
	}

	@GetMapping(path = "/activation/{code}")
	public String activation(@PathVariable("code") String code, HttpServletResponse Response) {
		userService.userActivation(code);
		return "auth/login";
	}

}
