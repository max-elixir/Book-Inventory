package controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import model.ViewType;

public class MenuController {
	private static Logger logger = LogManager.getLogger();
	
	@FXML private MenuItem menuQuit, menuBookList;
	
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
		
	}
	

}
