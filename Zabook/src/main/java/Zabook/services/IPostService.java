package Zabook.services;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import Zabook.models.Post;
import Zabook.models.User;

public interface IPostService {
	Post createPost(Post post) ;
    
    
    Post updatePost(Post post);
	

	void deletePost(ObjectId postId);

	Optional<Post> findById(ObjectId id);

	boolean existsById(ObjectId id);

	List<Post> getUserPosts(User user);
	
	public Post sharePost(ObjectId userId, ObjectId originalPostId);
}
