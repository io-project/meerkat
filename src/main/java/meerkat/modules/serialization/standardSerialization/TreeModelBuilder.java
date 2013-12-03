package meerkat.modules.serialization.standardSerialization;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

public class TreeModelBuilder {
	TreeModel buildTreeModel(DirectoryNode node) {
		return new DefaultTreeModel(DFS(node));
	}
	
	private DefaultMutableTreeNode DFS(DirectoryNode node) {
		 DefaultMutableTreeNode treeNode =
			        new DefaultMutableTreeNode(node.getRelativePath());
		 
		 for(DirectoryNode n : node.getContent())
			 	treeNode.add(DFS(n));
		 
		 return treeNode;
	}
	
}
