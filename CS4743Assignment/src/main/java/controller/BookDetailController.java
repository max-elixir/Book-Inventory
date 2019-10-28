package controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import misc.BookInventory;
import model.Book;
import model.BookException;
import model.GatewayException;
import javafx.scene.control.Alert.AlertType;

public class BookDetailController implements Controller{
	private static Logger logger = LogManager.getLogger();
	private Book book;
	
	@FXML private TextField tfTitle, tfYear, tfIsbn;
	@FXML private TextArea taSummary;
	@FXML private Button buttonSave;
	
	/**TODO
	 * Change to take in a Book object from DB
	 * 
	 * @param book
	 */
	public BookDetailController(Book book) {
		this.book = book;
	}
	
	public void initialize() {
		tfTitle.setText(book.getTitle());
		tfYear.setText( Integer.toString( book.getYear()) );
		tfIsbn.setText(book.getISBN());
		taSummary.setWrapText(true);
		taSummary.setText(book.getSummary());
	}
	
	@FXML 
	public void handleButtonSave(ActionEvent action) throws IOException, GatewayException {
		Object source = action.getSource();
		if(source == buttonSave) {
			logger.info("Attempting to save.");
			save();	
		}
	}
	
	/**
	 * @return Ask book to try to save itself to database, determine if it was able to.
	 * @throws GatewayException 
	 * @throws BookException 
	 */
	public void save() throws GatewayException {
		Book original = book;
		int originalId = book.getId();
		try {
			book.setTitle(tfTitle.getText());
			book.setISBN(tfIsbn.getText());
			book.setSummary(taSummary.getText());
			
			try {
				book.setYear(Integer.parseInt(tfYear.getText()));
			} catch (NumberFormatException e) {
				throw new BookException("Please enter a valid year between 1455 and 2019");
			}
			
			book.save();
			if (originalId != -1) {
				showMessage("Changes Saved","Changes to book "+book+" saved!");
				logger.info("Changes saved for \""+ book +"\"");
			} else {
				showMessage("Creation Saved","Creation of book \""+book+"\" saved!");			
				logger.info("Creation saved for \""+ book +"\"");
			}
		} catch(BookException e) {
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
			}
			
			book.setTitle(original.getTitle());
			book.setYear(original.getYear());
			book.setISBN(original.getISBN());
			book.setSummary(original.getSummary());
		} 
		
		return;
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

	@Override
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
		} catch (NullPointerException e) {
			return true;
		}
		
		return false;
	}
	
}
