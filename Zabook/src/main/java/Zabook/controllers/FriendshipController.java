package Zabook.controllers;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Zabook.dto.UserDTO;
import Zabook.models.FriendShip;
import Zabook.models.User;
import Zabook.services.impl.FriendshipService;
import Zabook.services.impl.UserService;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/friendships")
public class FriendshipController {
	@Autowired
	private FriendshipService friendshipService;
	@Autowired
	UserService userService;
	

	@GetMapping("")
    public String AddFriends(Model model) {
		
		User user = userService.getCurrentUser();
		model.addAttribute("currentuser", user);
		
        // Thêm logic xử lý dữ liệu nếu cần
        return "friends"; // Trả về trang messenger.html
    }
	
	// API gửi lời mời kết bạn
	@PostMapping("/request")
	public String postMethodName(
		@RequestParam("senderId") String senderId,
		@RequestParam("receiverId") String receiverId
	) {
		System.out.println("Received senderId: " + senderId);
		System.out.println("Received receiverId: " + receiverId);
		ObjectId senderObjectId = new ObjectId(senderId);
		ObjectId receiverObjectId = new ObjectId(receiverId);
	   try {
		   FriendShip friendship = friendshipService.sendFriendRequest(senderObjectId, receiverObjectId);
		   User user = userService.getCurrentUser();
		   List<FriendShip> list = user.getFriendships();
		   list.add(friendship);
		   user.setFriendships(list);
		   userService.updateUser(user);
		   return "redirect:/user/profile/" + receiverId;

	   } catch (Exception e) {
			return "redirect:/user/profile/" + receiverId;

	   }
	}
	
	// API chấp nhận lời mời kết bạn
	@PutMapping("/accept/{friendshipId}")
	public ResponseEntity<?> acceptFriendRequest(@PathVariable ObjectId friendshipId) {
		try {
			FriendShip friendship = friendshipService.acceptFriendRequest(friendshipId);
			UserDTO senderDTO = friendshipService.convertToUserDTO(friendship.getUser1(), friendship);  // Cập nhật trạng thái của sender
	        UserDTO receiverDTO = friendshipService.convertToUserDTO(friendship.getUser2(), friendship);  // Cập nhật trạng thái của receiver
	        return ResponseEntity.ok(friendship);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// API từ chối lời mời kết bạn
	@PutMapping("/reject/{friendshipId}")
	public ResponseEntity<?> rejectFriendRequest(@PathVariable ObjectId friendshipId) {
		try {
			FriendShip friendship = friendshipService.rejectFriendRequest(friendshipId);
			return ResponseEntity.ok(friendship);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// API lấy danh sách bạn bè
	@GetMapping("/friends/{userId}")
	public ResponseEntity<List<User>> getFriendList(@PathVariable ObjectId userId) {
		List<User> friends = friendshipService.getFriendList(userId);
		return ResponseEntity.ok(friends);
	}

	// API lấy danh sách lời mời kết bạn đang chờ
	@GetMapping("/pending/{userId}")
	public ResponseEntity<List<FriendShip>> getPendingRequests(@PathVariable ObjectId userId) {
		List<FriendShip> pendingRequests = friendshipService.getPendingRequests(userId);
		return ResponseEntity.ok(pendingRequests);
	}

	// Thêm method mới để hiển thị trang Friends.html
	@GetMapping("/view")
	public String showFriendsPage(Model model, @RequestParam(required = false) ObjectId userId) {
		if (userId != null) {
			// Lấy danh sách lời mời kết bạn
			List<FriendShip> pendingRequests = friendshipService.getPendingRequests(userId);
			model.addAttribute("friendRequests", pendingRequests);
			
			// Lấy danh sách bạn bè
			List<User> friends = friendshipService.getFriendList(userId);
			model.addAttribute("friendsList", friends);
		}
		
		return "Friends"; // Trả về tên file Friends.html
	}

	// API tìm kiếm người dùng
	@GetMapping("/search")
	public ResponseEntity<List<User>> searchUsers(
			@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName) {
		try {
			List<User> users = friendshipService.searchUsers(firstName, lastName);
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	// API tìm kiếm với một từ khóa chung
	@GetMapping("/search/keyword")
	public ResponseEntity<List<User>> searchByKeyword(@RequestParam String keyword) {
		try {
			// Sử dụng cùng một từ khóa cho cả firstName và lastName
			List<User> users = friendshipService.searchUsers(keyword, keyword);
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	// API hiển thị trang tìm kiếm
	@GetMapping("/search-page")
	public String showSearchPage(
			Model model,
			@RequestParam(required = false) String keyword) {
		
		if (keyword != null && !keyword.trim().isEmpty()) {
			List<User> searchResults = friendshipService.searchUsers(keyword, keyword);
			model.addAttribute("searchResults", searchResults);
			model.addAttribute("keyword", keyword);
		}
		return "UserSearch";
	}
}
