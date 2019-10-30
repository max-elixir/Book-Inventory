package model;

import java.util.List;

import misc.BookTableGateway;

public class Book {
	private String title, summary, ISBN;
	private int year, id, publisher_id;
	private BookTableGateway gateway;

	
	public Book(String title, String summary, int year, String ISBN, int id, int publisher_id) {
		setTitle(title);
		setSummary(summary);
		setYear(year);
		setISBN(ISBN);
		setId(id);
		setPublisher(publisher_id);
	}

	public Book() {
		setTitle(null);
		setSummary(null);
		setYear(-1);
		setISBN(null);
		setId(-1);
		setPublisher(0);
	}
	
	public void save() throws BookException, GatewayException {
		if(!isValidTitle(getTitle())) 
			throw new BookException("Title must be less than 255 characters and not null");
		
		if(!isValisYear(getYear()))
			throw new BookException("Book must be published between 1455 and 2019");

		if(!isValidSummary(getSummary()))
			throw new BookException("Summary has a maximum of 65536 characters and can't be null");
		
		if(!isValidIsbn(getISBN()))
			throw new BookException("ISBN must be formated to atleast 13 characters and can't be null");
		
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
	
	public List<AuditTrailEntry> getAuditTrail() {
		return gateway.getAuditTrail(this);
	}
	
	private boolean isValidIsbn(String isbn) {
		if (isbn == null) {
			return false;
		} else if (isbn.length() > 13) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isValidSummary(String summary) {
		if (summary == null) {
			return false;
		} else if (summary.length() > 65536) {
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
		if (title == null) {
			return false;
		} else if (title.length() > 255) {
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

	public int getPublisher() {
		return publisher_id;
	}
	
	public void setPublisher(int publisher_id) {
		this.publisher_id = publisher_id;
	}
	
}
