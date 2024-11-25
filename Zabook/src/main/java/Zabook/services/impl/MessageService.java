package Zabook.services.impl;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import Zabook.models.Image;
import Zabook.models.User;
import Zabook.models.Message;
import Zabook.repository.MessageRepository;
import Zabook.repository.UserRepository;
import Zabook.services.IMessageService;

public class MessageService implements IMessageService {
	
	@Autowired
	UserRepository userRepo;
	
	private MessageRepository messageRepository;
	@Override
	public Message sendMessage(ObjectId senderId, ObjectId receiverId, String content, ObjectId attachmentId) {
		// Tìm người gửi (sender) từ database
	    User sender = userRepo.findById(senderId)
	            .orElseThrow(() -> new RuntimeException("Sender not found"));

	    // Tìm người nhận (receiver) từ database
	    User receiver = userRepo.findById(receiverId)
	            .orElseThrow(() -> new RuntimeException("Receiver not found"));

	    // Tìm hình ảnh đính kèm (nếu có)
	    Image attachment = null;
//	    if (attachmentId != null) {
//	        attachment = imageRepository.findById(attachmentId)
//	                .orElseThrow(() -> new RuntimeException("Attachment not found"));
//	    }

	    // Tạo đối tượng Message mới
	    Message message = new Message();
	    message.setMessageId(new ObjectId()); // Tự động tạo ID cho tin nhắn
	    message.setSender(sender); // Gán người gửi
	    message.setReceiver(receiver); // Gán người nhận
	    message.setContent(content); // Gán nội dung tin nhắn
	    message.setTimestamp(new Date(0).toString()); // Gán timestamp (có thể dùng định dạng ISO 8601)
	    message.setRead(false); // Tin nhắn mới gửi mặc định chưa được đọc
	    message.setAttachment(attachment); // Gán hình ảnh đính kèm nếu có

	    // Lưu tin nhắn vào database và trả về kết quả
	    return messageRepository.save(message);
	}
	
	@Override
	public List<Message> getMessagesBySender(ObjectId senderID) {
		return messageRepository.findBySenderId(senderID);
	}

	@Override
	public List<Message> getMessagesByReceiver(ObjectId receiverId) {
		return messageRepository.findBySenderId(receiverId);
	}

	// Lấy danh sách tin nhắn giữa hai người
	@Override
	public List<Message> getMessagesBetweenUsers(ObjectId user1Id, ObjectId user2Id) {
		User user1 = userRepo.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User1 not found"));
        User user2 = userRepo.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User2 not found"));

        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(user1, user2, user2, user1);
	}
	
	// Lấy danh sách người đã nhắn tin và tin nhắn gần nhất
	@Override
	public List<Message> getRecentMessages(ObjectId userId) {
		User user = userRepo.findById((userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Message> messages = messageRepository.findLatestMessagesForUser(user);
        // Sắp xếp theo timestamp giảm dần
        return messages.stream()
                .sorted(Comparator.comparing(Message::getTimestamp).reversed())
                .collect(Collectors.toList());
    }
}

	



