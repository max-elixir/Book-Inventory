package cardemo;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class ListViewController {
	private static Logger logger = LogManager.getLogger();
	
	@FXML private ListView<Car> listCars;
	
	//list collection of car data for the list view
	private List<Car> cars;
	
	public ListViewController(List<Car> cars) {
		this.cars = cars;
	}
		
	public void initialize() {
		ObservableList<Car> items = listCars.getItems();
		
		//fill list with car data
		for(Car car : cars) {
			items.add(car);
		}
		//items.add("Toyota Camry");
		//items.add("Chevrolet Volt");
		
		//add double-click handler to load detail view
		listCars.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if(click.getClickCount() == 2) {
                	//Use ListView's getSelected Item
                	Car selected = listCars.getSelectionModel().getSelectedItem();
                   
                	logger.info("double-clicked " + selected);
                	
        			MasterController.changeView(ViewType.CAR_DETAIL, selected);
        			
                	
                }
            }
        });

	}
}
