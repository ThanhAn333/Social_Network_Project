package Zabook.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Zabook.models.ChatMessage;
import Zabook.models.Message;
import Zabook.models.User;
import Zabook.services.IMessageService;
import Zabook.services.IUserService;

import java.util.List;

@Controller
@RequestMapping("/user/messenger")
@CrossOrigin(origins = "*")
public class MessageController {
	@Autowired
	private IMessageService messageService;
	@Autowired
	private IUserService userService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@GetMapping("")
	public String message(Model model) {
		User user = userService.getCurrentUser();
		model.addAttribute("user",user);
		return "/user/test";
	}

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		return chatMessage;
	}

	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		// Add username in web socket session
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}

}
