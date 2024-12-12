package Zabook.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "Videos")
public class Video {

	@Id
	private ObjectId id;
	private String linkVideo;
	private String typeVideo;
	
	
	public ObjectId getVideoID() {
		return id;
	}

	public void setVideoID(ObjectId videoID) {
		this.id = videoID;
	}

	public String getLinkVideo() {
		return linkVideo;
	}

	public void setLinkVideo(String linkVideo) {
		this.linkVideo = linkVideo;
	}

	public String getTypeVideo() {
		return typeVideo;
	}

	public void setTypeVideo(String typeVideo) {
		this.typeVideo = typeVideo;
	}
	public Video(ObjectId videoID, String linkVideo, String typeVideo) {
		super();
		this.id = videoID;
		this.linkVideo = linkVideo;
		this.typeVideo = typeVideo;
	}

	public Video() {
		super();
		}

}
