package Zabook.models;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Posts")
public class Post {
	@Id
	private ObjectId id;

	@DBRef
	private User user;

	private String content;
	
	private LocalDateTime createdAt;
	
	@DBRef
	private List<Image> image;
	
	@DBRef
	private List<Video> video;
	
	@DBRef
	private List<Comment> comment;
	
	
	
	private ObjectId originalPostId; // Nếu là bài chia sẻ, lưu ID của bài gốc
    private boolean isShared;      // Đánh dấu bài viết là bài chia sẻ hay không
    private int shareCount;
	
	

	public Post() {
		this.createdAt = LocalDateTime.now();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Image> getImage() {
		return image;
	}

	public void setImage(List<Image> image) {
		this.image = image;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<Video> getVideo() {
		return video;
	}

	public void setVideo(List<Video> video) {
		this.video = video;
	}

	
	
	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	public Post(ObjectId id, User user, String content, LocalDateTime createdAt, List<Image> image, List<Video> video) {
		super();
		this.id = id;
		this.user = user;
		this.content = content;
		this.createdAt = createdAt;
		this.image = image;
		this.video = video;
	}

	
}
