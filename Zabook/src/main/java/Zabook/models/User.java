package Zabook.models;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {

	@Id
	private ObjectId userID;
	private String firstName;
	private String lastName;
	private String gender;
	private String birthDay;
	private String address;
	private String email;
	private String password;
	private String bio;
	
	@DBRef
	private Image avatar;

	private String page;

	@DBRef
	private List<Video> video;

	@DBRef
	private List<Image> image;

	private String role;
	private String phone;

	private List<User> friend;
	private List<User> requestFriend;
	private List<User> following;

	public ObjectId getUserID() {
		return userID;
	}

	public void setUserID(ObjectId userID) {
		this.userID = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public Image getAvatar() {
		return avatar;
	}

	public void setAvatar(Image avatar) {
		this.avatar = avatar;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<User> getFriend() {
		return friend;
	}

	public void setFriend(List<User> friend) {
		this.friend = friend;
	}

	public List<User> getRequestFriend() {
		return requestFriend;
	}

	public void setRequestFriend(List<User> requestFriend) {
		this.requestFriend = requestFriend;
	}

	public List<User> getFollowing() {
		return following;
	}

	public void setFollowing(List<User> following) {
		this.following = following;
	}
	
	

	public List<Video> getVideo() {
		return video;
	}

	public void setVideo(List<Video> video) {
		this.video = video;
	}

	public List<Image> getImage() {
		return image;
	}

	public void setImage(List<Image> image) {
		this.image = image;
	}

	public User(ObjectId userID, String firstName, String lastName, String gender, String birthDay, String address,
			String email, String password, String bio,Image avatar, String page, List<Video> video, List<Image> image,
			String role, String phone, List<User> friend, List<User> requestFriend, List<User> following) {
		super();
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthDay = birthDay;
		this.address = address;
		this.email = email;
		this.password = password;
		this.bio = bio;
		this.avatar = avatar;
		this.page = page;
		this.video = video;
		this.image = image;
		this.role = role;
		this.phone = phone;
		this.friend = friend;
		this.requestFriend = requestFriend;
		this.following = following;
	}

	public User() {
		super();

	}

}
