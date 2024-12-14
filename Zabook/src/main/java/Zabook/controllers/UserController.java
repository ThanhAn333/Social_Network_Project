package Zabook.controllers;


import java.awt.Image;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import Zabook.models.Post;
import Zabook.models.Story;
import Zabook.models.User;
import Zabook.models.Video;
import Zabook.services.IPostService;
import Zabook.services.IStoryService;
import Zabook.services.impl.CommentService;
import Zabook.services.impl.UserService;


@Controller
@RequestMapping("/user")
public class UserController {
	
	private final CommentService commentService;
	UserService userService;
	IPostService postService;

    @Autowired
    private IStoryService storyService; 

	// Inject service thông qua constructor
	public UserController(CommentService commentService, UserService userService,IPostService postService) {
		this.commentService = commentService;
		this.userService=userService;
		this.postService=postService;
	}

    
	//lâm
	@GetMapping("/header")
	public String header() {
		return "user/header";
	}
	
	//anh
	@GetMapping("")
	public String getPostByUserId(Principal principal, Model model) {
	    try {
	    	ObjectId userId = userService.getCurrentBuyerId(principal);
	        List<Post> posts = postService.findByUserId(userId);
            
            // Gọi service để lấy danh sách bình luận theo bài viết
	        //List<Comment> comments = commentService.getCommentsByPostId(postObjectId);
	        model.addAttribute("posts",posts);
            model.addAttribute("hilo","hi");
	        return "user/index";
	    } catch (Exception e) {
	        return null;
	    }
	}

    @Value("${upload.directory}")
    private String uploadDir;

    @GetMapping("/")
    public String getMethodName(Model model,Principal principal) {
    	ObjectId userId = userService.getCurrentBuyerId(principal);
    	List<Post> posts = postService.getAllPost();
        User user = userService.getCurrentUser();
        model.addAttribute("currentuser", user);
        List<Story> stories = storyService.getActiveStories();
        model.addAttribute("stories",stories);
    	model.addAttribute("posts",posts);
    	model.addAttribute("id",userId);
        return "user/index";
    }

    //lâm
    @GetMapping("/profile")
    public String profile(Model model) {

        User user = userService.getCurrentUser();
        List<Post> posts = postService.findByUserId(user.getUserID());
        List<User> FriendList = userService.getFriendList(user);
        List<Zabook.models.Image> images = userService.getImages(user);
        List<Video> videos = userService.getVideos(user);

        model.addAttribute("images", images);
        model.addAttribute("videos", videos);
        model.addAttribute("friendList", FriendList);
        model.addAttribute("posts", posts );
        model.addAttribute("currentuser", user);
        
        return "user/profile-page";
    }
   
	@PostMapping("/profile/deletepost/{id}")
	public String deletePostFromProfilePage(@PathVariable ObjectId id) {
		postService.deletePost(id);
		return "redirect:/user/profile";

	}
	//lâm
    @PostMapping("/profile/save_avatar")
    public String changeAvatar(
            @RequestParam(value = "avatar", required = false) MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(uploadDir + "/" + fileName);
            Files.write(path, file.getBytes());

            User user = userService.getCurrentUser();
            user.setAvatar("/images/data/" + fileName);
            userService.updateUser(user);
        } catch (IOException e) {
            return "redirect:/user/profile";
        }

        return "redirect:/user/profile";
    }

    @PostMapping("/profile/update_bio")
    @ResponseBody
    public ResponseEntity<String> updateBio(@RequestBody Map<String, String> requestData) {
        String bio = requestData.get("bio");

        // Lấy người dùng hiện tại
        User currentUser = userService.getCurrentUser();
        currentUser.setBio(bio);

        // Cập nhật thông tin
        userService.updateUser(currentUser);

        return ResponseEntity.ok("Tiểu sử đã được cập nhật!");
    }

    @PostMapping("/profile/update_profile")
    @ResponseBody
    public ResponseEntity<String> updateProfile(@RequestBody Map<String, String> requestData) {
        String gender = requestData.get("gender");
        String address = requestData.get("address");

        // Lấy người dùng hiện tại
        User currentUser = userService.getCurrentUser();
        currentUser.setGender(gender);
        currentUser.setAddress(address);

        // Lưu thay đổi
        userService.updateUser(currentUser);
        return ResponseEntity.ok("Thông tin đã được cập nhật!");
    }
    //lâm
}
