package model;

import misc.BookTableGateway;

public class Book {
	private String title, summary, ISBN;
	private int year, id;
	private BookTableGateway gateway;
	
	public Book(String title, String summary, int year, String ISBN, int id) {
		setTitle(title);
		setSummary(summary);
		setYear(year);
		setISBN(ISBN);
		setId(id);
	}
	
	public Book() {
		setTitle(null);
		setSummary(null);
		setYear(-1);
		setISBN(null);
		setId(-1);
	}
	
	public void save() throws BookException, GatewayException {
		if(!isValidTitle(getTitle())) 
			throw new BookException("Title must be less than 255 characters");
		
		if(!isValisYear(getYear()))
			throw new BookException("Book must be published between 1455 and 2019");

		if(!isValidSummary(getSummary()))
			throw new BookException("Summary has a maximum of 65536 characters");
		
		if(!isValidIsbn(getISBN()))
			throw new BookException("ISBN must be formated to 13 characters or less");
		
		try {
			if(getId() == -1) {
				id = gateway.insertBook(this);
			} else {
				gateway.updateBook(this);
			}
		} catch (GatewayException e) {
			throw new BookException("Unable to save valid book to database: " 
					+ e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private boolean isValidIsbn(String isbn) {
		if (isbn.length() > 13) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isValidSummary(String summary) {
		if (summary.length() > 65536) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isValisYear(int year) {
		if (year > 2019 || year < 1455) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isValidTitle(String title) {
		if (title.length() > 255) {
			return false;
		} else {
			return true;
		}
	}

	public String toString() {
		return getTitle();	
	}
	
	public BookTableGateway getGateway() {
		return gateway;
	}
	
	public void setGateway(BookTableGateway gate) {
		gateway = gate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
