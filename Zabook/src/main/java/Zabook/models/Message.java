package Zabook.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "Messages")
public class Message {
	 @Id
	    private ObjectId messageId;
	    private String content; // Nội dung tin nhắn
	    private String timestamp; // Thời gian gửi
	    private boolean isRead; // Trạng thái đọc
	    @DBRef
	    private User sender; // Người gửi
	    @DBRef
	    private User receiver; // Người nhận
	    @DBRef
	    private Image attachment; // Hình ảnh đính kèm (nếu có)

	    public ObjectId getMessageId() {
	        return messageId;
	    }
	    public void setMessageId(ObjectId messageId) {
	        this.messageId = messageId;
	    }

	    public String getContent() {
	        return content;
	    }
	    public void setContent(String content) {
	        this.content = content;
	    }

	    public String getTimestamp() {
	        return timestamp;
	    }
	    public void setTimestamp(String timestamp) {
	        this.timestamp = timestamp;
	    }

	    public boolean isRead() {
	        return isRead;
	    }
	    public void setRead(boolean isRead) {
	        this.isRead = isRead;
	    }

	    public User getSender() {
	        return sender;
	    }
	    public void setSender(User sender) {
	        this.sender = sender;
	    }

	    public User getReceiver() {
	        return receiver;
	    }
	    public void setReceiver(User receiver) {
	        this.receiver = receiver;
	    }

	    public Image getAttachment() {
	        return attachment;
	    }
	    public void setAttachment(Image attachment) {
	        this.attachment = attachment;
	    }

	}
