package controller;

import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class BookListController {
	private static Logger logger = LogManager.getLogger();
	
	@FXML private ListView<Book> listBooks;
	@FXML private Button buttonDelete;
	
	private List<Book> books;
	
	public BookListController(List<Book> books) {
		this.books = books;
	}
		
	public void initialize() {
		ObservableList<Book> items = listBooks.getItems();
		
		for(Book book : books) {
			items.add(book);
		}
		
		listBooks.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent click) {
                if(click.getClickCount() == 2) {
                	Book selected = listBooks.getSelectionModel().getSelectedItem();
                   
                	logger.info("Double clicked on book \"" + selected + "\"");
                	
        			BookController.changeView(ViewType.BOOK_DETAIL, selected);
                }
            }
        });

	}
	
	@FXML 
	public void handleButtonDelete(ActionEvent action) throws IOException, GatewayException {
		Object source = action.getSource();
		if(source == buttonDelete) {
			Book selected = listBooks.getSelectionModel().getSelectedItem();
			int index = listBooks.getSelectionModel().getSelectedIndex();
			if (selected != null) {
				logger.info("Attempting to delete "+ selected);
				listBooks.getSelectionModel().clearSelection(index);
				//delete(selected);
			} else {
				return;
			}
			
		}
	}

	public boolean delete(Book selected) {
		return false;
	}
}
