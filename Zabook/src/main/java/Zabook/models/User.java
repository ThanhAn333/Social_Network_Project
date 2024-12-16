package Zabook.models;

import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {

    @Id
    private ObjectId id;

    private String firstName;
    private String lastName;
    private String gender;
    private String birthDay;
    private String address;
    private String email;
    private String password;
    private String bio;
    private String  avatar;


    @DBRef
    private String page;

    @DBRef
    private List<Video> video;

    @DBRef
    private List<Image> image;

    private String role;
    private String phone;

    @DBRef
    private List<FriendShip> friendships;  // Danh sách quan hệ bạn bè (thay vì danh sách bạn bè trực tiếp)

    private boolean accounNonLocked;

    private boolean enabled = false;

    private String verificationCode;

    // Các getter và setter
    public ObjectId getUserID() {
        return id;
    }

    public void setUserID(ObjectId userID) {
        this.id = userID;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
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

    public List<FriendShip> getFriendships() {
        return friendships;
    }

    public void setFriendships(List<FriendShip> friendships) {
        this.friendships = friendships;
    }

    public boolean isAccounNonLocked() {
        return accounNonLocked;
    }

    public void setAccounNonLocked(boolean accounNonLocked) {
        this.accounNonLocked = accounNonLocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    // Constructor
    public User(ObjectId userID, String firstName, String lastName, String gender, String birthDay, String address,
                String email, String password, String bio, String avatar, String page, List<Video> video, List<Image> image,
                String role, String phone, List<FriendShip> friendships) {
        this.id = userID;
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
        this.friendships = friendships;
    }

    public User() {
    }
    
    public User(ObjectId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
