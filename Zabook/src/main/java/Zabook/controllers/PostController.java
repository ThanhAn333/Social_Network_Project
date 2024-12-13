package Zabook.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;

import Zabook.models.Image;
import Zabook.models.Post;
import Zabook.models.User;
import Zabook.models.Video;
import Zabook.services.IPostService;
import Zabook.services.IUserService;
import Zabook.services.IVideoService;
import Zabook.services.IImageService;

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
	public ModelAndView createPost(@RequestParam String content, @RequestParam("images") MultipartFile[] images,
			@RequestParam("videos") MultipartFile[] videos, ModelMap modelMap, Principal principal) {

		// Tạo đối tượng Post mới
		Post newpost = new Post();
		newpost.setComment(new ArrayList<>());

		newpost.setContent(content);
		newpost.setCreatedAt(LocalDateTime.now());

		// Lấy thông tin người dùng hiện tại
		ObjectId userid = userService.getCurrentBuyerId(principal);
		User user = new User();
		user.setUserID(userid);
		newpost.setUser(user);

		// Xác định đường dẫn đầy đủ đến thư mục static
		String staticDir = new File("src/main/resources/static").getAbsolutePath();
		String imageDir = staticDir + "/uploads/images/";
		String videoDir = staticDir + "/uploads/videos/";

		
		//createDirectory(imageDir);
		//createDirectory(videoDir);

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
			}}
		}
		newpost.setVideo(videoList); // Gán danh sách video vào bài viết
		newpost.setLikeCount(0);
		newpost.setLikedUsers(null);
		
		// Lưu Post vào database
		Post savedPost = postService.createPost(newpost);

		if (savedPost != null) {
			modelMap.addAttribute("post", savedPost);
		} else {
			modelMap.addAttribute("message", "Lỗi");
		}

		// Chuyển hướng sau khi tạo post
		return new ModelAndView("redirect:/user/", modelMap);
	}

	// Hàm tạo thư mục
	private void createDirectory(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	// Hàm lưu file
	private String saveFile(MultipartFile file, String uploadDir) {
		try {
			if (!file.isEmpty()) {
				// Đặt tên tệp mới và tạo đường dẫn
				String originalFilename = file.getOriginalFilename();
				String filePath = uploadDir + UUID.randomUUID() + "_" + originalFilename;
				File dest = new File(filePath);
				file.transferTo(dest); // Lưu tệp vào thư mục
				return filePath; // Trả về đường dẫn của tệp đã lưu
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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

	@PostMapping("/{originalPostId}/share")
	public ResponseEntity<String> sharePost(@PathVariable ObjectId originalPostId, @RequestParam ObjectId userId) {
		Post sharedPost = postService.sharePost(userId, originalPostId);
		return ResponseEntity.ok("Share success!!");
	}


	@PostMapping("/like/")
	public ResponseEntity<?> likePost(@RequestBody ObjectId postId) {
	    try {
	        // Tìm bài viết theo postId
	        Post post = postService.findById(postId)
	                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

	        post.incrementLikeCount();

	      
	        postService.createPost(post);

	        return ResponseEntity.ok("Cảm xúc đã được gửi!");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra.");
	    }
	}
	
	@GetMapping("/likeList")
    public List<User> getLikeList(@RequestParam ObjectId postId) {
        // Gọi service để lấy danh sách người đã like bài viết
        return postService.getUsersWhoLiked(postId);
    }

	

	
	
	@PostMapping("/like")
	public String likePost2(@RequestParam ObjectId postId) {
	    try {
	        // Tìm bài viết theo postId
	        Post post = postService.findById(postId)
	                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

	        // Tìm người dùng (ví dụ: lấy từ SecurityContextHolder nếu bạn dùng Spring Security)
	        User currentUser = userService.getCurrentUser();

	        // Kiểm tra xem người dùng đã "like" bài viết này chưa
	       // if (post.getLikedUsers().contains(currentUser)) {
	      //      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bạn đã thích bài viết này.");
	       // }

	        // Tăng số lượng like
	        post.incrementLikeCount();

	        // Thêm người dùng vào danh sách người thích
	      //  post.addLikedUser(currentUser);

	        // Lưu lại bài viết
	        postService.createPost(post);

	        return "redirect:/user/";
	    } catch (Exception e) {
	        return null;
	    }
	}
}
