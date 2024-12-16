package Zabook.controllers;


import java.awt.Image;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import Zabook.dto.FriendshipStatus;
import Zabook.models.Notification;
import Zabook.dto.UserDTO;
import Zabook.models.FriendShip;
import Zabook.models.Post;
import Zabook.models.Story;
import Zabook.models.User;
import Zabook.models.Video;
import Zabook.services.INotificationService;
import Zabook.services.IPostService;
import Zabook.services.IStoryService;
import Zabook.services.impl.CommentService;
import Zabook.services.impl.FriendshipService;
import Zabook.services.impl.UserService;


@Controller
@RequestMapping("/user")
public class UserController {
	
	private final CommentService commentService;
	UserService userService;
	IPostService postService;

    @Autowired
    private IStoryService storyService; 
    
    @Autowired
	private FriendshipService friendshipService;

    @Autowired
    private INotificationService notificationService;

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
    	User user = userService.getCurrentUser();
    	List<Post> posts = postService.getAllPostSortedByTime();
        model.addAttribute("currentuser", user);

        storyService.updateStoryStatusIfExpired();
        List<Story> stories = storyService.getActiveStories();
        model.addAttribute("stories",stories);
    	model.addAttribute("posts",posts);
    	model.addAttribute("id",userId);
    	model.addAttribute("user",user);


        List<Notification> notifications = notificationService.getNotifications(userId.toString()); 
        model.addAttribute("notifications", notifications);
        return "user/index";
    }

    @GetMapping("/profile/{userId}")
    public String viewProfile(@PathVariable ObjectId userId, Model model) {
        // Lấy thông tin người dùng từ cơ sở dữ liệu
    	User user1 = userService.getCurrentUser();
        model.addAttribute("currentuser", user1);


        List<Notification> notifications = notificationService.getNotifications(user1.toString()); 
        model.addAttribute("notifications", notifications);

        User user = userService.getUserById(userId.toString());
        if (user == null) {
            return "redirect:/error";  
        }

        model.addAttribute("user", user);
        return "user/ProfileUser"; 
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
    
    @GetMapping("/inviteFriend")
    public String getMethodName(Model model) {
        User currentUser = userService.getCurrentUser();
        List<FriendShip> requestList = friendshipService.getPendingRequests(currentUser.getUserID());
        model.addAttribute("currentuser", currentUser);
        model.addAttribute("requestFriends",requestList );
        return "user/inviteFriends";
    }
    
    
    //Long
    @GetMapping("/getalluser")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
    	// Lấy người dùng hiện tại
        User currentUser = userService.getCurrentUser();
        ObjectId currentUserId = currentUser.getUserID();

        // Lấy tất cả người dùng và ánh xạ sang UserDTO
        List<UserDTO> users = userService.getAllUsers().stream().map(user -> {
            // Lấy thông tin cơ bản
            String fullName = user.getFirstName() + " " + user.getLastName();
            ObjectId otherUserId = user.getUserID();

            // Trạng thái tình bạn
            FriendshipStatus friendshipStatus = friendshipService.getFriendshipStatus(currentUserId, otherUserId);

            // Lấy thông tin mối quan hệ (nếu có)
            FriendShip friendship = friendshipService.getFriendshipBetweenUsers(currentUserId, otherUserId);
            String friendshipId = friendship != null ? friendship.getFriendshipID().toString() : null;
            boolean isSender = friendship != null && friendship.getUser1().equals(currentUser);

            // Tính số lượng bạn chung
            //int mutualFriends = calculateMutualFriends(currentUser, user);
            int mutualFriends = 0;

            // Ánh xạ sang UserDTO
            return new UserDTO(
                user.getUserID(),
                fullName,
                user.getAvatar(),
                mutualFriends,
                friendshipStatus,
                friendshipId,
                isSender
            );
        }).collect(Collectors.toList());

        // Log danh sách kết quả để kiểm tra
        System.out.println("Mapped UserDTO List: " + users);

        return ResponseEntity.ok(users);
    }
}
