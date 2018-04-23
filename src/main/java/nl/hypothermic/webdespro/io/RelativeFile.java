package nl.hypothermic.webdespro.io;

import java.io.File;

/******************************\
 * > RelativeFile.java      < *
 * WebDesPro by hypothermic   *
 * www.github.com/hypothermic *
 * See LICENSE.txt for legal  *
\******************************/

public class RelativeFile extends File {
	
	/**
	 * RelativeFile's function is to return only the files's name for the TreeView, not the whole path.
	 * The overridden path method is for the MenuItem "Copy path to clipboard"
	 */

	public RelativeFile(String path) {
		super(path);
	}

	@Override
	public String toString() {
		return super.getName();
	}
	
	@Override
	public String getCanonicalPath() {
		try {
			return super.getCanonicalPath();
		} catch (Exception x) {
			return "null";
		}
	}
}
