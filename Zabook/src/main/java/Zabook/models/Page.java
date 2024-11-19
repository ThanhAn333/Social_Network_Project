package Zabook.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.List;

@Document(collection = "Pages")
public class Page {

    @Id
    private String id;
    private String image;
    private String pageName;
    
    @DBRef
    private User admin;
    private String introduce;
    private String contact;
    private String background;
    private String category;
    
    @DBRef
    private List<Post> posts;
    @DBRef
    private List<Image> images;

    @DBRef
    private List<Video> videos;

    @DBRef
    private List<User> followers;

    private Date createTime;

    private Boolean enabled;

    public Page() {
    }



    public Page(String id, String image, String pageName, User admin, String introduce, String contact,
			String background, String category, List<Post> posts, List<Image> images, List<Video> videos,
			List<User> followers, Date createTime, Boolean enabled) {
		super();
		this.id = id;
		this.image = image;
		this.pageName = pageName;
		this.admin = admin;
		this.introduce = introduce;
		this.contact = contact;
		this.background = background;
		this.category = category;
		this.posts = posts;
		this.images = images;
		this.videos = videos;
		this.followers = followers;
		this.createTime = createTime;
		this.enabled = enabled;
	}



	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }


    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}

