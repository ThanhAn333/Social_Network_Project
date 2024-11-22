package Zabook.services;

import org.bson.types.ObjectId;

public interface ICommentService {
	void addComment(ObjectId postId, ObjectId userId, String content, double rate);
	void editComment(ObjectId commentId, ObjectId userId, String content);
	void deleteComment(ObjectId commentId, ObjectId userId);
}
