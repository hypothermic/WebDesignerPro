package nl.hypothermic.webdespro.io;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;

/******************************\
 * > FileExplorerFactory.java *
 * WebDesPro by hypothermic   *
 * www.github.com/hypothermic *
 * See LICENSE.txt for legal  *
\******************************/

public class FileExplorerFactory extends Service<TreeItem<RelativeFile>> {
	
	public FileExplorerFactory(String url) {
		this.url = url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	private String url;
	
    @Override
    protected Task<TreeItem<RelativeFile>> createTask() {
        return new Task<TreeItem<RelativeFile>>() {
            @Override
            protected TreeItem call() throws Exception {
            	TreeItem<RelativeFile> root = new TreeItem<RelativeFile>(new RelativeFile(url));
            	addNodes(root);
            	return root;
            }
            
            public void addNodes(TreeItem<RelativeFile> root) throws IOException {
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(root.getValue().toPath())) {
                    for (Path path : directoryStream) {
                        TreeItem<RelativeFile> newItem = new TreeItem<RelativeFile>(new RelativeFile(path.toString()));
                        
                        root.getChildren().add(newItem);
                        if (Files.isDirectory(path)) {
                            addNodes(newItem);
                        }
                    }
                }
            }
        };
    }
}
