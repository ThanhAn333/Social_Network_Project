package Zabook.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import Zabook.Until.TimeUntil;
import jakarta.persistence.Id;

@Document(collection = "Comments")
public class Comment {

	@Id
	private ObjectId id;
	private String content;
	private LocalDateTime createTime;

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



	public LocalDateTime getCreateTime() {
		return createTime;
	}



	public void setCreateTime(LocalDateTime createTime) {
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
		this.createTime = LocalDateTime.now();
	}
	public String getTimeAgo() {
	       return TimeUntil.timeAgo(createTime);
	    }

	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    Comment comment = (Comment) o;
	    return id.equals(comment.id); // So sánh dựa trên id
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id);
	}

}
