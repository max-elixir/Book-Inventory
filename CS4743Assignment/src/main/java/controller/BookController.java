package controller;

import model.*;
import misc.BookTableGateway;
import misc.PublisherTableGateway;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
	private static BookTableGateway bookGate;
	private static Controller currentController;
	
	public BookController() {	
	}

	public static boolean changeView(ViewType viewType, Object object) {
		/* If trying to change views and but there were changes made on the screen,
		 * prompt the user if they want to save before changing views.
		 * Yes or no will either save or not save then change the view,
		 * cancel will make the user not change views.
		 * 
		 * Small issues putting alert message in its own method, 
		 * copy-pasted here for now.
		 */
		if(currentController != null && currentController.hasChanged()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			
			alert.getButtonTypes().clear();
			ButtonType buttonTypeOne = new ButtonType("Yes");
			ButtonType buttonTypeTwo = new ButtonType("No");
			ButtonType buttonTypeThree = new ButtonType("Cancel");
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

			alert.setTitle("Save Changes?");
			alert.setHeaderText("The current view has unsaved changes.");
			alert.setContentText("Do you wish to save them before switching to a different view?");
			
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get().getText().equalsIgnoreCase("Yes")) {
				try {
					controllerSave();
				} catch (GatewayException e) {
					BookDetailController.showMessage("Unable to save", "An error has occured or the record was changed before you could save. Unable to save.");
				}
			} else if(result.get().getText().equalsIgnoreCase("Cancel") ) {
				return false;
			}
		} 
		
		/* Determine which view to change */
		FXMLLoader loader = null;
		
		if(viewType == ViewType.BOOK_LIST) {
			List<Book> dbBooks = bookGate.getBooks();
			
			loader = new FXMLLoader(BookController.class.getResource("BookListView.fxml"));
			currentController = new BookListController(dbBooks);
			loader.setController(currentController);
			logger.info("Changing to List View");
		} else if (viewType == ViewType.BOOK_DETAIL) {
			if(object == null) {
				Book newBookObject = new Book();
				newBookObject.setGateway(bookGate);
				object = newBookObject;
			}
			if (((Book) object).getId() != -1) {
				try {
					bookGate.lockBook((Book) object);
				} catch (GatewayException e) {
					BookDetailController.showMessage("Unable to edit " + (Book) object, (Book) object + " is currently being updated.");
					return false;
				}
			}
			List<Publisher> publishers = null;
			try {
				publishers = PublisherTableGateway.getPublishers();
			} catch ( Exception e){
				return false;
			}
			
			loader = new FXMLLoader(BookController.class.getResource("BookDetailView.fxml"));
			currentController = new BookDetailController((Book) object, publishers);
			loader.setController(currentController);
			logger.info("Changing to Detail View");
		} else if (viewType == ViewType.AUDIT_TRAIL) {
			loader = new FXMLLoader(BookController.class.getResource("AuditTrailView.fxml"));
			currentController = new AuditTrailController((Book) object);
			loader.setController(currentController);
			logger.info("Changing to Audit Trail View");
		}
		
		Parent view = null;
		try {
			view = loader.load();
			logger.info("View change successful");
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		} catch (NullPointerException e) {
			/* Catch is meant for when menuQuit is clicked. 
			 * Attempting to change to a "null" view and 
			 * choosing yes/no on the dialogue should make the user close either way */
			return true; 
		}
		
		root.setCenter(view);		
		return true;
	}

	public static void setRootPane(BorderPane view) {
		root = view;
	}

	public static void initBookTableGateway() {
		try {
			bookGate = new BookTableGateway();
			logger.info("Gateway initiated");
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			Platform.exit();
		}	
	}
	
	/* handle telling the different controllers how to save before changing views*/
	private static void controllerSave() throws GatewayException {
		if (currentController instanceof BookDetailController) {
			((BookDetailController) currentController).save();
		}
	}

	public static void close() {
		bookGate.close();
	}
}
