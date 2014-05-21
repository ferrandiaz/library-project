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

public class Book {

	@InjectLinks({
			@InjectLink(resource = ReviewResource.class, style = Style.ABSOLUTE, rel = "book-review", title = "Book Review", type = MediaType.LIBRARY_API_REVIEW_COLLECTION, method = "getReviews", bindings = @Binding(name = "bookid", value = "${instance.bookid}")),
			@InjectLink(resource = BookResource.class, style = Style.ABSOLUTE, rel = "books", title = "Latest books", type = MediaType.LIBRARY_API_BOOK_COLLECTION),
			@InjectLink(resource = BookResource.class, style = Style.ABSOLUTE, rel = "self edit", title = "Book", type = MediaType.LIBRARY_API_BOOK, method = "getBook", bindings = @Binding(name = "bookid", value = "${instance.bookid}")) })
	private List<Link> links;
	private int bookid;
	private String title;
	private String author;
	private String lenguage;
	private String editorial;
	private String ed_date;
	private String print_date;
	private long lastModified;
	
	
	public int getBookid() {
		return bookid;
	}

	public void setBookid(int bookid) {
		this.bookid = bookid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLenguage() {
		return lenguage;
	}

	public void setLenguage(String lenguage) {
		this.lenguage = lenguage;
	}

	public String getEditorial() {
		return editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void setEd_date(String ed_date) {
		this.ed_date = ed_date;
	}

	public void setPrint_date(String print_date) {
		this.print_date = print_date;
	}

	public String getEd_date() {
		return ed_date;
	}

	public String getPrint_date() {
		return print_date;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

}
