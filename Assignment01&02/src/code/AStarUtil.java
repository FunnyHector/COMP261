package code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * This utility class provides a public method to find a guaranteed shorted path
 * between two nodes, using A* algorithm.
 * @author Hector
 *
 */
public class AStarUtil {
    
    /**
     * A static method to find a guaranteed shorted path between two nodes 
     * using A* algorithm
     * @param start   the start point
     * @param end     the destination
     * @param restrictions 
     * @return    returns a list of Segment objects, all segments within 
     *            are the path from start to end. Returns an empty List 
     *            If no path can be found 
     */
    public static List<Segment> findPath(Node start, Node end, 
            Set<Restriction> restrictions, boolean distanceFirst) {
        // a collection storing all nodes that are visited
        HashSet<Integer> visitedNodes = new HashSet<>();
        PriorityQueue<AStarNode> fringe = new PriorityQueue<>();

        // enqueue the start node
        fringe.offer(new AStarNode(start, null, end, distanceFirst));
        
        while (!fringe.isEmpty()) {
            AStarNode polledNode = fringe.poll();
            // mark it as visited by adding it into visited set.
            visitedNodes.add(polledNode.node.nodeID);
            
            // if the end node is dequeued, the path is found
            if (polledNode.node.nodeID == end.nodeID) {
                List<Segment> path = new ArrayList<>();
                AStarNode backTracer = polledNode;
                // back trace it from end to start.
                while (backTracer.cameFrom != null){
                    path.add(backTracer.edge);
                    backTracer = backTracer.cameFrom;
                }
                
                Collections.reverse(path);
                
                return path;
            }
            
            Node oneEnd = polledNode.node;
            outer: for (Segment seg : polledNode.node.joinedSegments) {
                Node theOtherEnd = seg.theOtherEnd(oneEnd);
                
                // check if this segment is not for car, if so, don't enqueue this node
                if (seg.road.notForCar) {
                    continue;
                }
                
                // check if from this segment is one-way only, if so, don't enqueue this node
                if (isOneWay(oneEnd, seg, theOtherEnd)) {
                    continue;
                }
                
                // check if this node is in restriction list, if so, don't enqueue this node
                for (Restriction restr : restrictions) {
                    if (isInRestriction(polledNode, oneEnd, seg, theOtherEnd, restr)) {
                        continue outer;
                    }
                }

                if (!visitedNodes.contains(theOtherEnd.nodeID)) { // filter out those visited
                    AStarNode neighbour = new AStarNode(theOtherEnd, polledNode, end, distanceFirst);
                    // a flag to mark if this neighbor is updated or added directly
                    boolean isInFringe = false;  
                    for (AStarNode nodeAInFringe : fringe) {
                        if (nodeAInFringe.node.nodeID == theOtherEnd.nodeID) {
                            // if already in fringe, see if its cost needs to be updated
                            compareAndUpdate(neighbour, nodeAInFringe, distanceFirst);
                            isInFringe = true;
                            break;
                        }
                    }
                    
                    // if not in fringe, add it in.
                    if (!isInFringe) {
                        fringe.offer(neighbour);
                    }
                }
            }
        }
        // if cannot find a path, return an empty list
        return new ArrayList<>();
    }

    /**
     * This method check if the next-to-be-enqueued node is one of restrictions.
     * @param polledNode
     * @param oneEnd
     * @param seg
     * @param theOtherEnd
     * @param restr
     * @return    
     */
    private static boolean isInRestriction(AStarNode polledNode, Node oneEnd, Segment seg, Node theOtherEnd,
            Restriction restr) {
        return oneEnd.nodeID == restr.rstrNode.nodeID
                && polledNode.cameFrom != null && polledNode.edge != null
                && polledNode.cameFrom.node.nodeID == restr.nodeFrom.nodeID
                && polledNode.edge.roadID == restr.roadFrom.roadID
                && seg.roadID == restr.roadTo.roadID
                && theOtherEnd.nodeID == restr.nodeTo.nodeID;
    }

    /**
     * This method check if the next-to-be-enqueued node in on the other end 
     * of a one-way only road.
     * @param oneEnd
     * @param seg
     * @param theOtherEnd
     * @return
     */
    private static boolean isOneWay(Node oneEnd, Segment seg, Node theOtherEnd) {
        // for debug
        // System.out.println("one-way encountered: roadID: " + seg.road.roadID
        //         + ". road name: " + seg.road.label + ", " + seg.road.city
        //         + ". from Node: " + oneEnd.nodeID + ". to node: " + theOtherEnd.nodeID);
        return seg.road.isOneWay && oneEnd.nodeID == seg.end.nodeID 
                && theOtherEnd.nodeID == seg.start.nodeID;
    }

    private static void compareAndUpdate(AStarNode neighbour, AStarNode nodeAInFringe, boolean distanceFirst) {
        if (neighbour.costFromStart < nodeAInFringe.costFromStart) {
            nodeAInFringe.cameFrom = neighbour.cameFrom;
            nodeAInFringe.setEdge();
            if (distanceFirst) {
                nodeAInFringe.updateCostFromStartDist();                
            } else {
                nodeAInFringe.updateCostFromStartTime();
            }
        }
    }
}
