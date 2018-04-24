package nl.hypothermic.webdespro.ui.dialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class DoubleDialog implements Initializable {
	
	public DoubleDialog(AnchorPane parent) throws IOException {
		FXMLLoader fx = new FXMLLoader(getClass().getResource("DoubleDialog.fxml"));
		fx.setController(this);
		parent.getChildren().add((BorderPane) fx.load());
	}
	
	@FXML private BorderPane background;
	
	@FXML private void onCloseRequested() {
		;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
}
