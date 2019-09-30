package cardemo;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;

public class MenuController {
	private static Logger logger = LogManager.getLogger();
	
	@FXML private MenuItem menuQuit;
	@FXML private MenuItem menuAddCar, menuCarList;
	
	public MenuController() {
		
	}
	
	@FXML private void handleMenuAction(ActionEvent action) throws IOException {
		Object source = action.getSource();
		if(source == menuQuit) {
			Platform.exit();
		}
		if(source == menuCarList) {
			//get a collection of cars from the gateway
			MasterController.changeView(ViewType.CAR_LIST, null);
			return;
		}
		if(source == menuAddCar) {
			Car car = new Car();
			car.setGateway(MasterController.getCarGateway());
			MasterController.changeView(ViewType.CAR_DETAIL, car);
			return;
		}
	}
	
	//create event handlers and load data from models into fields
	public void initialize() {
		logger.error("Controller init called");
		
	}
}
