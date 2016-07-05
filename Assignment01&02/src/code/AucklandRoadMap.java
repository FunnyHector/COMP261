package code;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A digital map of Auckland. It has some basic features such as panning,
 * zooming, searching, and dragging.
 * 
 * PS:Quad-tree is still in the waiting list. Gotta make some time to finish it.
 * 
 * @author Hector
 *
 */
public class AucklandRoadMap extends GUI {

    Graph graph;

    // the center of Auckland City according to Google Maps, as
    // latitude-longitude
    private static final double CENTRE_LAT = -36.847622;
    private static final double CENTRE_LON = 174.763444;
    // the center of Auckland City as a Location object.
    private static final Location CENTRE = Location.newFromLatLon(CENTRE_LAT, CENTRE_LON);

    private Location currentOrigin; // the location used as origin.
    private double currentScale; // the scale factor
    
    // defines how much you zoom in/out per button press, and the maximum and
    // minimum zoom levels.
    private static final double ZOOM_FACTOR = 1.3;
    private static final double MIN_ZOOM = 5, MAX_ZOOM = 800;

    // the node that is clicked-on
    private Node selectedNode;
    
    // the road(s) that matches the search input
    private ArrayList<Road> selectedRoad;

    // where the drag started, as Location
    private Location locationOnDragStart;
    
    // start and end intersection for navigation, and the path between them
    private Node startNode;
    private Node endNode;
    private List<Segment> pathFound;
    
    // for articulation
    boolean isDisplayingArtPts;
    private Set<Node> articulationPoints;
    
    // a flag switch between distance-first or time-first. Default is distance-first.
    boolean distanceFirst;
    
    /**
     * Constructor. Initialise the fields, and set a good scale to display.
     */
    public AucklandRoadMap() {
        graph = new Graph();
        currentOrigin = CENTRE; // use center of Auckland as the origin.
        Dimension d = getDrawingAreaDimension();
        // after several tests, 50 is a good choice for the scale
        currentScale = Math.max(d.getHeight(), d.getWidth()) / 50;
        selectedNode = null;
        selectedRoad = new ArrayList<>();
        locationOnDragStart = null;
        startNode = null;
        endNode = null;
        pathFound = new ArrayList<>();
        articulationPoints = new HashSet<>();
        isDisplayingArtPts = false;
        distanceFirst = true;
    }

    @Override
    protected void redraw(Graphics g) {
        Dimension d = getDrawingAreaDimension();
        graph.redraw(g, currentOrigin, currentScale, d);
    }

    @Override
    protected void onPressed(MouseEvent e) {
        Point pointOnDragStart = e.getPoint();
        locationOnDragStart = Location.newFromPoint(pointOnDragStart, currentOrigin, currentScale);
    }

    @Override
    protected void onReleased(MouseEvent e) {

        Point pointOnDragEnd = e.getPoint();
        Location locationOnDragEnd = Location.newFromPoint(pointOnDragEnd, currentOrigin, currentScale);

        // Relative distance moved
        double deltaX = locationOnDragStart.x - locationOnDragEnd.x;
        double deltaY = locationOnDragStart.y - locationOnDragEnd.y;

        // update the origin point
        currentOrigin = currentOrigin.moveBy(deltaX, deltaY);

        // how much pixels need to be adjusted
        Dimension d = this.getDrawingAreaDimension();
        int centrXCoord = (int) (d.getWidth() / 2);
        int centrYCoord = (int) (d.getHeight() / 2);

        // get the location where the click happened, and adjust it to left-top
        Point pointOnClick = e.getPoint();
        Point adjustedPointOnClick = new Point(pointOnClick.x - centrXCoord, pointOnClick.y - centrYCoord);
        Location locationOnClick = Location.newFromPoint(adjustedPointOnClick, currentOrigin, currentScale);
        Node nodeOnClick = graph.findNode(locationOnClick); 

        if (nodeOnClick != null) {
            // Selecting node (not for navigation)
            if (!isSelectingStartNode() && !isSelectingEndNode()) {
                selectNode(nodeOnClick);
            // Selecting the start node for navigation
            } else if (isSelectingStartNode()) {
                selectStart(nodeOnClick);
            // Selecting the end node for navigation
            } else if (isSelectingEndNode()) {
                selectEnd(nodeOnClick);
            }
        } else {
            // if no node is found, display nothing
            this.getTextOutputArea().setText(null);
        }
        
        // Find a path between start and end intersections using a*
        if (startNode != null && endNode != null) {
            findPath();
        }
    }
    
