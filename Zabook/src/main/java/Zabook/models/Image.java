package Zabook.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "Images")
public class Image {

	@Id
	private Object imageID;

	private String linkImage;
	private String typeImage;


	public Object getImageID() {
		return imageID;
	}
	public void setImageID(Object imageID) {
		this.imageID = imageID;
	}
	public String getLinkImage() {
		return linkImage;
	}
	public void setLinkImage(String linkImage) {
		this.linkImage = linkImage;
	}
	public String getTypeImage() {
		return typeImage;
	}
	public void setTypeImage(String typeImage) {
		this.typeImage = typeImage;
	}
	
	public Image(Object imageID, String linkImage, String typeImage) {
		super();
		this.imageID = imageID;
		this.linkImage = linkImage;
		this.typeImage = typeImage;
	}
	
	public Image() {
		super();
	}
}
