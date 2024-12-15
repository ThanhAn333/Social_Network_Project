package Zabook.models;

import java.time.LocalDateTime;
import java.util.List;
import java.time.format.DateTimeFormatter;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "Messages")
public class Message {
		@Id
		private ObjectId messageId;
		
		private String content; // Nội dung tin nhắn
		
		private LocalDateTime timestamp; // Thời gian gửi
		
		private boolean isRead; // Trạng thái đọc
		@DBRef
		private User sender; // Người gửi
		@DBRef
		private User receiver; // Người nhận
		@DBRef(lazy = false)
		private List<Image> image;
		@DBRef(lazy = false)
		private List<Video> video;
	
		public enum MessageStatus {
			SENT,
			DELIVERED, 
			READ
		}
	

		public Message() {
		}
		private MessageStatus status = MessageStatus.SENT;
	
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
	
		public LocalDateTime getTimestamp() {
			return timestamp;
		}
	
		public void setTimestamp(LocalDateTime timestamp) {
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
	
		public List<Image> getImage() {
			return image;
		}
	
		public void setImage(List<Image> image) {
			this.image = image;
		}
	
		public List<Video> getVideo() {
			return video;
		}
	
		public void setVideo(List<Video> video) {
			this.video = video;
		}
	
		public MessageStatus getStatus() {
			return status;
		}
	
		public void setStatus(MessageStatus status) {
			this.status = status;
		}
	
		public Message(ObjectId messageId, String content, LocalDateTime timestamp, boolean isRead, User sender,
				User receiver, List<Image> image, List<Video> video, MessageStatus status) {
			super();
			this.messageId = messageId;
			this.content = content;
			this.timestamp = timestamp;
			this.isRead = isRead;
			this.sender = sender;
			this.receiver = receiver;
			this.image = image;
			this.video = video;
			this.status = status;
		}
		// Format thời gian kiểu Messenger
		public String getFormattedTime() {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime messageTime = this.timestamp;
			
			// Nếu là ngày hôm nay
			if (messageTime.toLocalDate().equals(now.toLocalDate())) {
				return messageTime.format(DateTimeFormatter.ofPattern("HH:mm"));
			}
			
			// Nếu là tuần này
			if (messageTime.isAfter(now.minusDays(7))) {
				return messageTime.format(DateTimeFormatter.ofPattern("EEE -HH:mm"));
			}
			
			// Nếu là năm nay
			if (messageTime.getYear() == now.getYear()) {
				return messageTime.format(DateTimeFormatter.ofPattern("dd/MM - HH:mm"));
			}
			
			// Nếu là năm trước
			return messageTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"));
		}
}
