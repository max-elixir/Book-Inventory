package misc;

import controller.*;

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
		
		/*
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  System.out.println("hmmm");
	        	  if (BookController.changeView(null, null)) {
	        		  System.out.println("dont leave me");
	        		  we.consume();
	        	  } else {
	        		  System.out.println("leave me");
	        		  try {
						stop();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	  }
	          }
	    }); */
		
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
