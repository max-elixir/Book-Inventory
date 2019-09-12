package misc;

/* Wont bother using this until we implement database stuff
 * */
public class Book {
	private String title, summary, ISBN;
	private int year;
	
	public Book(String title, String summary, int year, String ISBN) {
		this.title = title;
		this.summary = summary;
		this.year = year;
		this.ISBN = ISBN;
	}
	
	public String toString() {
		return title + ", Published: " + year + " ISBN: "+ ISBN;	
	}
}
