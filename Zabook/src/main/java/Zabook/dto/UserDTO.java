package Zabook.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import Zabook.dto.FriendshipStatus;

public class UserDTO {
	@Id
	@JsonSerialize(using = ToStringSerializer.class) // Chuyển ObjectId thành chuỗi
	private ObjectId id;
	private String fullName;
	private String avatar;
	private int mutualFriends;
	
	private FriendshipStatus friendshipStatus;  // Sử dụng enum FriendshipStatus
	private String friendshipId;              // ID của mối quan hệ
    private boolean isSender;

	
	public UserDTO(ObjectId id, String fullName, String avatar, int mutualFriends, FriendshipStatus friendshipStatus, String friendshipId, boolean isSender) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.avatar = avatar;
		this.mutualFriends = mutualFriends;
		this.friendshipStatus = friendshipStatus;
		this.friendshipId = friendshipId;
	    this.isSender = isSender;
	}

	// Getters và Setters
	public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getMutualFriends() {
        return mutualFriends;
    }

    public void setMutualFriends(int mutualFriends) {
        this.mutualFriends = mutualFriends;
    }

    public FriendshipStatus getFriendshipStatus() {
        return friendshipStatus;
    }

    public void setFriendshipStatus(FriendshipStatus friendshipStatus) {
        this.friendshipStatus = friendshipStatus;
    }
    
    public String getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(String friendshipId) {
        this.friendshipId = friendshipId;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", mutualFriends=" + mutualFriends +
                ", friendshipStatus=" + friendshipStatus +
                ", friendshipId='" + friendshipId + '\'' +
                ", isSender=" + isSender +
                '}';
    }
}

