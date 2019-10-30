package model;

import java.sql.Timestamp;

public class AuditTrailEntry {
	private int id, book_id;
	private Timestamp dateAdded; //private Date dateAdded;
	private String message;
	
	/* Constructor for filling a list with entries to use locally */
	public AuditTrailEntry(Timestamp dateAdded, String message) {
		this.dateAdded = dateAdded;
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

	public Timestamp getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Timestamp dateAdded) {
		this.dateAdded = dateAdded;
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
