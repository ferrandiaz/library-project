package edu.upc.eetac.dsa.ferrandiaz.library.android.api;

import java.util.HashMap;
import java.util.Map;


public class Book {

	private Map<String, Link> links = new HashMap<>();
	private String bookid;
	private String title;
	private String author;
	private String lenguage;
	private String editorial;
	private String ed_date;
	private String print_date;
	private long lastModified;
	
	
	public String getBookid() {
		return bookid;
	}

	public void setBookid(String bookid) {
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

	public Map<String, Link> getLinks() {
		return links;
	}

	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}

}
