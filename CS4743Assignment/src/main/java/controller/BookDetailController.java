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
import model.Book;
import model.BookException;
import model.GatewayException;
import javafx.scene.control.Alert.AlertType;

public class BookDetailController {
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
			save();
		}
	}
	
	/**
	 * @return true if save is successful, else false
	 * @throws GatewayException 
	 */
	public boolean save() throws GatewayException {
		Book original = book;
		try {
			book.setTitle(tfTitle.getText());
			book.setYear(Integer.parseInt(tfYear.getText()));
			book.setISBN(tfIsbn.getText());
			book.setSummary(taSummary.getText());
			
			book.save();
			showMessage("Changes Saved","Changes to book saved!");
			logger.info("Changes saved for \""+ book +"\"");
		} catch(BookException e) {
			showMessage("Changes Not Saved", e.getMessage());
			logger.error("Could not save: " + e.getMessage());
			book.setTitle(original.getTitle());
			book.setYear(original.getYear());
			book.setISBN(original.getISBN());
			book.setSummary(original.getSummary());
			return false;
		} 
		
		return true;
	}
	
	private void showMessage(String title, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
}
