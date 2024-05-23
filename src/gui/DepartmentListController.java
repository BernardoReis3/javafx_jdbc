package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener{
	
	private DepartmentService depService;
	
	private ObservableList<Department> obsDepartmentList;

	@FXML
	private TableView<Department> tableViewDepartemnt;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button buttonNewDep;
	
	@FXML
	public void onButtonNewDepAction(ActionEvent event) {
		Stage parentStage = Utils.getCurretnStage(event);
		Department department = new Department();
		createDialogDepartmentForm(department, "/gui/DepartmentForm.fxml", parentStage);
	}

	
	public void setDepartmentService(DepartmentService service) {
		this.depService = service;
	}
	
	public void updateTableView() {
		if(depService == null) {
			throw new IllegalStateException("Service must not be null");
		}
		List<Department> departmentList = depService.findAll();
		obsDepartmentList = FXCollections.observableArrayList(departmentList);
		tableViewDepartemnt.setItems(obsDepartmentList);
	}
	
	private void createDialogDepartmentForm(Department department, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			DepartmentFormController departmentFormController = loader.getController();
			departmentFormController.setDepartment(department);
			departmentFormController.setDepartmentService(depService);
			departmentFormController.addToDataChangeListeners(this);
			departmentFormController.updateFormData();
			
			Stage dialogDepStage = new Stage();
			dialogDepStage.setTitle("Enter new Department data: ");
			dialogDepStage.setScene(new Scene(pane));
			dialogDepStage.setResizable(false);
			dialogDepStage.initOwner(parentStage);
			dialogDepStage.initModality(Modality.WINDOW_MODAL);
			dialogDepStage.showAndWait();
			
		} catch (IOException ioe) {
			Alerts.showAlert("Error", "Error loading view", ioe.getMessage(), AlertType.ERROR);
		}
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


	@Override
	public void onDataChanged() {
		updateTableView();
	}

}
