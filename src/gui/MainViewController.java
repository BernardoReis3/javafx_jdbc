package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("Seller item clicked");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		System.out.println("Department item clicked");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadAboutView("/gui/AboutView.fxml");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle resourceB) {
		
	}
	
	public void loadAboutView(String pathView) {		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(pathView));
			VBox aboutVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
					
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(aboutVBox.getChildren());
		} catch (IOException ioe) {
			Alerts.showAlert("IO Exception", "Error loading view", ioe.getMessage(), AlertType.ERROR);
		}
	}

}
