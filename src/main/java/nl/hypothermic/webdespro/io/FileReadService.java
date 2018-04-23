package nl.hypothermic.webdespro.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/******************************\
 * > FileReadService.java   < *
 * WebDesPro by hypothermic   *
 * www.github.com/hypothermic *
 * See LICENSE.txt for legal  *
\******************************/

public class FileReadService extends Service<String> {
	
	public FileReadService() {
		;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	private String url;
	
    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
            	String content = null;
                File file = new File(url); // For example, foo.txt
                FileReader reader = null;
                try {
                    reader = new FileReader(file);
                    char[] chars = new char[(int) file.length()];
                    reader.read(chars);
                    content = new String(chars);
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(reader != null){
                        reader.close();
                    }
                }
                return content;
            }
        };
    }
}