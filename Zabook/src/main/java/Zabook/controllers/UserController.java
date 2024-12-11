package Zabook.controllers;

import java.security.Principal;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Zabook.models.Comment;
import Zabook.models.Post;
import Zabook.models.User;
import Zabook.services.IPostService;
import Zabook.services.impl.CommentService;
import Zabook.services.impl.UserService;




@Controller
@RequestMapping("/user")
public class UserController {
	
	private final CommentService commentService;
	UserService userService;
	IPostService postService;

	// Inject service thông qua constructor
	public UserController(CommentService commentService, UserService userService,IPostService postService) {
		this.commentService = commentService;
		this.userService=userService;
		this.postService=postService;
	}

	@GetMapping("/")
	public String getMethodName() {
		return "user/index";
	}

	//lâm
	@GetMapping("/header")
	public String header() {
		return "user/header";
	}
	
	//anh
	@GetMapping("")
	public String getPostByUserId(Principal principal, Model model) {
	    try {
	    	ObjectId userId = userService.getCurrentBuyerId(principal);
	        List<Post> posts = postService.findByUserId(userId);
	        // Gọi service để lấy danh sách bình luận theo bài viết
	        //List<Comment> comments = commentService.getCommentsByPostId(postObjectId);
	        model.addAttribute("posts",posts);
	        model.addAttribute("hilo","hi");
	        return "user/index";
	    } catch (Exception e) {
	        return null;
	    }
	}
}
