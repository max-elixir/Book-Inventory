package controller;

import model.ViewType;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MenuController {
	private static Logger logger = LogManager.getLogger();
	
	@FXML private MenuItem menuQuit, menuBookList, menuBookAdd, menuAbout;
	
	public MenuController() {
	}
	
	@FXML private void handleMenuAction(ActionEvent action) throws IOException {
		Object source = action.getSource();
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
