package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable{

	@FXML
	private TableView<Department> tableViewDepartemnt;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button buttonNewDep;
	
	@FXML
	public void onButtonNewDepAction() {
		
	}

	
	@Override
	public void initialize(URL uri, ResourceBundle resourceB) {
		initializeNodes();
	}
	
	public void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		// tableView height adjustment to the window
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartemnt.prefHeightProperty().bind(stage.heightProperty());
	}

}
