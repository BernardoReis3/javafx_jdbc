package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable{
	
	private Department department;

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
	
	public void updateFormData() {
		if(department == null) {
			throw new IllegalStateException("Department cannot be null");
		}
		textFieldId.setText(String.valueOf(department.getId()));
		textFieldName.setText(department.getName());
	}
	
	@FXML
	public void onButtonSaveAction() {
		System.out.println("Save action");
	}
	
	@FXML
	public void onButtonCancelAction() {
		System.out.println("Cancel action");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle resourceB) {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 20);		
	}

}
