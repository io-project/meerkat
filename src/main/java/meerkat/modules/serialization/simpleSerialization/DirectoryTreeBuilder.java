package meerkat.modules.serialization.simpleSerialization;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jakub Cieśla
 */
public class DirectoryTreeBuilder {

	// Narazie olewamy dowiązania.
	// Należy dodać obsługę wyjątków, np braku dostępu do plików, itd.

	public DirectoryNode buildTree(String pathToRoot) {

		DirectoryNode root = DirectoryDFS(new File(pathToRoot));

		return root;
	}

	private DirectoryNode DirectoryDFS(File file) {

		DirectoryNode node = new DirectoryNode(file.getAbsolutePath());

		if (file.isFile()) {
			node.setSize(file.length());
		} else if (file.isDirectory()) {

			node.setDirectory(true);

			List<DirectoryNode> content = new ArrayList<DirectoryNode>();

			long size = 0;

			for (String s : file.list()) {

				s = file.getAbsolutePath() + File.pathSeparator + s;

				DirectoryNode child = DirectoryDFS(new File(s));
				size += child.getSize();

				content.add(child);

			}

			node.setSize(size);
			node.setContent(content);
		}

		return node;
	}

}
