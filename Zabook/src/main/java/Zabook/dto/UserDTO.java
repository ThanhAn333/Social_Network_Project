package Zabook.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class UserDTO {
	@Id
	private ObjectId id;
	private String fullName;
	private String avatar;
	private int mutualFriends;
	private String friendshipStatus;

	public UserDTO(ObjectId id, String fullName, String avatar, int mutualFriends, String friendshipStatus) {
		this.id = id;
		this.fullName = fullName;
		this.avatar = avatar;
		this.mutualFriends = mutualFriends;
		this.friendshipStatus = friendshipStatus;
	}

	// Getters và Setters
	@Override
	public String toString() {
		return "UserDTO{" + "userID='" + id + '\'' + ", fullName='" + fullName + '\'' + ", avatar='" + avatar + '\''
				+ ", mutualFriends=" + mutualFriends + ", friendshipStatus='" + friendshipStatus + '\'' + '}';
	}

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

	public String getFriendshipStatus() {
		return friendshipStatus;
	}

	public void setFriendshipStatus(String friendshipStatus) {
		this.friendshipStatus = friendshipStatus;
	}

}
