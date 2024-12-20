package Zabook.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Zabook.models.Image;
import Zabook.models.Notification;
import Zabook.models.NotificationType;
import Zabook.models.Post;
import Zabook.models.User;
import Zabook.models.Video;
import Zabook.services.IPostService;
import Zabook.services.IUserService;
import Zabook.services.IVideoService;
import Zabook.services.IImageService;
import Zabook.services.INotificationService;

@Controller
@RequestMapping("/user/post")

public class PostController {

	@Autowired
	IPostService postService;
	@Autowired
	IUserService userService;
	@Autowired
	IImageService imageService;
	@Autowired
	IVideoService videoService;

	@Autowired
	INotificationService  notificationService;

	@GetMapping("")
	public String index() {
		return "index";
	}

	@GetMapping("/users/")
	public String getUserPosts(Principal principal, Model model) {

		List<Post> list = postService.findByUserId(userService.getCurrentBuyerId(principal));
		model.addAttribute("listpost", list);
		return "test";
	}

	@PostMapping("/create")
	public String createPost(@RequestParam String content, @RequestParam("images") MultipartFile[] images,
			@RequestParam("videos") MultipartFile[] videos,  Principal principal) {

		// Tạo đối tượng Post mới
		Post newpost = new Post();
		newpost.setComment(new ArrayList<>());

		newpost.setContent(content);
		newpost.setCreatedAt(LocalDateTime.now());

		// Lấy thông tin người dùng hiện tại
		//ObjectId userid = userService.getCurrentBuyerId(principal);
		User user = userService.getCurrentUser();
		
		newpost.setUser(user);

		// Xác định đường dẫn đầy đủ đến thư mục static
		String staticDir = new File("src/main/resources/static").getAbsolutePath();
		String imageDir =staticDir+ "/uploads/images/";
		String videoDir =staticDir+ "/uploads/videos/";
		

		// Lưu ảnh
		List<Image> imageList = new ArrayList<>();
		for (MultipartFile image : images) {
			if (image != null && !image.isEmpty()) {
				String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
				Path path = Paths.get(imageDir + fileName);
				try {
					Files.write(path, image.getBytes()); // Lưu tệp
					Image img = new Image();
					img.setLinkImage(fileName); // Đường dẫn ảnh
					img.setTypeImage(image.getContentType()); // Loại ảnh
					Image savedImage = imageService.addImage(img);
					imageList.add(savedImage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		newpost.setImage(imageList); // Gán danh sách ảnh vào bài viết

		// Lưu video
		List<Video> videoList = new ArrayList<>();
		for (MultipartFile video : videos) {
			if (video != null && !video.isEmpty()) {
				String fileName = UUID.randomUUID() + "_" + video.getOriginalFilename();
				Path path = Paths.get(videoDir + fileName);
				try {
					Files.write(path, video.getBytes()); // Lưu tệp
					Video vid = new Video();
					vid.setLinkVideo(fileName); // Đường dẫn video
					vid.setTypeVideo(video.getContentType()); // Loại video
					Video savedVideo = videoService.addVideo(vid);
					videoList.add(savedVideo);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		newpost.setVideo(videoList); // Gán danh sách video vào bài viết
		//newpost.setLikeCount(0);
		newpost.setLikedUsers(new ArrayList<>());
		// Lưu Post vào database
		Post savedPost = postService.createPost(newpost);
		user.setImage(imageList);
		user.setVideo(videoList);
		userService.updateUser(user);
		if (savedPost != null) {
			
		} else {
			
		}

		// Chuyển hướng sau khi tạo post
		return "redirect:/user/";
	}

	

	@DeleteMapping("/delete/{id}")
	public ModelAndView deletePost(@PathVariable ObjectId id, ModelMap modelMap) {
		if (postService.existsById(id)) {
			postService.deletePost(id);
			modelMap.addAttribute("message", "Deleted successfully!");
		} else {
			modelMap.addAttribute("message", "Post not found!");
		}
		return new ModelAndView("index", modelMap);
	}


	@GetMapping("/edit/{id}")
	public ModelAndView editPost(@PathVariable ObjectId id, ModelMap modelMap) {
		Optional<Post> optionalPost = postService.findById(id);
		if (optionalPost.isPresent()) {
			modelMap.addAttribute("post", optionalPost.get());
		} else {
			modelMap.addAttribute("message", "Post not found");
		}
		return new ModelAndView("test", modelMap);
	}

	@PostMapping("/update")
	public ModelAndView update(@RequestBody Post post, ModelMap modelMap) {
		Post newpost = postService.updatePost(post);

		if (newpost != null) {
			modelMap.addAttribute("message", "Đăng bài thành công");
			modelMap.addAttribute("post", newpost);
		} else {
			modelMap.addAttribute("message", "Lỗi");
		}

		// Trả về trang view hoặc chuyển hướng
		return new ModelAndView("index", modelMap);
	}

	@PostMapping("/share")
	public String sharePost(@RequestParam String postId, @RequestParam String content) {
		ObjectId postid = new ObjectId(postId);
		Post post = postService.findById(postid).orElse(null);
		User user = userService.getCurrentUser();
		Post sharedPost = postService.sharePost(user.getUserID(), postid,content);
		notificationService.sendNotification(
    				post.getUser().getUserID().toString(),
    				NotificationType.SHARE,
    				user.getLastName(),
    				postId,
					post.getUser().getUserID().toString()

				);
		return "redirect:/user/";
	}

	

	@PostMapping("/likeList")
	public ResponseEntity<?> getLikeList(@RequestParam String postId) {
		// Gọi service để lấy danh sách người đã like bài viết
		ObjectId objectId = new ObjectId(postId);
		List<User> users= postService.getUsersWhoLiked(objectId);
		
		return ResponseEntity.ok(users);
	}


	@PostMapping("/updateReaction")
	@ResponseBody
	public ResponseEntity<?> updateReaction(@RequestParam String postId, @RequestParam String reaction) {
	    try {
	        // Convert postId từ String sang ObjectId
	        ObjectId objectId = new ObjectId(postId);
	        
	        // Tìm bài viết theo postId
	        Post post = postService.findById(objectId)
	                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

	        // Tìm người dùng hiện tại (ví dụ nếu dùng Spring Security)
	        User currentUser = userService.getCurrentUser();

	        // Kiểm tra xem người dùng đã thích bài viết này chưa
	        if(post.getLikedUsers()!=null) {
	        	 if ("like".equals(reaction) && post.getLikedUsers().contains(currentUser)) {
	 	            // Trả về thông báo rằng đã thích bài viết
	 	            return ResponseEntity.status(HttpStatus.CONFLICT)
	 	            		.body(Map.of(
	                                "message", "Bạn đã thích bài viết này rồi.",
	                                "likeCount", post.getLikedUsers()
	                        ));
	 	        }
	        }
	       

	        if ("like".equals(reaction)) {
	            //post.incrementLikeCount(); // Tăng số lượng like
	            post.addLikedUser(currentUser); // Thêm người dùng vào danh sách likedUsers
				notificationService.sendNotification(
    				post.getUser().getUserID().toString(),
    				NotificationType.LIKE,
    				currentUser.getLastName(),
    				postId,
					post.getUser().getUserID().toString()

				);
	        } else if ("unlike".equals(reaction)) {
	            post.removeLikedUser(currentUser); // Xóa người dùng khỏi danh sách likedUsers
	            
	            //post.decrementLikeCount(); // Giảm số lượng like
	            
	            
	        } else {
	        	System.out.println("Lỗi like");
	            return ResponseEntity.badRequest().body("Hành động không hợp lệ");
	        }

	        // Lưu lại bài viết đã được cập nhật
	        Post post2 = postService.createPost(post); // Đảm bảo sử dụng phương thức save hoặc update thay vì createPost

	        int updatedLikeCount = post2.getLikedUsers().size();
	        System.out.println(updatedLikeCount);
	        return ResponseEntity.ok(updatedLikeCount); 
	        // Trả về số lượng like sau khi cập nhật
	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Đã xảy ra lỗi: " + e.getMessage());
	    }
	}

}
