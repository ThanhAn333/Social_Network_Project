package Zabook.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import Zabook.models.Message;
import Zabook.models.User;
import Zabook.repository.MessageRepository;
import Zabook.repository.UserRepository;
import Zabook.services.IMessageService;
@Service
public class MessageService implements IMessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserService userService;

	@Autowired
	UserRepository userRepo;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Override
	public Message saveMessage(Message message) {
		message.setTimestamp(LocalDateTime.now());
		return messageRepository.save(message);
	}

	@Override
	public void deleteMessage(ObjectId messageId) {
		messageRepository.deleteById(messageId);
	}
	
	@Override
	public List<Message> getConversation(ObjectId user1Id, ObjectId user2Id) {
		String user1ID = user1Id.toString();
		String user2ID = user2Id.toString();
		User user1 = userService.getUserById(user1ID);
		User user2 = userService.getUserById(user2ID);
		
		// Lấy tin nhắn và sắp xếp theo thời gian
		List<Message> messages = messageRepository.findConversation(user1, user2);
		messages.sort((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()));
		
		return messages;
	}

	@Override
	public void markAsDelivered(String messageId) {
		Message message = messageRepository.findById(new ObjectId(messageId))
				.orElseThrow(() -> new RuntimeException("Message not found"));
		message.setStatus(Message.MessageStatus.DELIVERED);
		messageRepository.save(message);
	}

	@Override
	public void markAsRead(String messageId) {
		Message message = messageRepository.findById(new ObjectId(messageId))
				.orElseThrow(() -> new RuntimeException("Message not found"));
		message.setStatus(Message.MessageStatus.READ);
		messageRepository.save(message);

        messagingTemplate.convertAndSendToUser(
            message.getSender().getUserID().toString(),
            "/queue/read-receipts",
            messageId
        );
	}

	@Override
	public List<User> getRecentChats(ObjectId userId) {
		User currentUser = userService.getUserById(userId.toString());
		List<Message> recentMessages = messageRepository.findMessagesByUser(currentUser);
		
		// Lọc ra danh sách unique users đã nhắn tin
		return recentMessages.stream()
			.map(message -> message.getSender().equals(currentUser) 
				? message.getReceiver() 
				: message.getSender())
			.distinct()
			.collect(Collectors.toList());
	}

	@Override
	public Message getLatestMessage(ObjectId user1Id, ObjectId user2Id) {
		User user1 = userService.getUserById(user1Id.toString());
		User user2 = userService.getUserById(user2Id.toString());
		return messageRepository.findFirstLatestMessageBetweenUsers(user1, user2);
	}


	@Override
	public Message sendMessage(String senderId, String recipientId, String content) {
        Message message = new Message();
        message.setSender(new User(new ObjectId(senderId)));
        message.setReceiver(new User(new ObjectId(recipientId)));
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);  

        return messageRepository.save(message);
    }
	@Override
	public List<Message> getMessagesBetweenUsers(String senderId, String recipientId) {
        // Lấy đối tượng User từ ID
		ObjectId sendID = new ObjectId(senderId);
		ObjectId recipID = new ObjectId(recipientId);
        User sender = userRepo.findById(sendID).orElse(null);
        User receiver = userRepo.findById(recipID).orElse(null);

        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Người gửi hoặc người nhận không tồn tại");
        }

        return messageRepository.findBySenderAndReceiverOrSenderAndReceiverOrderByTimestamp(
                sender, receiver, receiver, sender
        );
    }
	@Override
	public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByTimestampDesc();
    }
	
	

}
