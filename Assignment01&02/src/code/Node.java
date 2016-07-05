package code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the intersection of two roads.
 * @author Hector
 *
 */
public class Node {

    public final int nodeID;
    public final Location location;
    public final Set<Segment> joinedSegments;
    private Color color;
    // some colors
    public static final Color DEFAULT_COLOUR = new Color(83, 141, 213);  // default: light blue
    public static final Color SELECT_COLOUR = new Color(255, 192, 0);  // selected: orange
    public static final Color NAVI_COLOUR = new Color(255, 0, 0);  // navigation: red
    public static final Color ARTICULATION_COLOUR = new Color(204, 0, 255);   // articulation: purple
    // for articulation points
    public int depth;
    public Set<Node> neighbours;
    public static final int MAX_DEPTH = Integer.MAX_VALUE;
    /*
     * these two constants define the size of the node squares at different zoom
     * levels; the equation used is node size = NODE_INTERCEPT + NODE_GRADIENT *
     * log(scale)
     */
    public static final int NODE_INTERCEPT = 1;
    public static final double NODE_GRADIENT = 0.8;
    
    /**
     * Constructor. Note that the field joinedSegments is empty when
     * an object is constructed
     */
    public Node(String line) {
        String[] values = line.split("\t");
        this.nodeID = Integer.parseInt(values[0]);
        // translate latitude and longitude to location
        double lat = Double.parseDouble(values[1]);
        double lon = Double.parseDouble(values[2]);
        this.location = Location.newFromLatLon(lat, lon);
        this.color = DEFAULT_COLOUR;  // default colour for nodes
        joinedSegments = new HashSet<>();
        // for articulation points
        this.depth = MAX_DEPTH;
        neighbours = new HashSet<Node>();
    }

    /**
     * This method draws the node.
     * @param g    the passed Graphics object
     * @param currentOrigin    the current origin Location
     * @param currentScale    the current scale
     * @param d    the current dimension of the display panel
     */
    public void draw(Graphics g, Location currentOrigin, double currentScale, Dimension d) {
      
        // the coordinate of display panel center
        int centrXCoord = (int) (d.getWidth() / 2);
        int centrYCoord = (int) (d.getHeight() / 2);
        // location -> point
        Point p = this.location.asPoint(currentOrigin, currentScale);
        // move the point towards right-bottom, so that zoom-in and out is nicer
        Point newP = new Point(p.x + centrXCoord, p.y + centrYCoord);
        
        // for efficiency, don't render nodes that are off-screen.
        if (newP.x < 0 || newP.x > d.width || newP.y < 0 || newP.y > d.height) {
            return;
        }
        
        int size = (int) (NODE_GRADIENT * Math.log(currentScale) + NODE_INTERCEPT);
        g.setColor(this.color);
        g.fillOval(newP.x - size / 2, newP.y - size / 2, size, size);
    }

    /**
     * This method sets the color of the node. Is used to highlight the 
     * selected intersection
     * @param color    the colour to change to
     */
    public void setColor(Color color) {
    	this.color = color;
    }

    /**
     * This method adds all neighbour nodes into this node, make connections
     * between this node and its neighbours.
     * === for articulation point feature ===
     */
    public void setNeighbours() {
        for (Segment seg : this.joinedSegments) {
            Node theOtherEnd = seg.theOtherEnd(this);
            neighbours.add(theOtherEnd);
        }
    }

    @Override
    public String toString() {
        String nbrs = "";
        int i = 1;
        for (Node n : neighbours) {
            nbrs += i + "): " + n.nodeID + ". ";
            i++;
        }
        
        return "Node [nodeID=" + nodeID + ", neighbours=" + nbrs + "]";
    }
}
