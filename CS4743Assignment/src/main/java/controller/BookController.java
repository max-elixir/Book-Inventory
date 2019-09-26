package controller;

import model.*;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

public class BookController {
	private static BorderPane root;
	private static Logger logger = LogManager.getLogger();
	
	public BookController() {	
	}

	public static boolean changeView(ViewType viewType, Object object) {
		FXMLLoader loader = null;
		if(viewType == ViewType.BOOK_LIST) {
			/* TODO
			 * Fill with a call to a database to get a list of books.
			 */
			loader = new FXMLLoader(BookController.class.getResource("BookListView.fxml"));
			loader.setController(new BookListController());
			logger.info("Changing to List View");
		} else if(viewType == ViewType.BOOK_DETAIL) {
			loader = new FXMLLoader(BookController.class.getResource("BookDetailView.fxml"));
			loader.setController(new BookDetailController((String) object));
			logger.info("Changing to Detail View");
		}
		
		Parent view = null;
		try {
			view = loader.load();
			logger.info("View change succesfull");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		root.setCenter(view);		
		return true;
	}

	public static void setRootPane(BorderPane view) {
		root = view;
	}
}
