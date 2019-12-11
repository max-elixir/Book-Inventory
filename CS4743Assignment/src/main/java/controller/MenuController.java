package controller;

import model.ViewType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import misc.CryptoStuff;
import misc.VertxGateway;

public class MenuController {
	private static Logger logger = LogManager.getLogger();
	
	@FXML private MenuItem menuQuit, menuBookList, menuBookAdd, menuAbout, menuBookDetailReport;
	@FXML private Button login;
	@FXML private TextField username;
	@FXML private PasswordField password;
	
	public MenuController() {
	}
	
	@FXML private void handleMenuAction(ActionEvent action) throws Exception {
		Object source = action.getSource();
		if(source == login) {
			logger.info("Clicked on menu item Login");
			//logger.info("username is " + username.getText());
			//logger.info("Hashed password is " + CryptoStuff.sha256(password.getText()));
			String bearer = VertxGateway.vertxLogin(username.getText(), CryptoStuff.sha256(password.getText()));
			BookController.setSession(bearer);
		}
		if(source == menuQuit) {
			logger.info("Clicked on menu item Quit");
			if (BookController.changeView(null, null)) {
				Platform.exit();
				logger.info("Exiting program");
			} else {
				logger.info("User aborted menu item Quit to continue working");
				return;
			}
		}
		if(source == menuBookList) {
			logger.info("Clicked on menu item Book List");
			BookController.changeView(ViewType.BOOK_LIST, null);
			return;
		}
		if (source == menuBookAdd) {
			logger.info("Clicked on menu item Add Book");
			BookController.changeView(ViewType.BOOK_DETAIL, null);
			return;
		}
		if (source == menuBookDetailReport) {
			logger.info("Clicked on menu item Book Report");
			BookController.changeView(ViewType.BOOK_REPORT, null);
			return;
		}
		if (source == menuAbout) {
			logger.info("Clicked on menu item About");
			BookDetailController.showMessage("About Book Inventory",
				"The assignment is a JavaFX Application that allows the user to\r\n" + 
				"see a list of books and see details about each, adding and removing books\r\n" + 
				"from its inventory on the database as desired. Users are also able to update\r\n" + 
				"and details about each book. Meant to be a practice\r\n" + 
				"on the proper creation of an enterprise-tier application.\r\n" + 
				"");
			return;
		}
		
	}
}
