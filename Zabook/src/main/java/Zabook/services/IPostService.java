package Zabook.services;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import Zabook.models.Post;

public interface IPostService {
	Post createPost(Post post) ;

    List<Post> getUserPosts(ObjectId userId);
    
    void deletePost(String postId);
    
    Post updatePost(Post post);

	boolean existsById(String id); 
	
	Optional<Post> findById(String id);
}
