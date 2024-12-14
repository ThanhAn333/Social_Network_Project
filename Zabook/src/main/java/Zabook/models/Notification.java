package Zabook.models;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "notifications")
public class Notification {
    @Id
    private ObjectId id;
    
    private NotificationType type; // LIKE, COMMENT, SHARE, FRIEND_REQUEST
    private String senderName;     // Tên người gửi thông báo
    private String content;        // Nội dung thông báo
    private String targetId;       // ID của bài viết hoặc lời mời kết bạn
    private LocalDateTime time;

    public Notification(NotificationType type, String senderName, String content, String targetId) {
        this.type = type;
        this.senderName = senderName;
        this.content = content;
        this.targetId = targetId;
        this.time = LocalDateTime.now();
    }

    // Getters và Setters
    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
