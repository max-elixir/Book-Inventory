package model;

import java.sql.Date;

public class AuditTrailEntry {
	private int id;
	private Date dateAdded;
	private String message;
	
	public AuditTrailEntry(int id, Date dateAdded, String message) {
		this.id = id;
		this.dateAdded = dateAdded;
		this.message = message;
	}
	
	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
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

}
