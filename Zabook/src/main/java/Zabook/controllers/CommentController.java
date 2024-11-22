package Zabook.controllers;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Zabook.models.Comment;
import Zabook.services.impl.CommentService;

@Controller
@RequestMapping("/comments")
public class CommentController {

	private final CommentService commentService;

	// Inject service thông qua constructor
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping("")
	public ResponseEntity<String> addComment(@RequestBody Comment comment) {
		ObjectId postId = comment.getPost().getId();
		String content = comment.getContent();
		double rate = comment.getRate();
		ObjectId userId = comment.getUserComment().getUserID();

		try {
			commentService.addComment(postId, userId, content, rate);
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
