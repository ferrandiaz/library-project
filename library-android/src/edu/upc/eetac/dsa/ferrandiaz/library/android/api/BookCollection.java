package edu.upc.eetac.dsa.ferrandiaz.library.android.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookCollection {

	private Map<String, Link> links = new HashMap<>();
	private List<Book> books;
	private long newestTimestamp;
	private long oldestTimestamp;

	public BookCollection() {
		super();
		books = new ArrayList<>();
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public void addbook(Book book) {
		books.add(book);

	}

	public long getOldestTimestamp() {
		return oldestTimestamp;
	}

	public void setOldestTimestamp(long oldestTimestamp) {
		this.oldestTimestamp = oldestTimestamp;
	}

	public long getNewestTimestamp() {
		return newestTimestamp;
	}

	public void setNewestTimestamp(long newestTimestamp) {
		this.newestTimestamp = newestTimestamp;
	}

	public Map<String, Link> getLinks() {
		return links;
	}

	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}
}
