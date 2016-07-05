package code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents the segment in roads.
 * @author Hector
 *
 */
public class Segment {

    public final int roadID;
    public Node start, end;
    public final Road road;
    public final double length;
    // a list of all coordinates, with node_1's location being the first element
    // and node_2's location being the last
    public final List<Location> coords;
    private Color color;
    // default color
    public static final Color DEFAULT_COLOUR = new Color(130, 130, 130);
    public static final Color SELECT_COLOUR = new Color(255, 192, 0);
    public static final Color NAVI_COLOUR = new Color(255, 0, 0);

    /**
     * Constructor
     * @param line
     * @param nodeMap
     * @param roadMap 
     */
    public Segment(String line, Map<Integer, Node> nodeMap, Map<Integer, Road> roadMap) {
        this.coords = new ArrayList<>();
        String[] values = line.split("\t");
        this.roadID = Integer.parseInt(values[0]);
        this.road = roadMap.get(roadID);
        this.length = Double.parseDouble(values[1]);
        start = nodeMap.get(Integer.parseInt(values[2]));
        // add node_1's location as the first element in ArrayList
        coords.add(start.location); 
        end = nodeMap.get(Integer.parseInt(values[3]));
        // add all coordinates into the ArrayList
        for (int index = 4; index < values.length;) {
            double lat = Double.parseDouble(values[index++]);
            double lon = Double.parseDouble(values[index++]);
            coords.add(Location.newFromLatLon(lat, lon));
        }
        // add node_2's location as the last element in ArrayList
        coords.add(end.location);
        color = DEFAULT_COLOUR;
    }

    /**
     * This method draws the segment.
     * @param g    the passed Graphics object
     * @param currentOrigin    the current origin Location
     * @param currentScale    the current scale
     * @param d    the current dimension of the display panel
     */
    public void draw(Graphics g, Location currentOrigin, double currentScale, Dimension d) {
        // the coordinate of display panel center
        int centrXCoord = (int) (d.getWidth() / 2);
        int centrYCoord = (int) (d.getHeight() / 2);
        g.setColor(this.color);
        for (int i = 0; i < coords.size() - 1; i++) {
            // Location -> Point, and shift the point toward bottom-right
            Point point_i = coords.get(i).asPoint(currentOrigin, currentScale);
            Point newPoint_i = new Point(point_i.x + centrXCoord, point_i.y + centrYCoord);
            Point point_iNext = coords.get(i + 1).asPoint(currentOrigin, currentScale);
            Point newPoint_iNext = new Point(point_iNext.x + centrXCoord, point_iNext.y + centrYCoord);
            
            // for efficiency, don't render segments that are off-screen.
            if ((newPoint_i.x < 0 && newPoint_iNext.x < 0)
                    || (newPoint_i.x > d.width && newPoint_iNext.x > d.width)
                    || (newPoint_i.y < 0 && newPoint_iNext.y < 0)
                    || (newPoint_i.y > d.height && newPoint_iNext.y > d.height)) {
                continue;
            }
            
            g.drawLine(newPoint_i.x, newPoint_i.y, newPoint_iNext.x, newPoint_iNext.y);
        }
    }

    /**
     * This method sets the color of the segment. Is used to highlight the 
     * selected road
     * @param color    the colour to change to
     */
    public void setColor(Color color) {
    	this.color = color;
    }
    
    /**
     * Given one end of this segment, this method returns the other end. 
     * If the given node is not either end, this method returns null.
     * @param oneEnd    possible end of this segment
     * @return    the other end if the given end is one of two ends. 
     *             returns null if the given end is neither.
     */
    public Node theOtherEnd(Node oneEnd) {
        if (oneEnd.nodeID != start.nodeID && oneEnd.nodeID != end.nodeID) {
            return null;
        }
        
        if (oneEnd.nodeID == start.nodeID) {
            return end;
        } else {
            return start;
        }
    }

    @Override
    public String toString() {
        return "Segment [roadID=" + roadID + ", start=" + start.nodeID + ", end=" + end.nodeID
                + ", road=" + road.roadID + ", coords=" + coords.toString() + "]";
    }
}
