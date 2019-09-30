package controller;

import model.*;
import misc.BookTableGateway;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;


public class BookController {
	private static BorderPane root;
	private static Logger logger = LogManager.getLogger();
	private static BookTableGateway bookGate;
	
	public BookController() {	
	}

	public static boolean changeView(ViewType viewType, Object object) {
		FXMLLoader loader = null;
		
		if(viewType == ViewType.BOOK_LIST) {
			List<Book> dbBooks = bookGate.getBooks();
			loader = new FXMLLoader(BookController.class.getResource("BookListView.fxml"));
			loader.setController(new BookListController(dbBooks));
			logger.info("Changing to List View");
		} else if(viewType == ViewType.BOOK_DETAIL) {
			loader = new FXMLLoader(BookController.class.getResource("BookDetailView.fxml"));
			loader.setController(new BookDetailController((Book) object));
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

	public static void initBookTableGateway() {
		try {
			bookGate = new BookTableGateway();
			logger.info("Gateway initiated");
			//carGateway = new CarTableGatewayRedis();
		} catch (Exception e) {
			e.printStackTrace();
			Platform.exit();
		}	
	}
	
	public static void close() {
		bookGate.close();
	}
}
