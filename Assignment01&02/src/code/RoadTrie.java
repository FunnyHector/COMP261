package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a trie structure where all roads objects are stored.
 * It is indexed by their label + city (see Graph class, loadRoads() method).
 * @author Hector
 *
 */
public class RoadTrie {

	private RoadTriesNode root;

	public RoadTrie() {
		root = new RoadTriesNode();
	}

	/**
	 * Add a Road object according to its label and city.
	 * @param word    a String with road name stored inside
	 * @param road    the corresponding road object 
	 */
	public void add(String word, Road road) {
		RoadTriesNode node = root;
		word = word.trim();
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (node.children.get(c) == null) {
				node.children.put(c, new RoadTriesNode());
			}
			node = node.children.get(c);
		}
		node.isLabel = true;
		node.roads.add(road);
	}

	/**
	 * This method finds all roads that match the given prefix.
	 * @param word    the prefix to be searched
	 * @return    an ArrayList of Road objects that match the given prefix.
	 */
	public ArrayList<Road> find(String word) {
		RoadTriesNode node = root;
		word = word.trim();
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (node.children.get(c) == null) {
				return null;
			} else {
				node = node.children.get(c);
			}
		}
		// Use ArrayList instead of HashSet to keep the output in alphabet order
		// seems not working?
		ArrayList<Road> roads = new ArrayList<>();
		node.getAllLeaves(roads);
		return roads;
	}

	// for testing. not used
	public void traversal() {
		traversTrie(root);
	}
	
	// for testing. not used
	private void traversTrie(RoadTriesNode node) {
		if (node.isLabel){
			// System.out.println(node.road.label + ", " + node.road.city);
			if (node.children.isEmpty()) {
				return;
			}
		}
		for (RoadTriesNode childNode : node.children.values()) {
			traversTrie(childNode);
		}
	}

	/**
	 * A inner class representing the node of the trie.
	 * @author Hector
	 *
	 */
	public class RoadTriesNode {
		public boolean isLabel; // if this node is an end of a label
		Set<Road> roads;
		public Map<Character, RoadTriesNode> children;

		public RoadTriesNode() {
			isLabel = false;
			roads = new HashSet<>();
			children = new HashMap<Character, RoadTriesNode>();
		}

		/**
		 * This method finds all possible matches (all nodes with "true" tag)
		 * within the subtree of current node, and put them into an ArrayList,
		 * which is passed as argument.
		 * @param roads    a list of Road objects to store all roads found
		 */
		public void getAllLeaves(ArrayList<Road> roads) {
			// base case to exit recursion
		    if (this.isLabel && !this.roads.isEmpty()) {
				roads.addAll(this.roads);
			}
			for (RoadTriesNode node : this.children.values()) {
				node.getAllLeaves(roads);
			}
		}
	}
}
