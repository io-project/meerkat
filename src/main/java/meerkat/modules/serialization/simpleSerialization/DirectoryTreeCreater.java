package meerkat.modules.serialization.simpleSerialization;

import java.io.File;
import java.io.IOException;

public class DirectoryTreeCreater {
	public void createTree(DirectoryNode node) throws IOException {
		
		DirectoryDFS(node);
		
	}
	
	private void DirectoryDFS(DirectoryNode node) throws IOException {
		
		if(node.isDirectory()) {
			new File(node.getName()).mkdir();
		} else {
			new File(node.getName()).createNewFile();
		}
		
		for(DirectoryNode n : node.getContent()) {
			DirectoryDFS(n);
		}
	}
	
}
