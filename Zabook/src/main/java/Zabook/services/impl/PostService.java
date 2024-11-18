package Zabook.services.impl;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Zabook.models.Post;
import Zabook.repository.PostRepository;
import Zabook.services.IPostService;
@Service
public class PostService implements IPostService{

	@Autowired
    private PostRepository postRepository;

	@Override
	public Post createPost(Post post) {
		return postRepository.save(post);
	}

	@Override
	public List<Post> getUserPosts(ObjectId userId) {
		return postRepository.findByUserId(userId);
	}

	@Override
	public void deletePost(String postId) {
		postRepository.deleteById(postId);
	}

	@Override
	public Post updatePost(Post post) {
	    if (postRepository.existsById(post.getId())) {
	        Post exitPost = postRepository.findById(post.getId()).orElse(null);
	        if (exitPost != null) {
	        	exitPost.setContent(post.getContent());
	        	exitPost.setImage(post.getImage());
	            return postRepository.save(exitPost); 
	        }
	    }
	    return null; 
	}

	@Override
	public boolean existsById(String id) {
		if(postRepository.existsById(id)){
			return true;
		}else
			return false;
	}

	@Override
	public Optional<Post> findById(String id) {
		return postRepository.findById(id);
	}

}