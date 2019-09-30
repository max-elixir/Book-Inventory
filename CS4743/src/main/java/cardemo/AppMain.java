package cardemo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AppMain extends Application {
	private static Logger logger = LogManager.getLogger(AppMain.class);
		
	@Override
	public void init() throws Exception {
		super.init();
				
		//create car gateway
		logger.error("Calling init");
		MasterController.initCarGateway();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		//close gateway
		logger.error("Calling stop");
		MasterController.close();
		
		System.exit(0);
	}

	@Override
	public void start(Stage stage) throws Exception {
		//init the view
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view_master.fxml"));
		//init the controller 
		MenuController controller = new MenuController();
		
		//assign to loader
		loader.setController(controller);
		
		//load the view
		Parent view = loader.load();
		//save a global reference to the root-level view of the stage (it is a BorderPane)
		MasterController.setRootPane((BorderPane) view);
		
		//plug view into a scene
		Scene scene = new Scene(view);
		
		//set the stage
		stage.setScene(scene);
		stage.setTitle("Section 001 A1 Demo");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);

	}

}