package edu.upc.eetac.dsa.ferrandiaz.library.android.api;


import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


public class ReviewCollection {
	private Map<String, Link> links = new HashMap<>();
	private List<Review> reviews;
	private long newestTimestamp;
	private long oldestTimestamp;

	public ReviewCollection() {
		super();
		reviews = new ArrayList<>();
	}

		public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public long getNewestTimestamp() {
		return newestTimestamp;
	}

	public void setNewestTimestamp(long newestTimestamp) {
		this.newestTimestamp = newestTimestamp;
	}

	public long getOldestTimestamp() {
		return oldestTimestamp;
	}

	public void setOldestTimestamp(long oldestTimestamp) {
		this.oldestTimestamp = oldestTimestamp;
	}

	public void addReview(Review review) {
		reviews.add(review);

	}

	public Map<String, Link> getLinks() {
		return links;
	}

	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}

}
