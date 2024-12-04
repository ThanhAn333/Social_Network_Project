package Zabook.services;

import java.util.List;
import org.bson.types.ObjectId;
import Zabook.models.Message;

public interface IMessageService {

	Message sendMessage(ObjectId senderId, ObjectId receiverId, String content, ObjectId attachmentId);
	
	List<Message> getMessagesBySender(ObjectId senderId);
	
	List<Message> getMessagesByReceiver(ObjectId receiverId);
	
	List<Message> getMessagesBetweenUsers(ObjectId user1Id, ObjectId user2Id);
	
	List<Message> getRecentMessages(ObjectId userId);
}
