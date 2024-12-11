package Zabook.controllers;

import java.security.Principal;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Zabook.models.Comment;
import Zabook.models.Post;
import Zabook.services.IPostService;
import Zabook.services.impl.CommentService;
import Zabook.services.impl.UserService;

@Controller
@RequestMapping("/user/comments")
public class CommentController {

	private final CommentService commentService;
	UserService userService;
	IPostService postService;

	// Inject service thông qua constructor
	public CommentController(CommentService commentService, UserService userService,IPostService postService) {
		this.commentService = commentService;
		this.userService=userService;
		this.postService=postService;
	}

	@GetMapping("")
	public ResponseEntity<List<Comment>> getCommentsByPostId(@RequestParam("postId") String postId) {
	    try {
	        // Chuyển đổi postId từ String sang ObjectId
	        ObjectId postObjectId = new ObjectId(postId);
	        // Gọi service để lấy danh sách bình luận theo bài viết
	        List<Comment> comments = commentService.getCommentsByPostId(postObjectId);

	        return ResponseEntity.ok(comments);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
	}

	
	@PostMapping("/add")
	public ResponseEntity<String> addComment(
	        @RequestParam("postId") String postId,
	        @RequestParam("content") String content,
	        @RequestParam(value = "rate", defaultValue = "0") double rate,
	        Principal principal) {

	    try {
	        ObjectId postObjectId = new ObjectId(postId);
	        ObjectId userId = userService.getCurrentBuyerId(principal);

	        // Gọi service để thêm bình luận
	        commentService.addComment(postObjectId, userId, content, rate);

	        return ResponseEntity.ok("Comment added successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
	    }
	}


	@PutMapping("/{commentId}")
	public ResponseEntity<String> editComment(@PathVariable ObjectId commentId, @RequestBody Comment comment) {
	    // Trường hợp này commentId đã được lấy từ URL, còn các giá trị khác vẫn lấy từ request body
	    String content = comment.getContent();
	    ObjectId userId = comment.getUserComment().getUserID();

	    try {
	        commentService.editComment(commentId, userId, content);
	        return ResponseEntity.ok("Comment updated successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
	    }
	}


	@DeleteMapping("/{commentId}")
	public ResponseEntity<String> deleteComment(@PathVariable ObjectId commentId, @RequestBody Comment comment) {
		ObjectId userId = comment.getUserComment().getUserID();
		try {
			commentService.deleteComment(commentId, userId);
			return ResponseEntity.ok("Comment deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: " + e.getMessage());
		}
	}
}
