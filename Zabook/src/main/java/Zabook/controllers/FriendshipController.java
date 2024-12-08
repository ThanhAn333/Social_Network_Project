package Zabook.controllers;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.ui.Model;

import Zabook.models.User;
import Zabook.models.FriendShip;
import Zabook.services.impl.FriendshipService;

@Controller
@RequestMapping("/friendships")
public class FriendshipController {
	@Autowired
	private FriendshipService friendshipService;

	// API gửi lời mời kết bạn
	@PostMapping("/request")
	public ResponseEntity<?> sendFriendRequest(@RequestParam ObjectId senderId, @RequestParam ObjectId receiverId) {
		try {
			FriendShip friendship = friendshipService.sendFriendRequest(senderId, receiverId);
			return ResponseEntity.ok(friendship);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// API chấp nhận lời mời kết bạn
	@PutMapping("/accept/{friendshipId}")
	public ResponseEntity<?> acceptFriendRequest(@PathVariable ObjectId friendshipId) {
		try {
			FriendShip friendship = friendshipService.acceptFriendRequest(friendshipId);
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
}
