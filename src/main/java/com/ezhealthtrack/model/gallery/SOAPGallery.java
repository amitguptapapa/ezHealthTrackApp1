package com.ezhealthtrack.model.gallery;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SOAPGallery {

	@SerializedName("gallery_count")
	@Expose
	private String galleryCount;
	@SerializedName("gallery_id")
	@Expose
	private String galleryId;
	@Expose
	private List<SOAPPhoto> photos = new ArrayList<SOAPPhoto>();

	public List<SOAPPhoto> getPhotos() {
		return photos;
	}

	public void setPhotos(List<SOAPPhoto> photos) {
		this.photos = photos;
	}

	public String getGalleryCount() {
		return galleryCount;
	}

	public void setGalleryCount(String galleryCount) {
		this.galleryCount = galleryCount;
	}

	public String getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(String galleryId) {
		this.galleryId = galleryId;
	}
}
