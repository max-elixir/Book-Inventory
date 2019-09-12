package misc;

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

public class MenuController {
	private static Logger logger = LogManager.getLogger();
	
	@FXML private MenuItem menuQuit, menuBookList;
	
	public MenuController() {
		
	}
	
	@FXML private void handleMenuAction(ActionEvent action) throws IOException {
		Object source = action.getSource();
		if(source == menuQuit) {
			Platform.exit();
		}
		if(source == menuBookList) {
			//get a collection of cars from the gateway
			BookController.changeView(ViewType.BOOK_LIST, null);
			return;
		}
		
	}
	
	//create event handlers and load data from models into fields
	public void initialize() {
		logger.error("Controller init called");
		
	}
}
