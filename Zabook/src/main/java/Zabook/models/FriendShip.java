package Zabook.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Friendships")
public class FriendShip {

    @Id
    private ObjectId friendshipID;

    @DBRef
    private User user1;  // Người dùng thứ nhất trong mối quan hệ

    @DBRef
    private User user2;  // Người dùng thứ hai trong mối quan hệ

    private String status;  // Trạng thái của mối quan hệ (ví dụ: "pending", "accepted", "rejected")

    private Date createdAt; // Thêm trường thời gian

    public ObjectId getFriendshipID() {
        return friendshipID;
    }

    public void setFriendshipID(ObjectId friendshipID) {
        this.friendshipID = friendshipID;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // Constructor mặc định
    public FriendShip() {}

    // Constructor với các tham số
    public FriendShip(User user1, User user2, String status) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
        this.createdAt = new Date(); // Tự động set thời gian khi tạo
    }
}
