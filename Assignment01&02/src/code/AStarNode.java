package code;

/**
 * This class represents the object used in the priority queue in A* algorithm.
 * This class provides two sets of methods to calculate the cost to here and 
 * the heuristic cost to the destination, one is used when the minimum distance
 * is the top priority in path-finding, the other is used when the minimum time
 * is the top priority in path-finding.
 * 
 * @author Hector
 *
 */
public class AStarNode implements Comparable<AStarNode> {
    
    Node node;
    AStarNode cameFrom;
    Segment edge;
    double costFromStart = 0;
    double heuristicCostToEnd = 0;
    
    /**
     * Constructor. The Cost from start till here, and the heuristic cost to the end
     * is initialised or updated during the construction.
     * @param node
     * @param fromNode
     * @param target
     * @param distanceFirst    --- true means it's constructing the object assuming
     *                         distance-first, false means time-first
     */
    public AStarNode(Node node, AStarNode fromNode, Node target, boolean distanceFirst) {
        this.node = node;
        this.cameFrom = fromNode;
        this.setEdge();
        
        if (distanceFirst) {
            this.updateCostFromStartDist();
            this.setHeuristicCostDist(target);
        } else {
            this.updateCostFromStartTime();
            this.setHeuristicCostTime(target);
        }
    }
    
    /**
     * This method find out the edge between this node and the node that we came from
     */
    public void setEdge(){
        if (this.node == null || this.cameFrom == null) {
            return;
        }
        for (Segment s : this.node.joinedSegments) {
            if (this.cameFrom.node.joinedSegments.contains(s)) {
                this.edge = s;
                return;
            }
        } 
    }
    
    /**
     * This method updates the cost from start to this node, 
     * when distance is the priority.
     */
    public void updateCostFromStartDist() {
        if (cameFrom == null) {
            this.costFromStart = 0;
        } else {
            this.costFromStart = this.edge.length + this.cameFrom.costFromStart;
        }
    }
    
    /**
     * This method estimate the heuristic cost from this node to 
     * the end node by calculating their euclidean distance.
     * 
     * @param end   --- the target node.
     */
    public void setHeuristicCostDist(Node end) {
        this.heuristicCostToEnd = this.node.location.distance(end.location);
    }
    
    /**
     * This method updates the cost from start to this node, 
     * when time is the priority.
     */
    public void updateCostFromStartTime() {
        if (cameFrom == null) {
            this.costFromStart = 0;
        } else {
            this.costFromStart = (this.edge.length / estiVelocity(this.edge) )+ this.cameFrom.costFromStart;
        }
    }
    
    /**
     * This method takes the speed limit and road class as factors, and 
     * estimates the speed that a car can most likely to drive at.
     * 
     * Estimated speed = speed limit * roadClass factor
     * Here the road class factor is set to be not very predominant, see table below.
     * 
     * road class --|-- road class factor
     * -----------------------------------
     *        0     |        0.80
     *        1     |        0.85
     *        2     |        0.90
     *        3     |        0.95
     *        4     |        1.00
     * 
     * @param edge
     * @return
     */
    private double estiVelocity(Segment edge) {
        return edge.road.roadClass * edge.road.speedLimit;
    }

    /**
     * This method estimate the heuristic cost from this node to the end node 
     * by dividing their euclidean distance by maximum driving speed 120km/h.
     * 
     * Heuristic Cost = Euclidean distance / 120km/h
     * 
     * @param end   --- the target node.
     */
    public void setHeuristicCostTime(Node end) {
        this.heuristicCostToEnd = (this.node.location.distance(end.location)) / 120;
    }

    @Override
    public String toString() {
        String nodeString = node == null ? "null" : "" + node.nodeID;
        String cameFromString = cameFrom == null ? "null" : "" + cameFrom.node.nodeID;
        String edgeString = edge == null ? "null" : "" + edge.roadID;
        
        return "AStarNode [node=" + nodeString + ", cameFrom=" + cameFromString + ", edge="
                + edgeString + ", costFromStart=" + costFromStart + ", heuristicCostToEnd=" + heuristicCostToEnd + "]";
    }
    
    @Override
    public int compareTo(AStarNode other) {
        double costNode1 = this.costFromStart + this.heuristicCostToEnd;
        double costNode2 = other.costFromStart + other.heuristicCostToEnd;
        if (costNode1 > costNode2) {
            return 1;
        } else if (costNode1 < costNode2) {
            return -1;
        } else {
            return 0;
        }
    }
}
