package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{
	
	private Department department;
	
	private DepartmentService departmentService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textFieldId;
	
	@FXML
	private TextField textFieldName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button buttonSave;
	
	@FXML
	private Button buttonCancel;
	
	
	
	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public void setDepartmentService(DepartmentService depService) {
		this.departmentService = depService;
	}
	
	public void addToDataChangeListeners(DataChangeListener dataListener) {
		this.dataChangeListeners.add(dataListener);	
	}
	
	public void updateFormData() {
		if(department == null) {
			throw new IllegalStateException("Department cannot be null");
		}
		textFieldId.setText(String.valueOf(department.getId()));
		textFieldName.setText(department.getName());
	}
	
	@FXML
	public void onButtonSaveAction(ActionEvent event) {
		if(department == null) {
			throw new IllegalStateException("Department cannot be null");
		}
		if(departmentService == null) {
			throw new IllegalStateException("Department service cannot be null");
		}
		try {
			department = getFormData();
			departmentService.saveOrUpdate(department);
			notifyDataChangeListeners();
			Utils.getCurretnStage(event).close();
		}
		catch (DbException dbe) {
			Alerts.showAlert("Error saving or updating department", null, dbe.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Department getFormData() {
		Department department = new Department();
		department.setId(Utils.tryParseToInt(textFieldId.getText()));
		department.setName(textFieldName.getText());
		return department;
	}

	@FXML
	public void onButtonCancelAction(ActionEvent event) {
		Utils.getCurretnStage(event).close();
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle resourceB) {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 20);		
	}

}
