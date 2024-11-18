package Zabook.models;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Posts")
public class Post {
	@Id
    private String id;
    private ObjectId userId;
    private String content;
    private LocalDateTime createdAt;
    private String image;

    // Constructors, Getters, Setters
    public Post() {
        this.createdAt = LocalDateTime.now();
    }

    
    
    public String getImage() {
		return image;
	}



	public void setImage(String image) {
		this.image = image;
	}



	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public ObjectId getUserId() {
		return userId;
	}


	public void setUserId(ObjectId userId) {
		this.userId = userId;
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


	public Post(ObjectId userId, String content,String image) {
        this.userId = userId;
        this.content = content;
        this.image=image;
        this.createdAt = LocalDateTime.now();
    }
	public Post(ObjectId userId, String content) {
        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
