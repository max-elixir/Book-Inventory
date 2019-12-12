package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class BookReportController implements Controller {
	private static Logger logger = LogManager.getLogger();
	@FXML private TextField filename, filepath;
	@FXML private Button cd, Save;
	private String rawFile;
	
	public BookReportController(String report) {
		this.rawFile = report;
	}
	
	@Override
	public void initialize() {
		filename.setText("book_report.xls");
	}
	
	@FXML
	public void handleButtonSave (ActionEvent event) {
		PrintWriter writer;
        try {
        	if ( filepath.getText().compareTo("") != 0 && filename.getText().compareTo("") != 0) {
        		writer = new PrintWriter(new File( filepath.getText()));
				writer.println(rawFile);
		       	writer.close();
		       	BookDetailController.showMessage("File saved", "File saved at " + filepath.getText());
			} else {
				BookDetailController.showMessage("Please enter a name and path", "Please enter a file name and enter a path to the save file");
			}
		} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void handleButtonDirectory (ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select save file location");
		fileChooser.setInitialFileName(filename.getText());
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("XLS", "*.xls"));
		
		File selectedFile = fileChooser.showSaveDialog(null);
		if (selectedFile != null) {
			filepath.setText(selectedFile.getAbsolutePath());
			filename.setText(selectedFile.getName());
			logger.info("Save location chosen: "  + filepath.getText());
		}
		else {
		    logger.info("User canceled saving file");
		}
	}

	@Override
	public boolean hasChanged() {
		return false; /* There's a time limit already in place, no need to make user wait */
	}

}
