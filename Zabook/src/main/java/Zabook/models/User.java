package Zabook.models;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
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
    private List<Integer> followers;
    private List<Integer> followings;
	
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

	public List<Integer> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Integer> followers) {
		this.followers = followers;
	}

	public List<Integer> getFollowings() {
		return followings;
	}

	public void setFollowings(List<Integer> followings) {
		this.followings = followings;
	}

    public User(ObjectId userID, String firstName, String lastName, String gender, String birthDay, String address,
			String email, String password, String bio, List<Integer> followers, List<Integer> followings) {
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
		this.followers = followers;
		this.followings = followings;
	}
}
