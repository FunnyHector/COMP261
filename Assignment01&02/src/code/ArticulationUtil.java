package code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * This utility class provides a single method to find all articulation points
 * within a given collection of nodes, assuming nodes are connected as graph already. 
 * @author Hector
 *
 */
public class ArticulationUtil {

    // this field controls either to use iterative or recursive algorithm.
    public static final boolean ITERATIVE = true; // Default: iterative

    /**
     * This method finds all articulation points in the given node collection
     * 
     * @param nodes
     *            a collection of nodes within which this method will search for
     *            articulation points
     * @return a set of nodes containing all articulation points
     */
    public static Set<Node> findArticulationPoints(Collection<Node> nodes) {
        // make connections between between nodes, give them neighbours
        initialise(nodes);
        /*
         * Auckland map contains several disconnected sets. This set keeps track
         * of those nodes that are unvisited, so that all disconnected graphs
         * will be searched through.
         */
        Set<Node> unvisitedNodes = new HashSet<>(nodes);
        Set<Node> articulationPoints = new HashSet<>();

        while (!unvisitedNodes.isEmpty()) {
            int numSubtrees = 0;
            Node start = unvisitedNodes.iterator().next();
            start.depth = 0;
            for (Node neighbour : start.neighbours) {
                if (neighbour.depth == Node.MAX_DEPTH) {
                    if (ITERATIVE) {
                        iterArtPts(neighbour, start, articulationPoints, unvisitedNodes);
                    } else {
                        recArtPts(neighbour, 1, start, articulationPoints, unvisitedNodes);
                    }
                    numSubtrees++;
                }
            }

            if (numSubtrees > 1) {
                articulationPoints.add(start);
            }

            unvisitedNodes.remove(start);
        }
        // could return empty set
        return articulationPoints;
    }

    /**
     * This method adds all neighbour nodes into each node, make connections
     * between every node and its neighbours. (For articulation point feature)
     * 
     * @param nodes
     *            a collection of nodes within which this method will search for
     *            articulation points
     */
    private static void initialise(Collection<Node> nodes) {
        for (Node node : nodes) {
            node.setNeighbours();
        }
    }

    /**
     * Recursive version of articulation points search
     * 
     * @param node   
     * @param depth
     * @param from
     * @param artPoints
     * @param unvisitedNodes
     * @return
     */
    private static int recArtPts(Node node, int depth, Node from, Set<Node> artPoints, Set<Node> unvisitedNodes) {
        node.depth = depth;
        int reachBack = depth;

        for (Node neighbour : node.neighbours) {
            if (neighbour.nodeID == from.nodeID) {
                continue;
            }

            if (neighbour.depth < Node.MAX_DEPTH) {
                reachBack = Math.min(reachBack, neighbour.depth);
            } else {
                int childReach = recArtPts(neighbour, depth + 1, node, artPoints, unvisitedNodes);
                if (childReach >= depth) {
                    artPoints.add(node);
                }
                reachBack = Math.min(reachBack, childReach);
            }

            unvisitedNodes.remove(neighbour);
        }
        
        return reachBack;
    }

    /**
     * Iterative version of articulation points search
     * 
     * @param firstNode
     * @param root
     * @param artPoints
     * @param unvisitedNodes
     */
    private static void iterArtPts(Node firstNode, Node root, Set<Node> artPoints, Set<Node> unvisitedNodes) {
        Stack<IteArtStackElement> stack = new Stack<>();
        stack.push(new IteArtStackElement(firstNode, 1, new IteArtStackElement(root, 0, null)));

        while (!stack.isEmpty()) {
            IteArtStackElement elem = stack.peek();
            Node node = elem.node;
            if (elem.children == null) { // first time
                node.depth = elem.reach = elem.depth;
                elem.children = new ArrayList<>();
                for (Node neighbour : node.neighbours) {
                    if (neighbour.nodeID != elem.parent.node.nodeID) {
                        elem.children.add(neighbour);
                    }
                }
            } else if (!elem.children.isEmpty()) {  // children to process
                Node child = elem.children.remove(0);
                if (child.depth < Node.MAX_DEPTH) {
                    elem.reach = Math.min(elem.reach, child.depth);
                } else {
                    stack.push(new IteArtStackElement(child, node.depth + 1, elem));
                }
            } else {  // last time
                if (node.nodeID != firstNode.nodeID) {
                    if (elem.reach >= elem.parent.depth) {
                        artPoints.add(elem.parent.node);
                    }
                    elem.parent.reach = Math.min(elem.parent.reach, elem.reach);
                }
                stack.pop();
                unvisitedNodes.remove(elem.node);
            }
        }
    }
    
    /**
     * This class represents the object pushed into the stack in the iterative version of
     * articulation points algorithm.
     * @author Hector
     *
     */
    private static class IteArtStackElement {

        Node node;
        int reach;
        IteArtStackElement parent;
        int depth;
        List<Node> children;

        public IteArtStackElement(Node node, int depth, IteArtStackElement parent) {
            this.node = node;
            this.reach = Integer.MAX_VALUE;
            this.parent = parent;
            this.depth = depth;
            this.children = null;
        }
    }
    
}
