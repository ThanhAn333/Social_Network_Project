package Zabook.controllers;

import java.security.Principal;
import java.util.List;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import Zabook.models.Post;
import Zabook.models.User;
import Zabook.services.IPostService;
import Zabook.services.IUserService;
@Controller
@RequestMapping("/user/post")

public class PostController {
	
	@Autowired
	IPostService postService;
	@Autowired
	IUserService userService;
	@GetMapping("")
	public String index() {
		return "index";
	}
	@GetMapping("/users/")
	public String getUserPosts(Principal principal, Model model) {
		
	    List<Post> list = postService.findByUserId(userService.getCurrentBuyerId(principal));
	    model.addAttribute("listpost", list);
	    return "test"; 
	}
	@PostMapping("/create")
	public ModelAndView createPost(@RequestBody Post post, ModelMap modelMap) {
	    Post newpost = postService.createPost(post);

	    if (newpost != null) {
	        modelMap.addAttribute("message", "Đăng bài thành công");
	        modelMap.addAttribute("post", newpost);
	    } else {
	        modelMap.addAttribute("message", "Lỗi");
	    }

	    // Trả về trang view hoặc chuyển hướng
	    return new ModelAndView("index", modelMap);
	}
	@DeleteMapping("/delete/{id}")
	public ModelAndView deletePost(@PathVariable ObjectId id, ModelMap modelMap) {
	    if (postService.existsById(id)) {
	        postService.deletePost(id);
	        modelMap.addAttribute("message", "Deleted successfully!");
	    } else {
	        modelMap.addAttribute("message", "Post not found!");
	    }
	    return new ModelAndView("index", modelMap);
	}

	@GetMapping("/edit/{id}")
	public ModelAndView editPost(@PathVariable ObjectId id, ModelMap modelMap) {
	    Optional<Post> optionalPost = postService.findById(id);
	    if (optionalPost.isPresent()) {
	        modelMap.addAttribute("post", optionalPost.get());
	    } else {
	        modelMap.addAttribute("message", "Post not found");
	    }
	    return new ModelAndView("test", modelMap);
	}

	
	@PostMapping("/update")
	public ModelAndView update(@RequestBody Post post, ModelMap modelMap) {
	    Post newpost = postService.updatePost(post);

	    if (newpost != null) {
	        modelMap.addAttribute("message", "Đăng bài thành công");
	        modelMap.addAttribute("post", newpost);
	    } else {
	        modelMap.addAttribute("message", "Lỗi");
	    }

	    // Trả về trang view hoặc chuyển hướng
	    return new ModelAndView("index", modelMap);
	}

	
	@PostMapping("/{originalPostId}/share")
    public ResponseEntity<String> sharePost(@PathVariable ObjectId originalPostId, @RequestParam ObjectId userId) {
        Post sharedPost = postService.sharePost(userId, originalPostId);
        return ResponseEntity.ok("Share success!!");
    }

}
