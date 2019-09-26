package model;

public class Book {
	private String title, summary, ISBN;
	private int year;
	
	public Book(String title, String summary, int year, String ISBN) {
		setTitle(title);
		setSummary(summary);
		setYear(year);
		setISBN(ISBN);
	}
	
	public String toString() {
		return title + ", Published: " + year + " ISBN: "+ ISBN;	
	}

	public String getTitle() {
		return title;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	private void setSummary(String summary) {
		this.summary = summary;
	}

	public String getISBN() {
		return ISBN;
	}

	private void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public int getYear() {
		return year;
	}

	private void setYear(int year) {
		this.year = year;
	}
	
}
