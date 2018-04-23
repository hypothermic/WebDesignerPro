package nl.hypothermic.webdespro;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/******************************\
 * > WebDesPro.java         < *
 * WebDesPro by hypothermic   *
 * www.github.com/hypothermic *
 * See LICENSE.txt for legal  *
\******************************/

public class WebDesPro extends Application {
	
	private FadeTransition fade = new FadeTransition(Duration.millis(850));
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage xs) throws IOException {
		StageManager.setStage(xs);
		xs.initStyle(StageStyle.UNDECORATED);
		Parent root = FXMLLoader.load(getClass().getResource("ui/Interface.fxml"));
	    fade.setNode(root);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setCycleCount(1);
        fade.setAutoReverse(false);
        xs.setScene(new Scene(root));
        fade.playFromStart();
	    xs.show();
	    StageManager.maximize();
	}
}
