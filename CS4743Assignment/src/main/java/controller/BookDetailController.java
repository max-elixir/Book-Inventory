package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import misc.AuditTableGateway;
import misc.BookInventory;
import model.AuditTrailEntry;
import model.Book;
import model.BookException;
import model.GatewayException;
import model.Publisher;
import model.ViewType;
import javafx.scene.control.Alert.AlertType;

public class BookDetailController implements Controller{
	private static Logger logger = LogManager.getLogger();
	private Book book;
	private List<Publisher> publishers;
	
	@FXML private TextField tfTitle, tfYear, tfIsbn;
	@FXML private TextArea taSummary;
	@FXML private Button buttonSave, buttonAudit;
	@FXML private ComboBox<Publisher> cbPublisher;
	
	public BookDetailController(Book book, List<Publisher> publishers) {
		this.book = book;
		this.publishers = publishers;
	}
	
	public void initialize() {
		tfTitle.setText(book.getTitle());
		tfYear.setText( Integer.toString( book.getYear()) );
		tfIsbn.setText(book.getISBN());
		taSummary.setWrapText(true);
		taSummary.setText(book.getSummary());
		cbPublisher.getItems().addAll(publishers);
		cbPublisher.setValue(publishers.get(book.getPublisher()));
		if (book.getYear() == -1)
			buttonAudit.setDisable(true);
	}
	
	@FXML 
	public void handleButtonSave(ActionEvent action) throws IOException, GatewayException {
		Object source = action.getSource();
		if(source == buttonSave) {
			logger.info("Attempting to save.");
			save();	
		} else if (source == buttonAudit) {
			BookController.changeView(ViewType.AUDIT_TRAIL, book);
		}
	}
	
	public void save() throws GatewayException {
		Book original = book;
		int originalId = book.getId();
		ArrayList<AuditTrailEntry> audits = new ArrayList<AuditTrailEntry>();
		String message = null;
		
		try {
			if (book.getId() == -1) {
				newBookSave();
				
			} else {
				if ( book.getTitle().compareTo( tfTitle.getText()) != 0 ) {
					message = "Title changed from " + book.getTitle()  + " to " + tfTitle.getText();
					audits.add( new AuditTrailEntry( book.getId(), message));
					book.setTitle( tfTitle.getText());
				}
				if ( book.getYear() != Integer.parseInt( tfYear.getText()))  {
					message = "Year changed from " + book.getYear()  + " to " + tfYear.getText();
					audits.add( new AuditTrailEntry( book.getId(), message));
					try {
						book.setYear( Integer.parseInt(tfYear.getText()));
					} catch (NumberFormatException e) {
						throw new BookException("Please enter a valid year between 1455 and 2019");
					}
				}	
				if ( book.getISBN().compareTo( tfIsbn.getText()) != 0) {
					message = "Isbn changed from " + book.getISBN()  + " to " + tfIsbn.getText();
					audits.add( new AuditTrailEntry( book.getId(), message));
					book.setISBN( tfIsbn.getText());
				}
				if ( book.getSummary().compareTo( taSummary.getText()) != 0) {
					message = "Summary changed for book.";
					audits.add( new AuditTrailEntry( book.getId(), message));
					book.setSummary( taSummary.getText());
				}
				if ( book.getPublisher() != cbPublisher.getValue().getId())  {
					message = "Publisher changed from " + book.getPublisher()  + " to " + cbPublisher.getValue().getId();
					audits.add( new AuditTrailEntry( book.getId(), message));
					book.setPublisher( cbPublisher.getValue().getId());
				}			
			}
			
			book.save();
			if (originalId != -1) {
				showMessage("Changes Saved","Changes to book "+book+" saved!");
				logger.info("Changes saved for \""+ book +"\"");
			} else {
				showMessage("Creation Saved","Creation of book \""+book+"\" saved!");			
				logger.info("Creation saved for \""+ book +"\"");
				buttonAudit.setDisable(false);
				audits = new ArrayList<AuditTrailEntry>();
				audits.add( new AuditTrailEntry( book.getId(), "Book added"));
			}
			
			for (AuditTrailEntry newEntry: audits) {
				try {
					AuditTableGateway.pushAudit(newEntry);
				} catch (GatewayException e) {
					throw new BookException(e.getMessage());
				}
			}
		} catch(BookException | NullPointerException e) {
			if (originalId != -1){
				showMessage("Changes Not Saved","Changes to \""+book+"\" not saved: "
						+ e.getMessage());
				logger.error("Changes not saved for \""+ book +"\": "
						+ e.getMessage());
			} else {
				showMessage("Creation Not Saved","Creation of \""+book+"\" not saved.: "
						+ e.getMessage());
				logger.error("Creation not saved for \""+ book +"\": "
						+ e.getMessage());
				e.printStackTrace();
			}
			
			book.setTitle(original.getTitle());
			book.setYear(original.getYear());
			book.setISBN(original.getISBN());
			book.setSummary(original.getSummary());
			book.setPublisher(original.getPublisher());
		} 
		
		return;
	}

	private void newBookSave() throws BookException {
		book.setTitle( tfTitle.getText());
		book.setISBN( tfIsbn.getText());
		book.setSummary( taSummary.getText());
		book.setPublisher( cbPublisher.getValue().getId());
		try {
			book.setYear(Integer.parseInt(tfYear.getText()));
		} catch (NumberFormatException e) {
			throw new BookException("Please enter a valid year between 1455 and 2019");
		}
	}
	
	
	public static void showMessage(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	    stage.getIcons().add(new Image(BookInventory.class.getResourceAsStream("../thebaby.jpg")));
		
		alert.showAndWait();
	}

	public boolean hasChanged() {
		try {
			if ( book.getTitle().compareTo( tfTitle.getText()) != 0 )
				return true;
			if ( book.getYear() != Integer.parseInt( tfYear.getText())) 
				return true;
			if ( book.getISBN().compareTo( tfIsbn.getText()) != 0)
				return true;
			if ( book.getSummary().compareTo( taSummary.getText()) != 0)
				return true;
			if ( book.getPublisher() != cbPublisher.getValue().getId()) 
				return true;
		} catch (NullPointerException e) {
			return true;
		}
		
		
		try {
			book.getGateway().unlockBook(book);
		} catch (GatewayException e) {
			logger.error("unable to unlock book");
		}
		return false;
	}
	
}
