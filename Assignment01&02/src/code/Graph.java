package code;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;

/**
 * This class represents the graph of intersections and road segments of Auckland.
 * @author Hector
 *
 */
public class Graph {

    // collections of nodes, roads, segments, polygons
    Map<Integer, Node> nodeMap;
    Map<Integer, Road> roadMap;
    Set<Segment> segmentSet;
    Set<Polygon> polygonSet;
    
    // a trie structure of roads
    RoadTrie roadTries;
    
    // Restriction nodes
    Set<Restriction> restrictions;
    
    // how far away from a node you can click before it isn't counted.
    public static final double MAX_CLICKED_DISTANCE = 0.15;
    
    // quad-tree of nodes to be implemented
    // QuadTree nodeQuadTree;

    public Graph() {
        nodeMap = new HashMap<>();
        roadMap = new HashMap<>();
        polygonSet = new HashSet<>();
        segmentSet = new HashSet<>();
        roadTries = new RoadTrie();
        restrictions = new HashSet<>();
    }

    /**
     * This is a wrapper method that loads data from files.
     * @param nodes    the file contains information of nodes
     * @param roads    the file contains information of roads
     * @param segments    the file contains information of segments
     * @param polygons    the file contains information of polygons
     */
    public void load(File nodes, File roads, File segments, File polygons, File restrfile) {
        nodeMap = new HashMap<>();
        roadMap = new HashMap<>();
        polygonSet = new HashSet<>();
        segmentSet = new HashSet<>();
        roadTries = new RoadTrie();
        restrictions = new HashSet<>();

        // delegate to several dedicated methods to load file
        loadNodes(nodes);
        loadRoads(roads);
        loadSegments(segments);
        if (polygons != null) {
            loadPolygons(polygons);
        }
        if (restrictions != null) {
            loadRestrictions(restrfile);            
        }
    }

