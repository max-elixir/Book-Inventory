package model;

import java.sql.Timestamp;

public class Publisher {
	private int id;
	private Timestamp timestamp;
	private String pubName;

	public Publisher(String pubName, Timestamp timestamp, int id) {
		setPublisherName(pubName);
		setDateAdded(timestamp);
		setId(id);
	}

	public String toString() {
		return getPublisherName();
	}
	
	public int getId() {
		return id;
	}
	
	private void setId(int id) {
		this.id = id;
	}

	public Timestamp getDateAdded() {
		return timestamp;
	}
	
	private void setDateAdded(Timestamp timestamp) {
		this.timestamp = timestamp;
		
	}
	
	public String getPublisherName() {
		return pubName;
	}

	private void setPublisherName(String pubName) {
		this.pubName = pubName;
		
	}
}
