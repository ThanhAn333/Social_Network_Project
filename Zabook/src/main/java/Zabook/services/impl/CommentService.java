package Zabook.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Zabook.models.Comment;
import Zabook.models.Post;
import Zabook.models.User;
import Zabook.repository.CommentRepository;
import Zabook.repository.PostRepository;
import Zabook.repository.UserRepository;
import Zabook.services.ICommentService;

@Service
public class CommentService implements ICommentService {

	@Autowired
	PostRepository postRepo;
	
	@Autowired
	CommentRepository commentRepo;
	
	@Autowired
	UserRepository userRepo;
	@Override
	public void addComment(ObjectId postId, ObjectId userId, String content, double rate) {
		// Tìm bài viết
	    Post post = postRepo.findById(postId)
	            .orElseThrow(() -> new RuntimeException("Post not found"));

	    // Tìm người dùng
	    User user = userRepo.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // Tạo comment mới
	    Comment comment = new Comment();
	    comment.setContent(content);
	    comment.setCreateTime(LocalDate.now());
	    comment.setRate(rate);
	    comment.setPost(post);
	    comment.setUserComment(user);

	    // Lưu comment vào cơ sở dữ liệu
	    commentRepo.save(comment);
	   
	    post.getComment().add(comment);
	   

	    // Lưu lại Post
	    postRepo.save(post);

	}
	@Override
	public void editComment(ObjectId commentId, ObjectId userId, String content) {
		Comment comment = commentRepo.findById(commentId)
	            .orElseThrow(() -> new RuntimeException("Comment not found"));

	    // Kiểm tra quyền của người dùng
	    if (!comment.getUserComment().getUserID().equals(userId)) {
	        throw new RuntimeException("You do not have permission to edit this comment");
	    }else {
	    	 throw new RuntimeException(userId.toString()+" - "+comment.getUserComment().getUserID() );
	    }

	    // Cập nhật nội dung và đánh giá
	   // comment.setContent(content);
	   // comment.setCreateTime(LocalDate.now()); // Cập nhật lại thời gian sửa

	    // Lưu lại thay đổi
	   // commentRepo.save(comment);
	}
	@Override
	public void deleteComment(ObjectId commentId, ObjectId userId) {
		// Kiểm tra xem commentId có tồn tại trong cơ sở dữ liệu không
		Comment comment = commentRepo.findById(commentId)
		    .orElseThrow(() -> new RuntimeException("Comment not found"));

		if (!comment.getUserComment().getUserID().equals(userId)) {
		    throw new RuntimeException(userId.toString()+" - "+comment.getUserComment().getUserID() );
		}


		// Xóa bình luận trong cơ sở dữ liệu
		commentRepo.delete(comment);

	}
	@Override
	public List<Comment> getCommentsByPostId(ObjectId postId) {
        return commentRepo.findByPostId(postId);
    }

}
