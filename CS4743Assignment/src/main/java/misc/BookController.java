package misc;

import java.io.IOException;
import java.util.ArrayList;
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
	
	public BookController() {}
	
	public static void setRootPane(BorderPane view) {
		root = view;
	}

	public static boolean changeView(ViewType viewType, Object object) {
		FXMLLoader loader = null;
		//load view appropriate to the give vType
		if(viewType == ViewType.BOOK_LIST) {
			List<Book> books = new ArrayList<Book>();
			
			loader = new FXMLLoader(BookController.class.getResource("BookListView.fxml"));
			loader.setController(new BookListController());
			
		} else if(viewType == ViewType.BOOK_DETAIL) {
			//logger.fatal("Car Detail clicked");
			//load detail view 1 and plug into center part of App border pane
			loader = new FXMLLoader(BookController.class.getResource("BookDetailView.fxml"));

			loader.setController(new BookDetailController( (Book) object ));
		}
		
		Parent view = null;
		try {
			view = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//attach view to application center of border pane
		root.setCenter(view);		
		return true;
	}

}
