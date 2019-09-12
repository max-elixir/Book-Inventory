package misc;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class BookListController {
	private static Logger logger = LogManager.getLogger();
	
	@FXML private ListView<String> listBooks;
	
	//list collection of car data for the list view
	private List<String> books;
	
	/** TODO
	 * Change to take in the list of book objects
	 * */
	public BookListController() {
	}
		
	public void initialize() {
		ObservableList<String> items = listBooks.getItems();
		books = new ArrayList<String>();
		books.add("Catcher in the Rye");
		books.add("Java 9 for Programmers");
		books.add("Duma Key");
		books.add("Computer Networking: A Top Down Approach");
		books.add("Where the Wild Things Are");
		
		for(String book : books) {
			items.add(book);
		}
		
		listBooks.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent click) {
                if(click.getClickCount() == 2) {
                	String selected = listBooks.getSelectionModel().getSelectedItem();
                   
                	logger.info("Double clicked on book \"" + selected + "\"");
                	
        			BookController.changeView(ViewType.BOOK_DETAIL, selected);
                }
            }
        });

	}
}
