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
			Platform.exit();
			logger.info("Exiting program");
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
		
	}
}
