package Zabook.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Zabook.models.Comment;
import Zabook.models.NotificationType;
import Zabook.models.Post;
import Zabook.models.User;
import Zabook.services.INotificationService;
import Zabook.services.IPostService;
import Zabook.services.impl.CommentService;
import Zabook.services.impl.UserService;

@Controller
@RequestMapping("/user/comments")
public class CommentController {

	private final CommentService commentService;
	UserService userService;
	IPostService postService;

	@Autowired
	INotificationService notificationService;

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

	
	@PostMapping("/addOrEdit")
	public String addComment(
	        //@RequestBody Comment comment,
	        @RequestParam("content") String content,
	        @RequestParam("postId") String postId,
	        @RequestParam(required = false) String commentId,
	        Principal principal) {

	    try {
	    	 if (commentId == null || commentId.isEmpty()) {
	    		ObjectId postObjectId = new ObjectId(postId);
	 	        Post post = postService.findById(postObjectId).orElse(null);
	 	        User currentUser = userService.getCurrentUser();
	 	        ObjectId userId = userService.getCurrentBuyerId(principal);	 	        
	 	        // Gọi service để thêm bình luận
	 	        commentService.addComment(postObjectId, userId, content, 0);

				notificationService.sendNotification(
    				post.getUser().getUserID().toString(),
    				NotificationType.COMMENT,
    				currentUser.getLastName(),
    				postId,
					post.getUser().getUserID().toString()

				);
	    	 }else {
	    		 ObjectId commentid = new ObjectId(commentId);
	    		 User user = userService.getCurrentUser();
	    		 commentService.editComment(commentid, user.getUserID(), content);
	    	 }
	        
	        return "redirect:/user/";
	    } catch (Exception e) {
	        return null;
	    }
	}


	@PutMapping("/{commentId}")
	public ResponseEntity<String> editComment(@PathVariable ObjectId commentId,@RequestParam String content) {
	    // Trường hợp này commentId đã được lấy từ URL, còn các giá trị khác vẫn lấy từ request body
	   
	    User user = userService.getCurrentUser();

	    try {
	        commentService.editComment(commentId, user.getUserID(), content);
	        return ResponseEntity.ok("Comment updated successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
	    }
	}


	@DeleteMapping("/{commentId}")
	public ResponseEntity<String> deleteComment(@PathVariable String commentId) {
		
		ObjectId commentId1 = new ObjectId(commentId);
		User user = userService.getCurrentUser();
		try {
			
			commentService.deleteComment(commentId1, user.getUserID());
			return ResponseEntity.ok("Comment deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: " + e.getMessage());
		}
	}
}
