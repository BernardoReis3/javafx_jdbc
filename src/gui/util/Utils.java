package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage getCurretnStage(ActionEvent event) {
		
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}
}
