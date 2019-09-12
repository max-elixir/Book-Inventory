package misc;

/* Wont bother using this until we implement database stuff
 * */
public class Book {
	private String title, summary, ISBN;
	private int yearPublished;
	
	public Book(String title, String summary, int year, String ISBN) {
		this.title = title;
		this.summary = summary;
		yearPublished = year;
		this.ISBN = ISBN;
	}
}
