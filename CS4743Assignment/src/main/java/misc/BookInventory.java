package misc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BookInventory extends Application{
	private static Logger logger = LogManager.getLogger();
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BookMenuView.fxml")); 
		MenuController controller = new MenuController();
		
		loader.setController(controller);
		Parent view = loader.load();
	
		BookController.setRootPane((BorderPane) view);
		
		Scene scene = new Scene(view, 400, 500);
		stage.setScene(scene);
		stage.setTitle("Book Inventory");
		stage.getIcons().add(new Image(BookInventory.class.getResourceAsStream("thebaby.jpg")));
		stage.show();
		
	}

	public static void main(String[] args) {
		logger.info("Starting Book Inventory...");
		launch(args);
	}
	
	@Override
	public void init() throws Exception {
		super.init();
				
		//create car gateway
		logger.info("Calling init");
	}

}
