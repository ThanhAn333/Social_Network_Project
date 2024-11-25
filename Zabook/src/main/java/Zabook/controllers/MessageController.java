package Zabook.controllers;

import org.bson.types.ObjectId;
import Zabook.models.Message;
import Zabook.services.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
	@Autowired
    private IMessageService messageService;

	 // API: Gửi tin nhắn
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
        @RequestParam ObjectId senderId,
        @RequestParam ObjectId receiverId,
        @RequestParam String content,
        @RequestParam(required = false) ObjectId attachmentId // Optional attachment
    ) {
        Message message = messageService.sendMessage(senderId, receiverId, content, attachmentId);
        return ResponseEntity.ok(message);
    }

    // API: Lấy danh sách tin nhắn giữa hai người
    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(
        @RequestParam ObjectId user1Id,
        @RequestParam ObjectId user2Id
    ) {
        List<Message> messages = messageService.getMessagesBetweenUsers(user1Id, user2Id);
        return ResponseEntity.ok(messages);
    }

    // API: Lấy danh sách người đã nhắn và tin nhắn gần nhất
    @GetMapping("/recent")
    public ResponseEntity<List<Message>> getRecentMessages(@RequestParam ObjectId userId) {
        List<Message> messages = messageService.getRecentMessages(userId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/sent/{senderId}")
    public List<Message> getSentMessages(@PathVariable ObjectId senderId) {
        return messageService.getMessagesBySender(senderId);
    }

    @GetMapping("/received/{receiverId}")
    public List<Message> getReceivedMessages(@PathVariable ObjectId receiverId) {
        return messageService.getMessagesByReceiver(receiverId);
    }
}

