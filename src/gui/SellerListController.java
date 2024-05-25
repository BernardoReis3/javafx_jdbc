package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService sellerService;

	private ObservableList<Seller> obsSellerList;

	@FXML
	private TableColumn<Seller, Seller> tableColumnUpdate;

	@FXML
	private TableColumn<Seller, Seller> tableColumnDelete;

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;

	@FXML
	private Button buttonNewSeller;

	@FXML
	public void onButtonNewSellerAction(ActionEvent event) {
		Stage parentStage = Utils.getCurretnStage(event);
		Seller seller = new Seller();
		//createDialogSellerForm(seller, "/gui/SellerForm.fxml", parentStage);
	}

	public void setSellerService(SellerService service) {
		this.sellerService = service;
	}

	public void updateTableView() {
		if (sellerService == null) {
			throw new IllegalStateException("Service must not be null");
		}
		List<Seller> sellerList = sellerService.findAll();
		obsSellerList = FXCollections.observableArrayList(sellerList);
		tableViewSeller.setItems(obsSellerList);
		initEditButtons();
		initRemoveButtons();
	}

	
	private void createDialogSellerForm(Seller seller, String absoluteName, Stage parentStage) {
		/*try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SellerFormController sellerFormController = loader.getController();
			sellerFormController.setSeller(seller);
			sellerFormController.setSellerService(sellerService);
			sellerFormController.addToDataChangeListeners(this);
			sellerFormController.updateFormData();

			Stage dialogDepStage = new Stage();
			dialogDepStage.setTitle("Enter new Seller data: ");
			dialogDepStage.setScene(new Scene(pane));
			dialogDepStage.setResizable(false);
			dialogDepStage.initOwner(parentStage);
			dialogDepStage.initModality(Modality.WINDOW_MODAL);
			dialogDepStage.showAndWait();

		} catch (IOException ioe) {
			Alerts.showAlert("Error", "Error loading view", ioe.getMessage(), AlertType.ERROR);
		} */
	} 

	@Override
	public void initialize(URL uri, ResourceBundle resourceB) {
		initializeNodes();
	}

	public void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);

		// tableView height adjustment to the window
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnUpdate.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnUpdate.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogSellerForm(obj, "/gui/SellerForm.fxml",
						Utils.getCurretnStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnDelete.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller seller) {
		Optional<ButtonType> confirmationResult = Alerts.showConfirmation("Confirmation", "Do you want to delete this seller?");
		
		if(confirmationResult.get() == ButtonType.OK) {
			if(sellerService == null) {
				throw new IllegalStateException("Service cannot be null");
			}
			try {
				sellerService.deleteSeller(seller);
				updateTableView();
			} catch (DbIntegrityException dbie) {
				Alerts.showAlert("Error deleting seller", null, dbie.getMessage(), AlertType.ERROR);
			}
		}
	}

}
