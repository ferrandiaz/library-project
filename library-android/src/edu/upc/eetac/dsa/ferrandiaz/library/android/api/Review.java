package edu.upc.eetac.dsa.ferrandiaz.library.android.api;

import java.util.HashMap;
import java.util.Map;

public class Review {

	private Map<String, Link> links = new HashMap<>();
	private String reviewid;
	private String username;
	private String bookid;
	private String name;
	private String review;
	private long lastModified;

	public String getReviewid() {
		return reviewid;
	}

	public void setReviewid(String reviewid) {
		this.reviewid = reviewid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBookid() {
		return bookid;
	}

	public void setBookid(String string) {
		this.bookid = string;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Link> getLinks() {
		return links;
	}

	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}

}