    /**
     * This method selects the node that is clicked on, highlight it, and display
     * some relevant information in the text area.
     * @param nodeOnClick
     */
    private void selectNode(Node nodeOnClick) {
        // set previously selected node as default color
        if (selectedNode != null) {
            selectedNode.setColor(Node.DEFAULT_COLOUR);
        }
        if (startNode != null) {
            startNode.setColor(Node.DEFAULT_COLOUR);
            startNode = null;
        }
        if (endNode != null) {
            endNode.setColor(Node.DEFAULT_COLOUR);
            endNode = null;
        }
        if (!pathFound.isEmpty()) {
            for (Segment seg : pathFound) {
                seg.setColor(Segment.DEFAULT_COLOUR);
            }
        }
        // update this field and set the color to highlight it
        selectedNode = nodeOnClick;
        selectedNode.setColor(Node.SELECT_COLOUR);
        displayNode(selectedNode);
    }

    /**
     * This method display some information in the text area at bottom
     * about the intersection being selected.
     * @param nodeOnClick   the intersection being selected
     */
    private void displayNode(Node nodeOnClick) {
        String lineSeparator = System.lineSeparator();
        StringBuffer sb = new StringBuffer("Selected node ID: " + nodeOnClick.nodeID
                + lineSeparator + "Roads at this intersection: " + lineSeparator);

        Set<String> rSet = new HashSet<>();
        for (Segment seg : nodeOnClick.joinedSegments) {
            String s = seg.road.label + ", " + seg.road.city + ", road ID: " + seg.roadID;
            if (!rSet.contains(s)) {
                rSet.add(s);
            }
        }
        
        int i = 1; // just a number for each road that connects to this node
        for (String s : rSet) {
            // some string appending, for a nicer text output
            sb.append("(").append(i++).append(") ").append(s).append(lineSeparator);
        }

        this.getTextOutputArea().setText(sb.toString());
    }

    /**
     * This method selects the start node for navigation, and highlight it.
     * @param nodeOnClick
     */
    private void selectStart(Node nodeOnClick) {
        // set previously selected navigation start node as default color
        if (startNode != null) {
            startNode.setColor(Node.DEFAULT_COLOUR);
        }
        if (selectedNode != null) {
            selectedNode.setColor(Node.DEFAULT_COLOUR);
            selectedNode = null;
        }
        // update this field and set the color to red to highlight it
        startNode = nodeOnClick;
        startNode.setColor(Node.NAVI_COLOUR);
    }

    /**
     * This method selects the end node for navigation, and highlight it.
     * @param nodeOnClick
     */
    private void selectEnd(Node nodeOnClick) {
        // set previously selected navigation start node as default color
        if (endNode != null) {
            endNode.setColor(Node.DEFAULT_COLOUR);
        }
        if (selectedNode != null) {
            selectedNode.setColor(Node.DEFAULT_COLOUR);
            selectedNode = null;
        }
        // update this field and set the color to red to highlight it
        endNode = nodeOnClick;
        endNode.setColor(Node.NAVI_COLOUR);
    }
    
    /**
     * This method finds the path between start node and end node.
     */
    private void findPath() {
        // set the displaying navigation path to default colour
        if (!pathFound.isEmpty()) {
            for (Segment seg : pathFound) {
                seg.setColor(Segment.DEFAULT_COLOUR);
            }
            pathFound.clear();
        }
        
        pathFound = AStarUtil.findPath(startNode, endNode, graph.restrictions, distanceFirst);
        // highlight the path
        if (!pathFound.isEmpty()) {
            for (Segment seg : pathFound) {
                seg.setColor(Segment.NAVI_COLOUR);
            }
        }
        displayNavigationInfo(pathFound);
    }
    
    /**
     * Display some information for the navigation in text area
     * @param startNode
     * @param endNode
     * @param path
     */
    private void displayNavigationInfo(List<Segment> path) {
        if (path == null || path.isEmpty()) {
            return;
        }
        
        String lineSeparator = System.lineSeparator();
        ArrayList<String> strings = new ArrayList<>();
        double totalLengthinOneRoad = 0;
        double totalLengthOfPath = 0;
        
        // add the first segment
        strings.add(path.get(0).road.label + ": " + 
                String.format("%.3f", path.get(0).length) + "km" + lineSeparator);
        totalLengthOfPath += path.get(0).length;
        
        for (int i = 1; i < path.size(); i++) {
            if (!path.get(i).road.label.equals(path.get(i-1).road.label)) {
                // remove duplicates
                totalLengthinOneRoad = path.get(i).length;
                strings.add(path.get(i).road.label + ": " + 
                        String.format("%.3f", totalLengthinOneRoad) + "km" + lineSeparator);
            } else {
                totalLengthinOneRoad += path.get(i).length;
                strings.set(strings.size() - 1, path.get(i).road.label + ": " + 
                        String.format("%.3f", totalLengthinOneRoad) + "km" + lineSeparator);
            }
            totalLengthOfPath += path.get(i).length;
        }
        
        StringBuffer sbuffer = new StringBuffer();
        sbuffer.append("Navigation:").append(lineSeparator)
            .append("============================").append(lineSeparator);
        for (String s : strings) {
            sbuffer.append(s);
        }
        sbuffer.append("============================").append(lineSeparator)
            .append("Total distance = " + String.format("%.3f", totalLengthOfPath) + "km");     
        
        this.getTextOutputArea().setText(sbuffer.toString());  
    }

