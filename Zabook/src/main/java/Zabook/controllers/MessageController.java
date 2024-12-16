package Zabook.controllers;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
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
    public String getChatPage(@PathVariable String userId, Model model) {
		User user = userService.getCurrentUser();
		ObjectId userID= new ObjectId(userId);
        // Lấy thông tin người nhận từ userId
        User recipient = userService.getUserById(userId);
		System.out.println(recipient);	
        if (recipient == null) {
            return "redirect:/error";
        }
        List<Message> messages=messageService.getMessagesBetweenUsers(userId,user.getUserID().toString());
        model.addAttribute("messages",messages);
        model.addAttribute("currentuser", user);
        model.addAttribute("recipient", recipient);
        
        for(Message mess : messages) {
        	System.out.print(mess.getContent());
        	System.out.print(mess.getSender().getUserID());
        }
        if (messages == null || messages.isEmpty()) {
            System.out.println("Danh sách tin nhắn rỗng hoặc chưa được khởi tạo!");
        } else {
            System.out.println("Danh sách tin nhắn có dữ liệu!");
        }

        return "user/messenger";
    }
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public void handleMessage(@Payload Message message) {
        // Lưu tin nhắn vào cơ sở dữ liệu
		if (message.getSenderId() == null ) {
			System.err.println("Sender or receiver is null.");
			return;  // Dừng lại nếu sender hoặc receiver là null
		}
		System.out.print("ok");
        Message savedMessage = messageService.sendMessage(
                message.getSenderId(),
				message.getRecipientId(),
                message.getContent()
        );
        System.out.print("ok2");
        // Gửi tin nhắn tới người nhận qua WebSocket
        messagingTemplate.convertAndSendToUser(
                message.getRecipientId(),
                "/queue/messages",
                savedMessage
        );
		//System.out.println("Received message from: " + message.getSender().getUserID());
		//System.out.println("Message content: " +message.getContent());
		//System.out.println("Sending to user: " + message.getReceiver().getUserID());
    }

}
