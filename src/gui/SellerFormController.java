package gui;

import java.net.URL;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable{
	
	private Seller seller;
	
	private SellerService sellerService;
	
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
	
	public void setSellerService(SellerService depService) {
		this.sellerService = depService;
	}
	
	public void addToDataChangeListeners(DataChangeListener dataListener) {
		this.dataChangeListeners.add(dataListener);	
	}
	
	public void updateFormData() {
		if(seller == null) {
			throw new IllegalStateException("Seller cannot be null");
		}
		textFieldId.setText(String.valueOf(seller.getId()));
		textFieldName.setText(seller.getName());
		textFieldEmail.setText(seller.getEmail());
		textFieldBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
		if(seller.getBirthDate() != null)
			datePickerBirthDate.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
	}
	
	@FXML
	public void onButtonSaveAction(ActionEvent event) {
		if(seller == null) {
			throw new IllegalStateException("Seller cannot be null");
		}
		if(sellerService == null) {
			throw new IllegalStateException("Seller service cannot be null");
		}
		try {
			seller = getFormData();
			sellerService.saveOrUpdate(seller);
			notifyDataChangeListeners();
			Utils.getCurretnStage(event).close();
		}
		catch (DbException dbe) {
			Alerts.showAlert("Error saving or updating seller", null, dbe.getMessage(), AlertType.ERROR);
		}
		catch (ValidationException ve) {
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
		if(textFieldName.getText() == null || textFieldName.getText().trim() == "") {
			validException.addError("name", "Field cannot be empty");
		}
		seller.setName(textFieldName.getText());
		
		if(validException.getInputErrors().size() > 0) {
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
		if(fieldErrors.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle resourceB) {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 50);	
		Constraints.setTextFieldDouble(textFieldBaseSalary);
		Constraints.setTextFieldMaxLength(textFieldEmail, 40);
		Utils.formatDatePicker(datePickerBirthDate, "dd/MM/yyyy");
	}

}