    /**
     * This method reads from the node file, creates Node objects and store them in
     * a HashMap, where the key is nodeID, and the value is the Node object. 
     * @param file   the file contains information of nodes
     */
    private void loadNodes(File file) {
        BufferedReader bfReader;
        try {
            bfReader = new BufferedReader(new FileReader(file));
            String line = bfReader.readLine();
            while (line != null) {
                Node node = new Node(line);
                nodeMap.put(node.nodeID, node);
                
                // TODO load it into Quad tree
                
                line = bfReader.readLine();
            }
            bfReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + file + " Not Found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    /**
     * This method reads from the road file, creates Road objects and store them in
     * a HashMap, where the key is roadID and the value is the Road object, and a 
     * Trie, where the format of the string is: "road.label, road.city". 
     * @param file    the file contains information of roads
     */
    private void loadRoads(File file) {
        BufferedReader bfReader;
        try {
            bfReader = new BufferedReader(new FileReader(file));
            bfReader.readLine();   // throw away the first line, labels
            String line = bfReader.readLine();
            while (line != null) {
                Road road = new Road(line);
                roadMap.put(road.roadID, road);
                // load it into trie
                roadTries.add(road.label + ", " + road.city, road);
                line = bfReader.readLine();
            }
            bfReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + file + " Not Found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    /**
     * This method reads from the segment file, creates Segment objects and store them in
     * a HashSet. In the meantime, connections between segment and road, segment and node
     * are established, so that the graph is connected as a whole.
     * @param file    the file contains information of segments
     * @param nodeMap    the collection in which all nodes are stored
     * @param roadMap 
     */
    private void loadSegments(File file) {
        BufferedReader bfReader;
        try {
            bfReader = new BufferedReader(new FileReader(file));
            bfReader.readLine();   // throw away the first line
            String line = bfReader.readLine(); 
            while (line != null) {
                Segment seg = new Segment(line, nodeMap, roadMap);
                segmentSet.add(seg);
                // add the road that this segment belongs to into the
                // appropriate node objects
                seg.start.joinedSegments.add(seg);
                seg.end.joinedSegments.add(seg);
                // add this segment into the appropriate road object
                roadMap.get(seg.roadID).roadSegments.add(seg);
                line = bfReader.readLine();
            }
            bfReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + file + " Not Found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    /**
     * This method reads from the polygon file, creates Polygon objects and store them in
     * a HashSet. 
     * @param file    the file contains information of polygons
     */
    private void loadPolygons(File file) {
        BufferedReader bfReader;
        try {
            bfReader = new BufferedReader(new FileReader(file));
            String line = bfReader.readLine();
            while (line != null) {
                if (line.equals("[POLYGON]")) {
                    // initialise variables for the arguments of polygon constructor
                    String label = null;
                    Integer type = null, endLevel = null, cityIdx = null;
                    Set<ArrayList<Location>> locations = new HashSet<>();
                    
                    line = bfReader.readLine();
                    while (!line.equals("[END]")) {
                        if (line.startsWith("Type")) {
                            type = Integer.parseInt(line.substring(7), 16);
                        } else if (line.startsWith("Label")) {
                            label = line.substring(6);
                        } else if (line.startsWith("EndLevel")) {
                            endLevel = Integer.parseInt(line.substring(9));
                        } else if (line.startsWith("CityIdx")) {
                            cityIdx = Integer.parseInt(line.substring(8));
                        } else if (line.startsWith("Data")) {
                            // remove brackets, split the line and convert it to a String array
                            String[] dots = line.substring(6).replace("(", "").replace(")", "").split(",");
                            ArrayList<Location> list = new ArrayList<>();
                            for (int index = 0; index < dots.length;) {
                                double lat = Double.parseDouble(dots[index++]);
                                double lon = Double.parseDouble(dots[index++]);
                                list.add(Location.newFromLatLon(lat, lon));
                            }
                            locations.add(list);
                        }
                        line = bfReader.readLine();
                    }
                    Polygon polygon = new Polygon(type, label, endLevel, cityIdx, locations);
                    this.polygonSet.add(polygon);

                    bfReader.readLine();  // throw away the empty line
                    line = bfReader.readLine();
                }
            }
            bfReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + file + " Not Found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }
    
    /**
     * This method reads from restriction file, and store them in
     * a Map database.
     * @param restrictions
     */
    private void loadRestrictions(File file) {
        BufferedReader bfReader;
        try {
            bfReader = new BufferedReader(new FileReader(file));
            bfReader.readLine(); // throw away the first line
            String line = bfReader.readLine();
            while (line != null) {
                Restriction restriction = new Restriction(line, nodeMap, roadMap);
                restrictions.add(restriction);
                line = bfReader.readLine();
            }
            bfReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + file + " Not Found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }
    
    /**
     * This method draws all polygons, road segments, and intersections
     * in this graph by delegating to each polygon, road segment, and node
     * to do so.
     * @param g     the passed Graphics object
     * @param currentOrigin    the current origin Location
     * @param currentScale     the current scale
     * @param d    the current dimension of the display panel
     */
    public void redraw(Graphics g, Location currentOrigin, double currentScale, Dimension d) {
        // draw all polygons
        if (!polygonSet.isEmpty()) {
            for (Polygon p : polygonSet) {
                p.draw(g, currentOrigin, currentScale, d);
            }
        }
        // draw all segments
        for (Segment seg : segmentSet) {
            seg.draw(g, currentOrigin, currentScale, d);
        }
        // draw all nodes
        for (Node n : nodeMap.values()) {
            n.draw(g, currentOrigin, currentScale, d);
        }
    }

    /**
     * This method implements a linear search to finds the intersection 
     * being clicked. 
     * Note: when the scale is big, i.e. when many intersections are drew 
     * overlapped, this method may not work.
     * @param location    the location being clicked
     * @return    the intersection selected. Returns null if no node can be found
     */
    public Node findNode(Location location) {
        
        double bestDist = Double.MAX_VALUE;
        Node closest = null;

        for (Node n : nodeMap.values()) {
            double distance = location.distance(n.location);
            if (distance < bestDist) {
                bestDist = distance;
                closest = n;
            }
        }
        
        // if it's close enough, highlight it and show some information.
        if (closest != null && location.distance(closest.location) < MAX_CLICKED_DISTANCE) {
            return closest;
        } else {
            return null;
        }
        
        // Quad-tree version
        
    }

    /**
     * This method takes the user input from the search box, and search through
     * the trie to find possible matches with same prefix.
     * @param str    the user input in search box
     * @return    a list of Road(s). The road in this list all have the same prefix
     *               that matches the user input. Returns null if no road can be found
     */
    public ArrayList<Road> searchRoad(String str) {
        return roadTries.find(str);
    }

    /**
     * 
     * @return
     */
    public Set<Node> findArticulationPoints() {
        return ArticulationUtil.findArticulationPoints(this.nodeMap.values());
    }
    
}
