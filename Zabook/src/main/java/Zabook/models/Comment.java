package Zabook.models;

import java.time.LocalDate;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "Comments")
public class Comment {

	@Id
	private ObjectId id;
	private String content;
	private LocalDate createTime;

	private double rate;

	@DBRef
	private Post post;

	@DBRef
	private User userComment;
	
	

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



	public LocalDate getCreateTime() {
		return createTime;
	}



	public void setCreateTime(LocalDate createTime) {
		this.createTime = createTime;
	}



	public double getRate() {
		return rate;
	}



	public void setRate(double rate) {
		this.rate = rate;
	}



	public Post getPost() {
		return post;
	}



	public void setPost(Post post) {
		this.post = post;
	}



	public User getUserComment() {
		return userComment;
	}



	public void setUserComment(User userComment) {
		this.userComment = userComment;
	}



	public Comment() {
		this.createTime = LocalDate.now();
	}

}
