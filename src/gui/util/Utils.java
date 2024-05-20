package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage getCurretnStage(ActionEvent event) {
		
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}
	
	public static Integer tryParseToInt(String strNumber) {
		try {
			return Integer.parseInt(strNumber);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}
}
