package meerkat.modules.serialization.simpleSerialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jakub Cieśla
 */

public class DirectoryNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name;
	private List<DirectoryNode> content;
	private long size;
	private boolean isDirectory = false;
	
	public DirectoryNode(String path) {
		name = path;
	}

	public String getName() {
		return name;
	}

	public List<DirectoryNode> getContent() {
		return content;
	}

	public void setContent(List<DirectoryNode> content) {
		this.content = content;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

}
