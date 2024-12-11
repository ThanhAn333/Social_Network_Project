package Zabook.services;

import java.util.List;

import org.bson.types.ObjectId;

import Zabook.models.Comment;

public interface ICommentService {
	void addComment(ObjectId postId, ObjectId userId, String content, double rate);
	void editComment(ObjectId commentId, ObjectId userId, String content);
	void deleteComment(ObjectId commentId, ObjectId userId);
	public List<Comment> getCommentsByPostId(ObjectId postId);
    
}
