package Zabook.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "Videos")
public class Video {

	@Id
	private Object videoID;
	private String linkVideo;
	private String typeVideo;
	
	
	public Object getVideoID() {
		return videoID;
	}

	public void setVideoID(Object videoID) {
		this.videoID = videoID;
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
	public Video(Object videoID, String linkVideo, String typeVideo) {
		super();
		this.videoID = videoID;
		this.linkVideo = linkVideo;
		this.typeVideo = typeVideo;
	}

	public Video() {
		super();
		}

}
