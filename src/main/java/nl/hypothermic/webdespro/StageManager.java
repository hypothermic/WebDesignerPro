package nl.hypothermic.webdespro;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

/******************************\
 * > StageManager.java      < *
 * WebDesPro by hypothermic   *
 * www.github.com/hypothermic *
 * See LICENSE.txt for legal  *
\******************************/

public class StageManager {
	
	private static Stage xs;
	
	public static void setStage(Stage ns) {
		if (xs == null) {
			xs = ns;
		} else {
			throw new RuntimeException("Invalid attempt to replace StageManager's stage.");
		}
	}
	
	// Minimize to taskbar
	public static void minimize() {
		xs.setIconified(true);
	}
	
	// From taskbar to visible
	public static void deminimize() {
		xs.setIconified(false);
	}
	
	private static Boolean isMaximized = false;
	
	// Fill whole screen
	public static void maximize() {
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();

		xs.setX(bounds.getMinX());
		xs.setY(bounds.getMinY());
		xs.setWidth(bounds.getWidth());
		xs.setHeight(bounds.getHeight());
		isMaximized = true;
	}
	
	public static void demaximize() {
		xs.setWidth(1080);
		xs.setHeight(620);
		isMaximized = false;
	}
	
	public static Boolean isMaximized() {
		return isMaximized;
	}
	
	// adapted from: https://stackoverflow.com/a/13460743/9107324
	private static double xOffset;
	private static double yOffset;
	
	public static void onWindowMoveStart(MouseEvent event) {
		xOffset = event.getSceneX();
        yOffset = event.getSceneY();
	}
	
	public static void onWindowMoveStop(MouseEvent event) {
		xs.setX(event.getScreenX() - xOffset);
        xs.setY(event.getScreenY() - yOffset);
	}
}
