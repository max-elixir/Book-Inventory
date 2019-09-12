package misc;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class BookDetailController {
	private static Logger logger = LogManager.getLogger();
	private String book;
	
	@FXML private TextField tfTitle, tfYear, tfIsbn;
	@FXML private TextArea taSummary;
	@FXML private Button buttonSave;
	
	/**TODO
	 * Change to take in a Book object from DB
	 * 
	 * @param book
	 */
	public BookDetailController(String book) {
		this.book = book;
	}
	
	public void initialize() {
		tfTitle.setText(book);
		tfYear.setText("1900");
		tfIsbn.setText("0123456789101112");
		taSummary.setWrapText(true);
		taSummary.setText("Lorem ipsum dolor sit amet, "
				+ "consectetur adipiscing elit. Praesent eu mauris quam. "
				+ "Sed scelerisque leo et placerat hendrerit. "
				+ "Nunc nec tellus eget tellus luctus congue non sit amet nisi. "
				+ "Aliquam tempus quam ac lectus interdum cursus. "
				+ "Vivamus vel ante egestas, dignissim lacus eu, pellentesque nisi. "
				+ "Cras ut enim accumsan, convallis lacus sed, luctus elit. "
				+ "Sed viverra vestibulum felis, quis lacinia tortor semper interdum. "
				+ "In in convallis magna, vitae tristique ligula. "
				+ "Proin consequat enim nec commodo eleifend. "
				+ "Aliquam pellentesque nisl non pulvinar tempus.");
	}
	

	@FXML public void handleButtonSave(ActionEvent action) throws IOException {
		Object source = action.getSource();
		if(source == buttonSave) {
			//MasterController.getInstance().changeView(ViewType.CAR_DETAIL, new Car());
			save();
		}
	}
	
	/**
	 * @return true if save is successful, else false
	 */
	public boolean save() {
		try {
			//update the model data
			/** TODO: 
			 * - should restore original model if save fails
			 * - Actually read the Book object and actually save it later
			 */
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Changes Saved");
			alert.setHeaderText(null);
			alert.setContentText("Changes saved theoretically!");
			logger.info("Changes saved for \""+ book+"\"");
			
			alert.showAndWait();
		} catch(Exception e) {
			logger.error("Could not save: " + e.getMessage());
			
			return false;
		}
		return true;
	}
	
}
