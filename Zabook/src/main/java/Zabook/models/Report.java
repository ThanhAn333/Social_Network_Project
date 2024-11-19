package Zabook.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "Reports")
public class Report {

    @Id
    private ObjectId id;
    
    private String contentReport;

    @DBRef
    private Post post;

    @DBRef
    private User user;

    @DBRef
    private Page page;

    @DBRef
    private Comment comment;

    public Report() {}

    public Report(String contentReport, Post post, User user, Page page, Comment comment) {
        this.contentReport = contentReport;
        this.post = post;
        this.user = user;
        this.page = page;
        this.comment = comment;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getContentReport() {
        return contentReport;
    }

    public void setContentReport(String contentReport) {
        this.contentReport = contentReport;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
