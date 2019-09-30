package cardemo;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class DetailView1Controller {
	private static Logger logger = LogManager.getLogger();
	
	@FXML private TextField tfModel, tfYear;
	@FXML private ComboBox<Make> ddlbMake;
	
	@FXML private Button buttonSave;
	
	//model member reference
	private Car car;
	private List<Make> listMakes;
	
	public DetailView1Controller(Car car, List<Make> makes) {
		this.car = car;
		this.listMakes = makes;
	}
	
	@FXML public void handleButtonAction(ActionEvent action) throws IOException {
		Object source = action.getSource();
		if(source == buttonSave) {
			//MasterController.getInstance().changeView(ViewType.CAR_DETAIL, new Car());
			save();
		}
	}
	
	/**
	 * tell model to save itself
	 * @return true if save is successful, else false
	 */
	public boolean save() {
		try {
			//update the model data
			//TODO: should restore original model if save fails
			car.setMake(ddlbMake.getValue());
			car.setModel(tfModel.getText());
			car.setYear(Integer.parseInt(tfYear.getText()));
			
			car.save();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Changes Saved");
			alert.setHeaderText(null);
			alert.setContentText("Changes saved successfully!");

			alert.showAndWait();
			
		} catch(GatewayException | ValidationException e) {
			logger.error("Could not save: " + e.getMessage());
			
			return false;
		}
		return true;
	}
	
	/**
	 * determines if data have changed
	 * compares field data to model data
	 * @return
	 */
	public boolean hasChanged() {
		if(car.getMake().getId() != ddlbMake.getValue().getId())
			return true;
		
		//fall through: has not changed
		return false;
	}
	
	public void initialize() {
		//populate GUI fields
		//tfMake.setText(car.getMake());
		ddlbMake.getItems().addAll(listMakes);
		ddlbMake.setValue(car.getMake());
		
		tfModel.setText(car.getModel());
		tfYear.setText("" + car.getYear());
	}
}
