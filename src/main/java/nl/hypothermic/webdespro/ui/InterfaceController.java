package nl.hypothermic.webdespro.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import nl.hypothermic.webdespro.StageManager;
import nl.hypothermic.webdespro.io.FileExplorerFactory;
import nl.hypothermic.webdespro.io.FileReadService;
import nl.hypothermic.webdespro.io.Icons;
import nl.hypothermic.webdespro.io.RelativeFile;

/******************************\
 * > InterfaceController.java *
 * WebDesPro by hypothermic   *
 * www.github.com/hypothermic *
 * See LICENSE.txt for legal  *
\******************************/

@SuppressWarnings({"rawtypes"})
public class InterfaceController implements Initializable {
	
	// Menu
	@FXML private MenuBar menubar;
	@FXML private AnchorPane logo;
	@FXML private CheckBox btnMinimize;
	@FXML private CheckBox btnExit;
	
	// About screen
	@FXML private BorderPane aboutmenu;
	private final FadeTransition fade = new FadeTransition(Duration.millis(100));
	
	// Web viewer
	@FXML protected WebView webc;
	
	// Edit area
	@FXML private TextArea writer; // TODO
	private final FileReadService frs = new FileReadService();
	
	// File Explorer
	@FXML private TreeView explorer;
	@FXML private TextField explorerField;
	@FXML private Button explorerBrowse;
	private final String defaultPath = "."; // Default startup path, TODO: let user specify
	private final FileExplorerFactory fef = new FileExplorerFactory(defaultPath);
	private String workspaceUrl = defaultPath;
	private FileChooser fc;
	
	// Info popup
	@FXML private BorderPane infoPopup;
	@FXML private Text infoTitle;
	@FXML private Text infoMessage;

