package cardemo;

import java.io.IOException;
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

/**
 * keeps program state info, gateways, and centralized control methods
 * implements singleton pattern
 * 
 * decided to make this its own class because JavaFX Application superclass 
 * prevents classic singleton pattern:
 * no private constructor possible because JavaFX uses its own instance for init, start, and stop
 * @author marcos
 *
 */
public class MasterController {
	private static final Logger logger = LogManager.getLogger(MasterController.class);
	
	private static BorderPane rootPane;

	private static CarGateway carGateway;
		
	public MasterController() {
	}

	public static void initCarGateway() {
		//create gateways
		try {
			carGateway = new CarTableGatewayMySQL();
			//carGateway = new CarTableGatewayRedis();
			
		} catch (GatewayException e) {
			e.printStackTrace();
			Platform.exit();
		}
	}
	
	/**
	 * transition to new view in window's center area of border pane
	 * TODO: if current view has changed then prompt to save
	 * @param vType
	 * @param data
	 * @return
	 */
	public static boolean changeView(ViewType vType, Object data) {
		// if current controller and its data have changed
		// then prompt to save

		// switch to new view
		FXMLLoader loader = null;
		//load view appropriate to the give vType
		if(vType == ViewType.CAR_LIST) {
			List<Car> cars = carGateway.getCars();
			
			loader = new FXMLLoader(MasterController.class.getResource("view_list.fxml"));
			loader.setController(new ListViewController(cars));
			
		} else if(vType == ViewType.CAR_DETAIL) {
			//logger.fatal("Car Detail clicked");
			//load detail view 1 and plug into center part of App border pane
			loader = new FXMLLoader(MasterController.class.getResource("view_detail_1.fxml"));

			loader.setController(new DetailView1Controller((Car) data, carGateway.getMakes()));
		}
		
		Parent view = null;
		try {
			view = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//attach view to application center of border pane
		rootPane.setCenter(view);		
		return true;
	}

	/**
	 * clean up method to close gateways, etc.
	 */
	public static void close() {
		carGateway.close();
	}
		
	public BorderPane getRootPane() {
		return rootPane;
	}

	public static void setRootPane(BorderPane rPane) {
		rootPane = rPane;
	}

	public static CarGateway getCarGateway() {
		return carGateway;
	}

	public void setCarGateway(CarTableGatewayMySQL carGateway) {
		this.carGateway = carGateway;
	}

}
