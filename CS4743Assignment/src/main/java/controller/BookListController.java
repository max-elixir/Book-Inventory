package controller;

import model.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import misc.BookInventory;

public class BookListController implements Controller{
	private static Logger logger = LogManager.getLogger();
	
	@FXML private ListView<Book> listBooks;
	@FXML private Button buttonDelete;
	
	private List<Book> books;
	private Alert alert;
	private ObservableList<Book> items;
	
	public BookListController(List<Book> books) {
		this.books = books;
	}
		
	public void initialize() {
		items = listBooks.getItems();
		
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
			if (selected != null) {
				logger.info("Selected "+selected+" to be deleted.");
				if(delete(selected)) {
					logger.info("Book has been deleted.");
					refresh(selected);
				}
			} else {
				return;
			}
		}
	}

	public boolean delete(Book selected) throws GatewayException {
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete a Book?");
		alert.setHeaderText("Attempting to delete " + selected);
		alert.setContentText(selected 
				+ " will be permanently removed from the Inventory. Continue?");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	    stage.getIcons().add(new Image(BookInventory.class.getResourceAsStream("../thebaby.jpg")));
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK){
			logger.info("Attempting to delete "+ selected);
			selected.getGateway().deleteBook(selected);
			return true;
		} else {
		    return false;
		}
	}
	
	private void refresh(Book selected) {
		items.removeAll(books);
		books.remove(selected);
		items.addAll(books);
	}

	@Override
	public boolean hasChanged() {
		// TODO Auto-generated method stub
		return false;
	}
}
