package misc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import controller.BookController;
import controller.MenuController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class BookInventory extends Application{
	private static Logger logger = LogManager.getLogger();
	
	public static void main(String[] args) {
		logger.info("Starting Book Inventory...");
		launch(args);
	}
	
	@Override
	public void init() throws Exception {
		super.init();
		logger.info("Calling init");
		BookController.initBookTableGateway();
	}

	
	@Override
	public void start(Stage stage) throws Exception {
		logger.info("Calling start");
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BookMenuView.fxml")); 
		MenuController controller = new MenuController();
		loader.setController(controller);
		Parent view = loader.load();
		BookController.setRootPane((BorderPane) view);
		
		Scene scene = new Scene(view, 400, 500);
		stage.setScene(scene);
		stage.setTitle("Book Inventory");
		stage.getIcons().add(new Image(BookInventory.class.getResourceAsStream("../thebaby.jpg")));
		
		/* Remove ability to close program when there are unsaved edits on current view. */
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() { 
			public void handle(WindowEvent we) {
				if (!BookController.changeView(null, null)) {
					logger.info("User aborted call to stop");
	        		we.consume();
	        	} else {
	        		try {
						stop();
					} catch (Exception e) {
						e.printStackTrace();
		}}}}); 
		
		stage.show();
	}
	
	@Override
	public void stop() throws Exception  {
		super.stop();
		logger.info("Calling stop");
		BookController.close();
		System.exit(0);
	}
	
}
