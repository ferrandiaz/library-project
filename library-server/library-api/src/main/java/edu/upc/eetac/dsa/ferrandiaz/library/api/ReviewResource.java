package edu.upc.eetac.dsa.ferrandiaz.library.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.ferrandiaz.library.api.MediaType;
import edu.upc.eetac.dsa.ferrandiaz.library.api.model.Review;
import edu.upc.eetac.dsa.ferrandiaz.library.api.model.ReviewCollection;

@Path("/books/{bookid}/review")
public class ReviewResource {
	@Context
	private SecurityContext security;
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@GET
	@Produces(MediaType.LIBRARY_API_REVIEW_COLLECTION)
	public ReviewCollection getReviews(@PathParam("bookid") int bookid) {
		ReviewCollection reviews = new ReviewCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {

			stmt = conn.prepareStatement(buildGetReviews());
			stmt.setInt(1, bookid);
			ResultSet rs = stmt.executeQuery();
			boolean first = true;
			long oldestTimestamp = 0;
			while (rs.next()) {
				Review review = new Review();
				review.setReviewid(rs.getInt("reviewid"));
				review.setBookid(bookid);
				review.setUsername(rs.getString("username"));
				review.setReview(rs.getString("review"));
				review.setName(rs.getString("name"));
				review.setLastModified(rs.getTimestamp("last_modified").getTime());
				
				reviews.addReview(review);
			}
			reviews.setOldestTimestamp(oldestTimestamp);

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
		return reviews;
	}

	private String buildGetReviews() {
		return "select *from review where bookid = ?";
	}

	@GET
	@Path("/{reviewid}")
	@Produces(MediaType.LIBRARY_API_REVIEW)
	public Review getReview(@PathParam("bookid") int bookid,
			@PathParam("reviewid") int reviewid) {
		Review review = new Review();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildGetReviewById();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, bookid);
			stmt.setInt(2, reviewid);
			ResultSet rs = stmt.executeQuery();
			long oldestTimestamp = 0;

			if (rs.next()) {
				review.setReviewid(reviewid);
				review.setBookid(bookid);
				review.setUsername(rs.getString("username"));
				review.setReview(rs.getString("review"));
				review.setName(rs.getString("name"));
				oldestTimestamp = rs.getTimestamp("last_modified").getTime();
				review.setLastModified(oldestTimestamp);
			} else {
				throw new NotFoundException("There's no review with reviewid="
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
		return review;
	}

	private String buildGetReviewById() {
		return "select *from review where bookid = ? and reviewid = ?";
	}

	@POST
	@Consumes(MediaType.LIBRARY_API_REVIEW)
	@Produces(MediaType.LIBRARY_API_REVIEW)
	public Review postReview(Review review, @PathParam("bookid") int bookid) {
		valitadeReview(review);
		boolean response = validateUser(bookid);
		if (response == true) {
			throw new ForbiddenException(
					"You already have a review in this book");
		} else if (security.getUserPrincipal().getName().equals("admin")) {
			throw new ForbiddenException("Admin can't Post");
		} else {
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException(
						"Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}

			PreparedStatement stmt = null;
			try {
				String sql = buildPostReview();
				stmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, security.getUserPrincipal().getName());
				stmt.setInt(2, bookid);
				stmt.setString(3, review.getReview());
				stmt.setString(4, getName(security.getUserPrincipal().getName()));
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys();
				while (rs.next()) {
					
					review = getReview(bookid, rs.getInt(1));
					
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
		}
		return review;
	}

	private String buildPostReview() {

		return "insert into review (username, bookid, review, name) values(?, ?, ?, ?)";
	}

	// Update a Review that a Username has Post
	@PUT
	@Path("/{reviewid}")
	@Consumes(MediaType.LIBRARY_API_REVIEW)
	@Produces(MediaType.LIBRARY_API_REVIEW)
	public Review updateReview(@PathParam("bookid") int bookid,
			@PathParam("reviewid") int reviewid, Review review) {
		valitadeReview(review);
		boolean response = validateUser(bookid);
		if (response == false) {
			throw new ForbiddenException("You don't have a review in this book");
		} else if (security.getUserPrincipal().getName().equals("admin")) {
			throw new ForbiddenException("Admin don't have reviews");
		} else {
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException(
						"Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}

			PreparedStatement stmt = null;
			try {
				String sql = buildUpdateReview();
				stmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, review.getReview());
				stmt.setInt(2, reviewid);
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {

					review = getReview(bookid, reviewid);
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
		}
		return review;
	}

	private String buildUpdateReview() {
		return "update review set review=? where reviewid = ?";
	}

	// Delete a Review with a certain reviewid
	@DELETE
	@Path("/{reviewid}")
	public void deleteReview(@PathParam("reviewid") int reviewid,
			@PathParam("bookid") int bookid) {
		boolean response = validateUser(bookid);

		if (response == false
				&& !security.getUserPrincipal().getName().equals("admin")) {
			throw new ForbiddenException("You can't do this");
		} else {
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException(
						"Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}

			PreparedStatement stmt = null;
			try {
				String sql = buildDeleteReview();
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, reviewid);
				stmt.executeUpdate();
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

	}

	private String buildDeleteReview() {

		return "delete from review where reviewid = ?";
	}

	private void valitadeReview(Review review) {
		if (review.getReview() == null)
			throw new BadRequestException("Review can't be null");

	}

	private boolean validateUser(int bookid) {
		// This Function Returns True if he finds a Username that has a Review
		// in the book with bookid =... and false if not
		boolean res = false;
		ReviewCollection reviews = getReviews(bookid);
		for (int i = 0; i < reviews.getReviews().size(); i++) {
			if (security.getUserPrincipal().getName()
					.equals(reviews.getReviews().get(i).getUsername())) {
				res = true;
			}
		}
		return res;

	}

	private String getName(String username) {
		Connection conn = null;
		String name = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			String sql = "Select name from users where username = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				name = rs.getString("name");

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

		return name;

	}
}
