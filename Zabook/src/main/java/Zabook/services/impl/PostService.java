package Zabook.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Zabook.models.Post;
import Zabook.models.User;
import Zabook.repository.PostRepository;
import Zabook.repository.UserRepository;
import Zabook.services.IPostService;

@Service
public class PostService implements IPostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepo;

	@Override
	public Post createPost(Post post) {
		return postRepository.save(post);
	}

	@Override
	public List<Post> findByUserId(ObjectId userId) {
		return postRepository.findByUserId(userId);
	}

	@Override
	public void deletePost(ObjectId postId) {
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
	public boolean existsById(ObjectId id) {
		if (postRepository.existsById(id)) {
			return true;
		} else
			return false;
	}

	@Override
	public Optional<Post> findById(ObjectId id) {
		return postRepository.findById(id);
	}

	@Override
	public Post sharePost(ObjectId userId, ObjectId originalPostId, String contentShare) {
		// Tìm bài viết gốc
		Post originalPost = postRepository.findById(originalPostId)
				.orElseThrow(() -> new RuntimeException("Post not found"));

		// Tăng số lần chia sẻ của bài gốc
		originalPost.setShareCount(originalPost.getShareCount() + 1);
		postRepository.save(originalPost);
		Optional<User> userOptional = userRepo.findById(userId);
		if (userOptional.isEmpty()) {
			throw new RuntimeException("User not found");
		}

		// Lấy User từ Optional
		User user = userOptional.get();

		// Tạo bài viết chia sẻ
		Post sharedPost = new Post();
		sharedPost.setUser(user);

		sharedPost.setOriginalPostId(originalPostId);
		sharedPost.setContentShare(contentShare);
		sharedPost.setContent(originalPost.getContent()); // Copy nội dung (hoặc thay đổi tùy ý)
		sharedPost.setCreatedAt(LocalDateTime.now());
		sharedPost.setImage(originalPost.getImage());
		sharedPost.setShared(true);
		sharedPost.setShareCount(0);

		return postRepository.save(sharedPost);
	}

	@Override
	public List<Post> getAllPost() {
		// TODO Auto-generated method stub
		return postRepository.findAll();
	}

	@Override
	public List<User> getUsersWhoLiked(ObjectId postId) {
		Post post = postRepository.findById(postId).orElse(null);

		// Kiểm tra bài viết có tồn tại không, nếu không trả về null hoặc danh sách rỗng
		if (post != null) {
			return post.getLikedUsers(); // Trả về danh sách người dùng đã like
		}

		return null;
	}
	
	@Override
	public Optional<Post> getPostByCommentId(ObjectId commentId) {
        return postRepository.findByCommentId(commentId);
    }
	@Override
	public List<Post> getAllPostSortedByTime() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

}
