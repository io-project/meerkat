package meerkat.modules.serialization.simpleSerialization;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jakub Cieśla
 */
public class DirectoryTreeBuilder {

	// Narazie nie przejmujemy się dowiązaniami.

	public DirectoryNode buildTree(String pathToRoot) {

		DirectoryNode root = DirectoryDFS(new File(pathToRoot), "");

		return root;
	}

	private DirectoryNode DirectoryDFS(File file, String relativePath) {

		DirectoryNode node = new DirectoryNode(file.getAbsolutePath(), relativePath + file.getName() );
		

		if (file.isFile()) {
			node.setSize(file.length());
		} else if (file.isDirectory()) {
			node.setDirectory(true);

			List<DirectoryNode> content = new ArrayList<DirectoryNode>();

			long size = 0;

			for (String s : file.list()) {

				s = file.getAbsolutePath() + File.separator + s;

				DirectoryNode child = DirectoryDFS(new File(s), relativePath + file.getName() + File.separator);
				size += child.getSize();

				content.add(child);

			}

			node.setSize(size);
			node.setContent(content);
		}

		return node;
	}

}