    @Override
    protected void onScroll(MouseWheelEvent e) {
        int i = e.getWheelRotation();
        if (i > 0) {
            onMove(Move.ZOOM_OUT);
        } else {
            onMove(Move.ZOOM_IN);
        }
    }
    
    @Override
    protected void findArticulationPoints(){
        if (!articulationPoints.isEmpty()) {
            if (isDisplayingArtPts) {
                for (Node n : articulationPoints) {
                    n.setColor(Node.DEFAULT_COLOUR); 
                }
                isDisplayingArtPts = !isDisplayingArtPts;
            } else {
                for (Node n : articulationPoints) {
                    n.setColor(Node.ARTICULATION_COLOUR); 
                }
                isDisplayingArtPts = !isDisplayingArtPts;
            }
            this.getTextOutputArea()
                .setText("Number of Articulation points: " + articulationPoints.size()); 
            
            return;
        }
        
        articulationPoints = graph.findArticulationPoints();
        
        this.getTextOutputArea()
        .setText("Number of Articulation points: " + articulationPoints.size()); 
        
        if (!articulationPoints.isEmpty()) {
            for (Node n : articulationPoints) {
                n.setColor(Node.ARTICULATION_COLOUR);
            }
            isDisplayingArtPts = !isDisplayingArtPts;
        }
    }

    @Override
    protected void onSearch() {
        String str = getSearchBox().getText();
        ArrayList<Road> roadsFound = graph.searchRoad(str);
        // if nothing found or roadsFound is empty.
        if (roadsFound == null || roadsFound.isEmpty()) {
            return;
        }
        // set previously selected roads back to original color, black
        if (!selectedRoad.isEmpty()) {
            for (Road road : selectedRoad) {
                for (Segment seg : road.roadSegments) {
                    seg.setColor(Segment.DEFAULT_COLOUR);
                }
            }
        }
        // highlight
        selectedRoad = roadsFound;
        for (Road road : selectedRoad) {
            for (Segment seg : road.roadSegments) {
                seg.setColor(Segment.SELECT_COLOUR);
            }
        }
        displayRoad(selectedRoad);
    }

    /**
     * This method display some information in the text area at bottom
     * about the intersection being selected.
     * @param selectedRoad   a list of road(s) that matches the input text in search box.
     */
    private void displayRoad(ArrayList<Road> selectedRoad) {
        String lineSeparator = System.lineSeparator();
        StringBuffer sb = new StringBuffer("Road(s) found:" + selectedRoad.size() + lineSeparator);

        for (Road road : selectedRoad) {
            sb.append("RoadID: ").append(road.roadID).append(", Road Name: ").append(road.label).append(", City: ")
                    .append(road.city).append(lineSeparator);
        }

        this.getTextOutputArea().setText(sb.toString());
    }

    @Override
    protected void onMove(Move m) {
        Dimension d = getDrawingAreaDimension();

        // after several test, 10 is preferable.
        if (m == Move.WEST) {
            currentOrigin = currentOrigin.moveBy(-(d.getWidth() / currentScale) / 10, 0);
        } else if (m == Move.EAST) {
            currentOrigin = currentOrigin.moveBy(d.getWidth() / currentScale / 10, 0);
        } else if (m == Move.NORTH) {
            currentOrigin = currentOrigin.moveBy(0, d.getHeight() / currentScale / 10);
        } else if (m == Move.SOUTH) {
            currentOrigin = currentOrigin.moveBy(0, -(d.getHeight() / currentScale) / 10);
        } else if (m == Move.ZOOM_IN) {
            if (currentScale < MAX_ZOOM) {
                currentScale *= ZOOM_FACTOR;
            }
        } else if (m == Move.ZOOM_OUT) {
            if (currentScale > MIN_ZOOM) {
                currentScale /= ZOOM_FACTOR;
            }
        }
    }

    @Override
    protected void onLoad(File nodes, File roads, File segments, File polygons, File restrictions) {
        currentOrigin = CENTRE; // use center of Auckland as the origin.
        Dimension d = getDrawingAreaDimension();
        // after several tests, 50 is a good choice for the scale
        currentScale = Math.max(d.getHeight(), d.getWidth()) / 50;
        // reset all fields
        selectedNode = null;
        selectedRoad = new ArrayList<>();
        locationOnDragStart = null;
        startNode = null;
        endNode = null;
        pathFound = new ArrayList<>();
        articulationPoints = new HashSet<>();
        isDisplayingArtPts = false;
        
        graph.load(nodes, roads, segments, polygons, restrictions);
    }
    
    @Override
    protected void switchTimeDistance(){
        distanceFirst = !distanceFirst;
        findPath();
    }

    public static void main(String[] args) {
        new AucklandRoadMap();
    }
}
