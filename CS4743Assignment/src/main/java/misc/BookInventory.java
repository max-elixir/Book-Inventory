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
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../BookMenuView.fxml")); 
		MenuController controller = new MenuController();
		
		//assign to loader
		loader.setController(controller);
		//load the view
		Parent view = loader.load();
	
		//save a global reference to the root-level view of the stage (it is a BorderPane)
		BookController.setRootPane((BorderPane) view);
		
		//plug view into a scene
		Scene scene = new Scene(view, 400, 500);
		
		//set the stage
		stage.setScene(scene);
		stage.setTitle("Book Inventory");
		stage.getIcons().add(new Image(BookInventory.class.getResourceAsStream("../thebaby.jpg")));
		stage.show();
		
	}

	public static void main(String[] args) {
		launch(args);

	}
	
	@Override
	public void init() throws Exception {
		super.init();
				
		//create car gateway
		logger.info("Calling init");
	}

}
