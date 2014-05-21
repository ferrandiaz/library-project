package edu.upc.eetac.dsa.ferrandiaz.library.android.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class LibraryAPI {
	private final static String TAG = LibraryAPI.class.getName();
	private static LibraryAPI instance = null;
	private URL url;

	private LibraryRootAPI rootAPI = null;

	private LibraryAPI(Context context) throws IOException,
			LibraryAndroidException {
		super();

		AssetManager assetManager = context.getAssets();
		Properties config = new Properties();
		config.load(assetManager.open("config.properties"));
		String serverAddress = config.getProperty("server.address");
		String serverPort = config.getProperty("server.port");
		url = new URL("http://" + serverAddress + ":" + serverPort
				+ "/library-api");

		Log.d("LINKS", url.toString());
		getRootAPI();
	}

	public final static LibraryAPI getInstance(Context context)
			throws LibraryAndroidException {
		if (instance == null)
			try {
				instance = new LibraryAPI(context);
			} catch (IOException e) {
				throw new LibraryAndroidException(
						"Can't load configuration file");
			}
		return instance;
	}

	private void getRootAPI() throws LibraryAndroidException {
		Log.d(TAG, "getRootAPI()");
		rootAPI = new LibraryRootAPI();
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new LibraryAndroidException(
					"Can't connect to Library API Web Service");
		}

		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, rootAPI.getLinks());
		} catch (IOException e) {
			throw new LibraryAndroidException(
					"Can't get response from Library API Web Service");
		} catch (JSONException e) {
			throw new LibraryAndroidException("Error parsing Library Root API");
		}

	}

	private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
			throws LibraryAndroidException, JSONException {
		for (int i = 0; i < jsonLinks.length(); i++) {
			Link link = SimpleLinkHeaderParser
					.parseLink(jsonLinks.getString(i));
			String rel = link.getParameters().get("rel");
			String rels[] = rel.split("\\s");
			for (String s : rels)
				map.put(s, link);
		}
	}

	public BookCollection getBooks() throws LibraryAndroidException {

		Log.d(TAG, "getBooks()");
		BookCollection books = new BookCollection();
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
					.get("books").getTarget()).openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new LibraryAndroidException(
					"Can't connect to Beeter API Web Service");
		}
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");
			parseLinks(jsonLinks, books.getLinks());

			books.setNewestTimestamp(jsonObject.getLong("newestTimestamp"));
			books.setOldestTimestamp(jsonObject.getLong("oldestTimestamp"));
			JSONArray jsonBooks = jsonObject.getJSONArray("books");
			for (int i = 0; i < jsonBooks.length(); i++) {
				Book book = new Book();
				JSONObject jsonBook = jsonBooks.getJSONObject(i);
				book.setBookid(jsonBook.getString("bookid"));
				book.setAuthor(jsonBook.getString("author"));
				book.setTitle(jsonBook.getString("title"));
				book.setLenguage(jsonBook.getString("lenguage"));
				book.setEditorial(jsonBook.getString("editorial"));
				book.setEd_date(jsonBook.getString("ed_date"));
				book.setPrint_date(jsonBook.getString("print_date"));
				jsonLinks = jsonBook.getJSONArray("links");
				parseLinks(jsonLinks, book.getLinks());
				books.getBooks().add(book);
			}
		} catch (IOException e) {
			throw new LibraryAndroidException(
					"Can't get response from Beeter API Web Service");
		} catch (JSONException e) {
			throw new LibraryAndroidException("Error parsing Beeter Root API");
		}

		return books;

	}

	public Book getBook(String urlBook) throws LibraryAndroidException {
		Book book = new Book();
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlBook);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonBook = new JSONObject(sb.toString());
			book.setBookid(jsonBook.getString("bookid"));
			book.setAuthor(jsonBook.getString("author"));
			book.setTitle(jsonBook.getString("title"));
			book.setLenguage(jsonBook.getString("lenguage"));
			book.setEditorial(jsonBook.getString("editorial"));
			book.setEd_date(jsonBook.getString("ed_date"));
			book.setPrint_date(jsonBook.getString("print_date"));
			JSONArray jsonLinks = jsonBook.getJSONArray("links");
			parseLinks(jsonLinks, book.getLinks());
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Bad sting url");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException(
					"Exception when getting the sting");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Exception parsing response");
		}
		return book;

	}

	public ReviewCollection getReviews(String urlReview)
	
			throws LibraryAndroidException {
		System.out.println(urlReview);
		ReviewCollection reviews = new ReviewCollection();
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlReview);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonReviews = jsonObject.getJSONArray("reviews");
			for(int i = 0; i<jsonReviews.length(); i++){
				Review review = new Review();
				JSONObject jsonReview = jsonReviews.getJSONObject(i);
				review.setReviewid(jsonReview.getString("reviewid"));
				review.setUsername(jsonReview.getString("username"));
				review.setBookid(jsonReview.getString("bookid"));
				review.setReview(jsonReview.getString("review"));
				review.setName(jsonReview.getString("name"));
				JSONArray jsonLinks = jsonReview.getJSONArray("links");
				parseLinks(jsonLinks, review.getLinks());
				reviews.getReviews().add(review);
			}
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Bad sting url");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException(
					"Exception when getting the review");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Exception parsing response");
		}

		return reviews;
	}
	
	public Review getReview(String urlReview) throws LibraryAndroidException {
		Review review = new Review();
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlReview);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonReview = new JSONObject(sb.toString());
			review.setReviewid(jsonReview.getString("reviewid"));
			review.setUsername(jsonReview.getString("username"));
			review.setBookid(jsonReview.getString("bookid"));
			review.setReview(jsonReview.getString("review"));
			review.setName(jsonReview.getString("name"));
			JSONArray jsonLinks = jsonReview.getJSONArray("links");
			parseLinks(jsonLinks, review.getLinks());
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Bad sting url");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException(
					"Exception when getting the sting");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibraryAndroidException("Exception parsing response");
		}
		return review;

	}
}
