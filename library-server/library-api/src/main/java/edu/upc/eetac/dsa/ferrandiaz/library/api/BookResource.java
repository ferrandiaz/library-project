package edu.upc.eetac.dsa.ferrandiaz.library.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.ferrandiaz.library.api.DataSourceSPA;
import edu.upc.eetac.dsa.ferrandiaz.library.api.MediaType;
import edu.upc.eetac.dsa.ferrandiaz.library.api.model.Book;
import edu.upc.eetac.dsa.ferrandiaz.library.api.model.BookCollection;

@Path("/books")
public class BookResource {
	@Context
	private SecurityContext security;
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@GET
	@Produces(MediaType.LIBRARY_API_BOOK_COLLECTION)
	public BookCollection getBooks(@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after) {
		BookCollection books = new BookCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			stmt = conn.prepareStatement(buildGetBooksQuery(updateFromLast));
			if (updateFromLast) {
				stmt.setTimestamp(1, new Timestamp(after));
			} else {
				if (before > 0)
					stmt.setTimestamp(1, new Timestamp(before));
				else
				stmt.setTimestamp(1, null);
				length = (length <= 0) ? 20 : length;
				stmt.setInt(2, length);
			}
			ResultSet rs = stmt.executeQuery();
			boolean first = true;
			long oldestTimestamp = 0;
			while (rs.next()) {
				Book book = new Book();
				book.setBookid(rs.getInt("bookid"));
				book.setAuthor(rs.getString("author"));
				book.setTitle(rs.getString("title"));
				book.setLenguage(rs.getString("lenguage"));
				book.setEditorial(rs.getString("editorial"));
				book.setEd_date(rs.getString("ed_date"));
				book.setPrint_date(rs.getString("print_date"));
				if (first) {
					first = false;
					books.setNewestTimestamp(book.getLastModified());
				}
				books.addbook(book);
			}
			books.setOldestTimestamp(oldestTimestamp);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return books;
	}

	private String buildGetBooksQuery (boolean updateFromLast) {
		if (updateFromLast)
			return "select * from books where s.last_modified > ? order by ed_date desc";
		else
			return "select * from books where last_modified < ifnull(?, now())  order by ed_date desc limit ?";
	
	}

