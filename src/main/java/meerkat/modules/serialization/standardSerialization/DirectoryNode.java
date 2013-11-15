package meerkat.modules.serialization.standardSerialization;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jakub Cieśla
 */

public class DirectoryNode implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String name;
	private final String relativePath;
	private List<DirectoryNode> content = new ArrayList<DirectoryNode>();
	private long size;
	private boolean isDirectory = false;

	public void print() {
		System.out.println(name);
		for (DirectoryNode node : content)
			node.print();
	}

	public void DFSReadFiles(FileSender fileSender,
			WritableByteChannel outputChannel) throws IOException {

		File file = new File(name);

		if (file.isFile()) {
			fileSender.sendFile(file, outputChannel);
		}

		for (DirectoryNode node : content) {
			node.DFSReadFiles(fileSender, outputChannel);
		}

	}
	
	public void DFSCreateFiles(FileCreater fileCreater,
			ReadableByteChannel inputChannel, String root) throws IOException {

		
		if(!isDirectory) {
			File file = new File(root + relativePath);
			fileCreater.createFile(file, size, inputChannel);
		}

		for (DirectoryNode node : content) {
			node.DFSCreateFiles(fileCreater, inputChannel, root);
		}

	}

	public DirectoryNode(String path, String relativePath) {
		this.name = path;
		this.relativePath = relativePath;
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

	public String getRelativePath() {
		return relativePath;
	}

}
