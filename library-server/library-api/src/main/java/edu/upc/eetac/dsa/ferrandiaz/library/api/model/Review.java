package edu.upc.eetac.dsa.ferrandiaz.library.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.ferrandiaz.library.api.BookResource;
import edu.upc.eetac.dsa.ferrandiaz.library.api.MediaType;
import edu.upc.eetac.dsa.ferrandiaz.library.api.ReviewResource;




public class Review {
	@InjectLinks({
		@InjectLink(resource = ReviewResource.class, style = Style.ABSOLUTE, rel = "self edit", title = "Review", type = MediaType.LIBRARY_API_REVIEW, method = "getReview", bindings = @Binding(name = "reviewid", value = "${instance.reviewid}")) })
	
	private List<Link> links;
	private int reviewid;
	private String username;
	private int bookid;
	private String name;
	private String review;
	private long lastModified;
	public int getReviewid() {
		return reviewid;
	}
	public void setReviewid(int reviewid) {
		this.reviewid = reviewid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getBookid() {
		return bookid;
	}
	public void setBookid(int bookid) {
		this.bookid = bookid;
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
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
