package Zabook.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import Zabook.models.Message;
import Zabook.models.User;

@Repository
public interface MessageRepository extends MongoRepository<Message, ObjectId>{
    // Lấy toàn bộ cuộc hội thoại giữa 2 người dùng, sắp xếp theo thời gian
    @Query(value = "{'$or': [{'sender': ?0, 'receiver': ?1}, {'sender': ?1, 'receiver': ?0}]}", 
           sort = "{ 'timestamp': -1 }",
           fields = "{'messageId': 1, 'content': 1, 'timestamp': 1, 'isRead': 1, 'sender': 1, 'receiver': 1, 'image': 1, 'video': 1, 'status': 1}")
    List<Message> findConversation(User user1, User user2);
    // Lấy tin nhắn gần nhất giữa 2 người đã từng nhắn tin
    @Query(value = "{$and: [" +
                   "  {$or: [{'sender': ?0}, {'receiver': ?0}]}, " +
                   "  {$or: [{'sender': ?1}, {'receiver': ?1}]} " +
                   "]}", 
           sort = "{ 'timestamp': -1 }")
    Message findFirstLatestMessageBetweenUsers(User user1, User user2);
    // Lấy danh sách người nhắn tin
    @Query(value = "{$or: [{'sender': ?0}, {'receiver': ?0}]}", 
           sort = "{'timestamp': -1}")
    List<Message> findMessagesByUser(User user);

    // Truy vấn lấy tất cả tin nhắn giữa người gửi và người nhận
    List<Message> findBySenderAndReceiverOrSenderAndReceiverOrderByTimestamp(
            User sender1, User receiver1,
            User sender2, User receiver2
    );
    List<Message> findAllByOrderByTimestampDesc();
}