	@GET
	@Path("/{bookid}")
	@Produces(MediaType.LIBRARY_API_BOOK)
	public Book getBook(@PathParam("bookid") int bookid) {
		// Create CacheControl

		Book book = new Book();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetBookByIdQuery());
			stmt.setInt(1, bookid);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				book.setBookid(rs.getInt("bookid"));
				book.setAuthor(rs.getString("author"));
				book.setTitle(rs.getString("title"));
				book.setLenguage(rs.getString("lenguage"));
				book.setEditorial(rs.getString("editorial"));
				book.setEd_date(rs.getString("ed_date"));
				book.setPrint_date(rs.getString("print_date"));

			} else {
				throw new NotFoundException("There's no book with bookid="
						+ bookid);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return book;
	}

	private String buildGetBookByIdQuery() {

		return "select * from books where bookid = ?";
	}

	@GET
	@Path("/search")
	@Produces(MediaType.LIBRARY_API_BOOK_COLLECTION)
	public BookCollection getBooks(@QueryParam("author") String author, @QueryParam("title") String title) {

		BookCollection books = new BookCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetBookByAuthorAndTitle());
			if(author != null){
			stmt.setString(1, "%" + author + "%");
			}
			else{
				stmt.setString(1, "%%");
			}
			if(title != null){
				stmt.setString(2, "%" + title + "%");
			}
			else{
				stmt.setString(2, "%%");
			}
			

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Book book = new Book();
				book.setBookid(rs.getInt("bookid"));
				book.setAuthor(rs.getString("author"));
				book.setTitle(rs.getString("title"));
				book.setLenguage(rs.getString("lenguage"));
				book.setEditorial(rs.getString("editorial"));
				book.setEd_date(rs.getString("ed_date"));
				book.setPrint_date(rs.getString("print_date"));

				books.addbook(book);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return books;

	}

	private String buildGetBookByAuthorAndTitle() {

		return "select *from books where author like ? and title like ?";

	}
	

	@POST
	@Consumes(MediaType.LIBRARY_API_BOOK)
	@Produces(MediaType.LIBRARY_API_BOOK)
	public Book createBook(Book book) {
		validateBook(book);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		String sql = buildInsertBook();
		try {
			
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			//(title, author, lenguage, ed_date, print_date, editorial)
			stmt.setString(1, book.getTitle() );
			stmt.setString(2, book.getAuthor());
			stmt.setString(3, book.getLenguage());
			stmt.setString(4, book.getEd_date());
			stmt.setString(5, book.getPrint_date());
			stmt.setString(6, book.getEditorial());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int bookid = rs.getInt(1);
	 
				book = getBook(bookid);
			} else {
				// Something has failed...
			}
			
			
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return book;

	}
	
	private String buildInsertBook(){
		return "insert into books (title, author, lenguage, ed_date, print_date, editorial, last_modified) values(?, ?, ?, ?, ?, ?, now())";
	}
	
	@PUT
	@Path("/{bookid}")
	@Consumes(MediaType.LIBRARY_API_BOOK)
	@Produces(MediaType.LIBRARY_API_BOOK)
	public Book updateBook(Book book, @PathParam ("bookid") int bookid){
		validateUser();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		try {
			String sql = buildUpdateBook();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, book.getTitle() );
			stmt.setString(2, book.getAuthor());
			stmt.setString(3, book.getLenguage());
			stmt.setString(4, book.getEd_date());
			stmt.setString(5, book.getPrint_date());
			stmt.setString(6, book.getEditorial());
			stmt.setInt(7, bookid);
			stmt.executeUpdate();
			
			int rows = stmt.executeUpdate();
			if (rows == 1)
				book = getBook(bookid);
			else {
				throw new NotFoundException("There's no book with bookid="
						+ bookid);// Updating inexistent book
			}
		}catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
		return book;
	}
	

	private String buildUpdateBook() {
		return "update books set title=ifnull(?,title), author=ifnull(?, author), lenguage=ifnull(?,lenguage), ed_date=ifnull(?, ed_date), print_date=ifnull(?,print_date), editorial=ifnull(?, editorial), last_modified = now() where bookid = ?";
	}
	
	@DELETE
	@Path("/{bookid}")
	public void deleteBook(@PathParam ("bookid") int bookid){
		validateUser();
		Connection conn = null;
		
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	 
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			String sql = buildDeleteReviewByBookId();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, bookid);
			stmt.executeUpdate();
			
			String sql2 = buildDeleteBook(); 
			stmt2 = conn.prepareStatement(sql2);
			stmt2.setInt(1, bookid);
			int rows = stmt2.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no book with bookid="
						+ bookid);
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	private String buildDeleteBook() {
		
		return "delete from books where bookid = ?";
	}
	private String buildDeleteReviewByBookId(){
		return "delete from review where bookid = ?";
	}

	private void validateBook(Book book) {
		if (book.getAuthor() == null)
			throw new BadRequestException("Author can't be null");
		if (book.getEditorial() == null)
			throw new BadRequestException("Editorial can't be null");
		if (book.getTitle() == null)
			throw new BadRequestException("Title can't be null");
		if (book.getLenguage() == null)
			throw new BadRequestException("Lenguage can't be null");
		if (book.getEd_date() == null)
			throw new BadRequestException("Ed_date can't be null");
		if (book.getPrint_date() == null)
			throw new BadRequestException("Print_date can't be null");
	}
	
	public void validateUser(){
		if(!security.getUserPrincipal().getName().equals("admin")){
			throw new NotAllowedException("You can't do this");
		}
	}
}
