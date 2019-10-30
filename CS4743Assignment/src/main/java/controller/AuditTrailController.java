package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
//import java.sql.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.AuditTrailEntry;
import model.Book;
import model.GatewayException;
import model.ViewType;

public class AuditTrailController implements Controller {
	private static Logger logger = LogManager.getLogger();
	private Book book;
	
	@FXML private Button buttonBack;
	@FXML private Text bookName;
	@FXML private TableView<AuditTrailEntry> tvAuditTrail;
	@FXML private TableColumn<AuditTrailEntry, String> tcTimestamp;
	@FXML private TableColumn<AuditTrailEntry, String> tcMessage;
	
	public AuditTrailController(Book book) {
		this.book = book;
	}

	@Override
	public void initialize() {
		bookName.setText( book.getTitle());
		tcTimestamp.setCellValueFactory( new PropertyValueFactory<AuditTrailEntry, String>("dateAdded"));
		tcMessage.setCellValueFactory( new PropertyValueFactory<AuditTrailEntry, String>("message"));
		tvAuditTrail.getItems().setAll( book.getAuditTrail());
	}
	
	@FXML 
	public void handleBackButton(ActionEvent action) throws IOException, GatewayException {
		Object source = action.getSource();
		if (source == buttonBack) {
			logger.info("Attempting to go back to book detail view");
			BookController.changeView( ViewType.BOOK_DETAIL, book);
		}
	}

	@Override
	public boolean hasChanged() {
		return false;
	}

}
