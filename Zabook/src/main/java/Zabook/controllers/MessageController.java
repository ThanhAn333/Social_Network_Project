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
import Zabook.services.IUserService;


@Controller
@RequestMapping("/user")
public class MessageController {

	@Autowired
	IUserService userService;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@GetMapping("/messenger/{userId}")
    public String getChatPage(@PathVariable ObjectId userId, Model model) {
		User user = userService.getCurrentUser();
		model.addAttribute("currentuser", user);
        // Lấy thông tin người nhận từ userId
        User recipient = userService.getUserById(userId.toString());
		System.out.println(recipient);	
        if (recipient == null) {
            return "redirect:/error";
        }
        model.addAttribute("recipient", recipient);

        return "user/messenger";
    }
    @MessageMapping("/sendMessage")
    public void handleMessage(@Payload Message message) {
        // Lưu tin nhắn vào cơ sở dữ liệu
		if (message.getSender() == null || message.getReceiver() == null) {
			System.err.println("Sender or receiver is null.");
			return;  // Dừng lại nếu sender hoặc receiver là null
		}
        Message savedMessage = messageService.sendMessage(
                message.getSender().getUserID().toString(),
				message.getReceiver().getUserID().toString(),
                message.getContent()
        );

        // Gửi tin nhắn tới người nhận qua WebSocket
        messagingTemplate.convertAndSendToUser(
                message.getReceiver	().getUserID().toString(),
                "/queue/messages",
                savedMessage
        );
		System.out.println("Received message from: " + message.getSender().getUserID());
		System.out.println("Message content: " +message.getContent());
		System.out.println("Sending to user: " + message.getReceiver().getUserID());
    }
}
