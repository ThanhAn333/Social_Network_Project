package Zabook.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Zabook.models.Message;
import Zabook.models.User;
import Zabook.services.IMessageService;

import java.util.List;

@Controller
@RequestMapping("/user/messenger")
@CrossOrigin(origins = "*")
public class MessageController {
	@Autowired
	private IMessageService messageService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@GetMapping("")
    public String showMessenger() {
        // Thêm logic xử lý dữ liệu nếu cần
        //return "user/messenger.html"; // Trả về trang messenger.html
		return "user/as.html"; // Trả về trang as.html
    }
	// API gửi tin nhắn thông qua WebSocket
	@MessageMapping("/chat")
	public void processMessage(@Payload Message message) {
		Message savedMessage = messageService.saveMessage(message);

		// Gửi tin nhắn đến người nhận qua WebSocket
		messagingTemplate.convertAndSendToUser(message.getReceiver().getUserID().toString(), "/queue/messages",
				savedMessage);
	}

	// API lấy lịch sử chat giữa 2 người
	@GetMapping("/conversation/{user1Id}/{user2Id}")
	public ResponseEntity<List<Message>> getConversation(@PathVariable String user1Id, @PathVariable String user2Id) {
		List<Message> messages = messageService.getConversation(new ObjectId(user1Id), new ObjectId(user2Id));
		return ResponseEntity.ok(messages);
	}

	// API lấy danh sách người đã nhắn tin gần đây
	@GetMapping("/recent-chats/{userId}")
	public ResponseEntity<List<User>> getRecentChats(@PathVariable String userId) {
		List<User> recentChats = messageService.getRecentChats(new ObjectId(userId));
		return ResponseEntity.ok(recentChats);
	}

	// API lấy tin nhắn gần nhất giữa 2 người
	@GetMapping("/latest/{user1Id}/{user2Id}")
	public ResponseEntity<Message> getLatestMessage(@PathVariable String user1Id, @PathVariable String user2Id) {
		Message latestMessage = messageService.getLatestMessage(new ObjectId(user1Id), new ObjectId(user2Id));
		return ResponseEntity.ok(latestMessage);
	}

	// API đánh dấu tin nhắn đã đọc
	@PutMapping("/{messageId}/read")
	public ResponseEntity<Void> markAsRead(@PathVariable String messageId) {
		messageService.markAsRead(messageId);
		return ResponseEntity.ok().build();
	}

	// API đánh dấu tin nhắn đã gửi đến
	@PutMapping("/{messageId}/delivered")
	public ResponseEntity<Void> markAsDelivered(@PathVariable String messageId) {
		messageService.markAsDelivered(messageId);
		return ResponseEntity.ok().build();
	}
}
