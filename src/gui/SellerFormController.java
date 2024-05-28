package gui;

import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller seller;

	private SellerService sellerService;

	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textFieldId;

	@FXML
	private TextField textFieldName;

	@FXML
	private TextField textFieldEmail;

	@FXML
	private DatePicker datePickerBirthDate;

	@FXML
	private TextField textFieldBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	private ObservableList<Department> obsListDepartment;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button buttonSave;

	@FXML
	private Button buttonCancel;

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public void setServices(SellerService sellerService, DepartmentService departmentService) {
		this.sellerService = sellerService;
		this.departmentService = departmentService;
	}

	public void addToDataChangeListeners(DataChangeListener dataListener) {
		this.dataChangeListeners.add(dataListener);
	}

	public void updateFormData() {
		if (seller == null) {
			throw new IllegalStateException("Seller cannot be null");
		}
		textFieldId.setText(String.valueOf(seller.getId()));
		textFieldName.setText(seller.getName());
		textFieldEmail.setText(seller.getEmail());
		textFieldBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
		if (seller.getBirthDate() != null)
			datePickerBirthDate
					.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
		if(seller.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		comboBoxDepartment.setValue(seller.getDepartment());
	}

	public void loadDepartments() {
		if (departmentService == null) {
			throw new IllegalStateException("Service cannot be null");
		}
		List<Department> departmetns = departmentService.findAll();
		obsListDepartment = FXCollections.observableArrayList(departmetns);
		comboBoxDepartment.setItems(obsListDepartment);
	}

	@FXML
	public void onButtonSaveAction(ActionEvent event) {
		if (seller == null) {
			throw new IllegalStateException("Seller cannot be null");
		}
		if (sellerService == null) {
			throw new IllegalStateException("Seller service cannot be null");
		}
		try {
			seller = getFormData();
			sellerService.saveOrUpdate(seller);
			notifyDataChangeListeners();
			Utils.getCurretnStage(event).close();
		} catch (DbException dbe) {
			Alerts.showAlert("Error saving or updating seller", null, dbe.getMessage(), AlertType.ERROR);
		} catch (ValidationException ve) {
			setInputErrors(ve.getInputErrors());
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Seller getFormData() {
		Seller seller = new Seller();
		ValidationException validException = new ValidationException("Error");
		seller.setId(Utils.tryParseToInt(textFieldId.getText()));
		
		if (textFieldName.getText() == null || textFieldName.getText().trim() == "") {
			validException.addError("name", "Field cannot be empty");
		}
		seller.setName(textFieldName.getText());
		
		if (textFieldEmail.getText() == null || textFieldEmail.getText().trim() == "") {
			validException.addError("email", "Field cannot be empty");
		}
		seller.setEmail(textFieldEmail.getText());

		if(datePickerBirthDate.getValue() == null) {
			validException.addError("birthDate", "Field cannot be empty");
		}
		Instant instant = Instant.from(datePickerBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
		seller.setBirthDate(Date.from(instant));
		
		if (textFieldBaseSalary.getText() == null || textFieldBaseSalary.getText().trim() == "") {
			validException.addError("salary", "Field cannot be empty");
		}
		seller.setBaseSalary(Utils.tryParseToDouble(textFieldBaseSalary.getText()));
		
		seller.setDepartment(comboBoxDepartment.getValue());
		
		if (validException.getInputErrors().size() > 0) {
			throw validException;
		}
		return seller;
	}

	@FXML
	public void onButtonCancelAction(ActionEvent event) {
		Utils.getCurretnStage(event).close();
	}

	private void setInputErrors(Map<String, String> errors) {
		Set<String> fieldErrors = errors.keySet();
		if (fieldErrors.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		} else {
			labelErrorName.setText("");
		}
		
		if (fieldErrors.contains("email")) {
			labelErrorEmail.setText(errors.get("email"));
		} else {
			labelErrorEmail.setText("");
		}
			
		if (fieldErrors.contains("baseSalary")) {
			labelErrorBaseSalary.setText(errors.get("baseSalary"));
		} else {
			labelErrorBaseSalary.setText("");
		}
		
		if (fieldErrors.contains("birthDate")) {
			labelErrorBaseSalary.setText(errors.get("birthDate"));
		} else {
			labelErrorBirthDate.setText("");
		}
		
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};

		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

	@Override
	public void initialize(URL uri, ResourceBundle resourceB) {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 50);
		Constraints.setTextFieldDouble(textFieldBaseSalary);
		Constraints.setTextFieldMaxLength(textFieldEmail, 40);
		Utils.formatDatePicker(datePickerBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

}
