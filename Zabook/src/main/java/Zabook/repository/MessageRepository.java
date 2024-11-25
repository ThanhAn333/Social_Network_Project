package Zabook.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import Zabook.models.Message;
import Zabook.models.User;

public interface MessageRepository extends MongoRepository<Message, ObjectId> {
    
	List<Message> findBySenderId(ObjectId sender);
    
	List<Message> findByReceiverId(ObjectId receiver);
	
 // Lấy danh sách tin nhắn giữa 2 người
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(User senderId, User receiverId, User receiverId2, User senderId2
    );
    // Lấy danh sách người đã nhắn và tin nhắn gần nhất
    @Query("{$or: [{'sender': ?0}, {'receiver': ?0}], $sort: {'timestamp': -1}}")
    List<Message> findLatestMessagesForUser(User user);
}
