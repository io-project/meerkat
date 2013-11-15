package meerkat.modules.serialization.simpleSerialization;

import java.io.File;
import java.io.IOException;

public class DirectoryTreeCreater {
	public void createTree(DirectoryNode node, String root) throws IOException {
		
		DirectoryDFS(node, root);
		
	}
	
	private void DirectoryDFS(DirectoryNode node, String root) throws IOException {
		
		if(node.isDirectory()) {
			new File(root + node.getRelativePath()).mkdir();
		} else {
			new File(root + node.getRelativePath()).createNewFile();
		}
		
		//System.out.println(root+node.getRelativePath());
		
		for(DirectoryNode n : node.getContent()) {
			DirectoryDFS(n, root);
		}
	}
	
}
