package Zabook.services;

import java.util.List;

import org.bson.types.ObjectId;

import Zabook.models.Message;
import Zabook.models.User;
public interface IMessageService {
    //Lưu tin nhắn
	Message saveMessage(Message message);
	//Xóa tin nhắn
    void deleteMessage(ObjectId messageId);
    //Lấy toàn bộ cuộc hội thoại giữa 2 người dùng
    List<Message> getConversation(ObjectId user1Id, ObjectId user2Id);
    //Đánh dấu tin nhắn đã được gửi đến người nhận
    void markAsDelivered(String messageId);
    //Đánh dấu tin nhắn đã được đọc
    void markAsRead(String messageId);
    // Lấy danh sách người nhắn tin gần đây với user
    List<User> getRecentChats(ObjectId userId);
    // Lấy tin nhắn gần nhất với mỗi người
    Message getLatestMessage(ObjectId user1Id, ObjectId user2Id);


    public Message sendMessage(String senderId, String recipientId, String content);
    
    public List<Message> getMessagesBetweenUsers(String senderId, String recipientId);
    
 // Lấy tin nhắn giữa hai người dùng
    public List<Message> getAllMessages();
}