	public void initialize(URL arg0, ResourceBundle arg1) {
		menubar.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	if (event.getClickCount() == 1) {
            		StageManager.onWindowMoveStart(event);
            	} else {
            		if (StageManager.isMaximized()) {
            			StageManager.demaximize();
            		} else {
            			StageManager.maximize();
            		}
            	}
            }
        });
        menubar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                StageManager.onWindowMoveStop(event);
            }
        });
        logo.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                StageManager.onWindowMoveStart(event);
            }
        });
        logo.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                StageManager.onWindowMoveStop(event);
            }
        });
        fade.setNode(aboutmenu);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setCycleCount(1);
        fade.setAutoReverse(false);
        final InterfaceController loopback = this;
        fef.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent wse) {
				explorer.setRoot(fef.getValue());
				onWorkspaceRebuilt();
			}
        });
        fef.setOnFailed(new EventHandler<WorkerStateEvent>() {
        	public void handle(WorkerStateEvent wse) {
        		explorer.setRoot(new TreeItem<String>("Error while loading workspace"));
        	}
        });
        frs.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent wse) {
				writer.setText(frs.getValue());
			}
        });
        frs.setOnFailed(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent wse) {
				loopback.createInfoPopup("Error", "Could not read the file: " + frs.getException().getCause());
			}
        });
        /*explorer.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            public TreeCell<File> call(TreeView<File> tv) {
                return new TreeCell<File>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);
                        setText((empty || item == null) ? "" : item.getName());
                    }
                };
            }
        });*/
        explorer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
			    onExplorerSelect();
			}
		}); 
        MenuItem view = new MenuItem("View in browser", new ImageView(Icons.Internet));
    	view.setOnAction(new EventHandler<ActionEvent>() {
    	    public void handle(ActionEvent t) {
    	    	try {
					loopback.getWebView().getEngine().load(((TreeItem<RelativeFile>)explorer.getSelectionModel().getSelectedItem()).getValue().toURI().toURL().toExternalForm());
				} catch (MalformedURLException e) {
					loopback.createInfoPopup("Error", "Could not load file.");
				}
    	    }
    	});
        MenuItem copy1 = new MenuItem("Copy name", new ImageView(Icons.Copy));
    	copy1.setOnAction(new EventHandler<ActionEvent>() {
    	    public void handle(ActionEvent t) {
    	    	Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(((TreeItem<RelativeFile>)explorer.getSelectionModel().getSelectedItem()).getValue().toString()), null);
    	    }
    	});
    	MenuItem copy2 = new MenuItem("Copy full path", new ImageView(Icons.Copy));
    	copy2.setOnAction(new EventHandler<ActionEvent>() {
    	    public void handle(ActionEvent t) {
    	    	Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(((TreeItem<RelativeFile>)explorer.getSelectionModel().getSelectedItem()).getValue().getCanonicalPath()), null);
    	    }
    	});
    	explorer.setContextMenu(new ContextMenu(view, copy1, copy2));
        explorerField.setText(defaultPath);
        this.onWorkspaceRebuild();
	}
	
	@FXML private void onInfoPopupClose() {
		infoPopup.setVisible(false);
	}
	
	/*-*/ private void onExplorerSelect() {
		writer.setText("");
		getWebView().getEngine().load("");
		try {
			File requested = ((TreeItem<RelativeFile>)explorer.getSelectionModel().getSelectedItem()).getValue();
			frs.setUrl(requested.getCanonicalPath());
			frs.restart();
			getWebView().getEngine().load(((TreeItem<RelativeFile>)explorer.getSelectionModel().getSelectedItem()).getValue().toURI().toURL().toExternalForm());
		} catch (IOException e) {
			createInfoPopup("Error", "Could not load file due to " + e.getCause());
		}
	}
	
	@FXML private void onExplorerFieldEnter() {
		workspaceUrl = explorerField.getText();
		explorerField.setDisable(true);
		explorerBrowse.setDisable(true);
		fef.setUrl(workspaceUrl);
		fef.restart();
		this.onWorkspaceRebuild();
	}
	
	@FXML private void onExplorerBrowse() {
		//createInfoPopup("We are sorry", "This feature is not availible yet.\nEnter a path manually instead.");
		this.onExplorerFieldEnter();
	}
	
	@FXML private void onWorkspaceRebuild() {
		explorer.setRoot(new TreeItem<String>("Building workspace..."));
		fef.setUrl(workspaceUrl);
		fef.restart();
	}
	
	/*-*/ private void onWorkspaceRebuilt() {
		explorerField.setDisable(false);
		explorerBrowse.setDisable(false);
	}
	
	@FXML private void onExitRequested() {
		System.exit(1);
	}
	
	@FXML private void onMinimizeRequested() {
		btnMinimize.setSelected(false);
		StageManager.minimize();
	}
	
	@FXML private void onMaximizeRequested() {
		//btnMaximize.setSelected(false);
		StageManager.maximize();
	}
	
	@FXML private void onDeMaximizeRequested() {
		//btnMaximize.setSelected(false);
		StageManager.demaximize();
	}
	
	@FXML private void onMenuAboutOpen() {
		aboutmenu.setVisible(true);
		fade.playFromStart();
	}
	
	@FXML private void onMenuAboutClose() {
		aboutmenu.setVisible(false);
	}
	
	/*-*/ private void onWebLoad(String url) {
		webc.getEngine().load(url);
	}
	
	@FXML private void onWebRefresh() {
		webc.getEngine().reload();
	}
	
	@FXML private void onEditReplace() {
		
	}
	
	// Landscape // TODO: don't mess with Anchors, set width & height.
	@FXML private void onWebDimChangeDesktop() {
		AnchorPane.setLeftAnchor(webc, 10.0);
		AnchorPane.setRightAnchor(webc, 10.0);
		AnchorPane.setTopAnchor(webc, 165.0);
		AnchorPane.setBottomAnchor(webc, 165.0);
	}
	
	// Portrait // TODO: don't mess with Anchors, set width & height.
	@FXML private void onWebDimChangePhone() {
		AnchorPane.setLeftAnchor(webc, 40.0);
		AnchorPane.setRightAnchor(webc, 40.0);
		AnchorPane.setTopAnchor(webc, 15.0);
		AnchorPane.setBottomAnchor(webc, 60.0);
	}
	
	/*-*/ private void createInfoPopup(final String title, final String message) {
		infoTitle.setText(title);
		infoMessage.setText(message);
		infoPopup.setVisible(true);
	}
	
	/*-*/ public WebView getWebView() {
		return this.webc;
	}
}
