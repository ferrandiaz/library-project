package edu.upc.eetac.dsa.ferrandiaz.library.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;


import edu.upc.eetac.dsa.ferrandiaz.library.api.MediaType;
import edu.upc.eetac.dsa.ferrandiaz.library.api.ReviewResource;
import edu.upc.eetac.dsa.ferrandiaz.library.api.model.Review;

public class ReviewCollection {
	
	private List<Link> links;
	private List<Review> reviews;
	private long newestTimestamp;
	private long oldestTimestamp;

	public ReviewCollection() {
		super();
		reviews = new ArrayList<>();
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
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

}
