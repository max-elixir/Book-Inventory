package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class AuditTrailEntry {
	private int id, book_id;
	private Timestamp date_Added; //private Date dateAdded;
	private String message, dateAdded, pattern = "yyyy-MM-dd hh:mm:ss";
	
	/* Constructor for filling a list with entries to use locally */
	public AuditTrailEntry(Timestamp date_Added, String message) {
		this.date_Added = date_Added;
		SimpleDateFormat datePattern = new SimpleDateFormat(pattern);
		dateAdded = datePattern.format(date_Added);
		this.message = message;
	}
	
	/* Constructor to build entry locally then send to dB */
	public AuditTrailEntry(int book_id, String message) {
		this.book_id = book_id;
		this.message = message;
	}
	
	public String toString() {
		return message;
	}

	public String getDateAdded() {
		return dateAdded;
	}
	
	public Timestamp getUnformatedTimestamp() {
		return date_Added;
	}

	@SuppressWarnings("unused")
	private void setDateAdded(Timestamp date_Added) {
		this.date_Added = date_Added;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getId() {
		return id;
	}
	
	public int getBookId() {
		return book_id;
	}
	
	public void setBookId(int book_id) {
		this.book_id = book_id;
	}

}
