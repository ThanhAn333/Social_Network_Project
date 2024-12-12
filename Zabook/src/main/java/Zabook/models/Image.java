package Zabook.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "Images")
public class Image {

	@Id
	private ObjectId id;

	private String linkImage;
	private String typeImage;


	public Object getImageID() {
		return id;
	}
	public void setImageID(ObjectId imageId) {
		this.id = imageId;
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
	
	public Image(ObjectId imageID, String linkImage, String typeImage) {
		super();
		this.id = imageID;
		this.linkImage = linkImage;
		this.typeImage = typeImage;
	}
	
	public Image() {
		super();
	}
}