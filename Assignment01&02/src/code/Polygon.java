package code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Set;

/**
 * This class represents a colored polygon on map. Polygons are used to make 
 * the map much nicer.
 * @author Hector
 *
 */
public class Polygon {

    public final String label;
    public final Integer type, endLevel, cityIdx; 

    /*
     * the polygon data is not so perfect, some polygon has two lines of vertices
     * data, so it is basically two set of vertices data existing in one polygons
     * Therefore, here I used a set of ArrayList<Location>, to cope with this case. 
     */
    public final Set<ArrayList<Location>> locations;
    private final Color color;

    /**
     * Constructor. Note that after constructed, the object could have null field(s)
     * @param type
     * @param label
     * @param endLevel
     * @param cityIdx
     * @param locations
     */
    public Polygon(Integer type, String label, Integer endLevel, Integer cityIdx, Set<ArrayList<Location>> locations) {
        this.type = type;
        this.label = label;
        this.endLevel = endLevel;
        this.cityIdx = cityIdx;
        this.locations = locations;

        /*
         * I didn't take much time to figure out which color should be
         * assigned to which type. this is only a simple implementation
         * with only a few colors.
         * All colors are picked from google map.
         */
        switch (this.type) {
        case 0x07:
            this.color = new Color(211, 202, 189); // airport
            break;
        case 0x0a:
            this.color = new Color(232, 221, 189); // college
            break;
        case 0x0b:
            this.color = new Color(235, 210, 207); // hospital
            break;
        case 0x14:
        case 0x15:
        case 0x16:
        case 0x17:
        case 0x18:
        case 0x19:
        case 0x1a:
        case 0x1e:
        case 0x1f:
        case 0x20:
        case 0x4e:
        case 0x4f:
        case 0x50:
        case 0x51:
            this.color = new Color(202, 223, 170); // green field / Cemetery /
            break;
        case 0x28:
        case 0x29:
        case 0x32:
        case 0x3b:
        case 0x3c:
        case 0x3d:
        case 0x3e:
        case 0x3f:
        case 0x40:
        case 0x41:
        case 0x42:
        case 0x43:
        case 0x44:
        case 0x45:
        case 0x46:
        case 0x47:
        case 0x48:
        case 0x49:
        case 0x4c:
        case 0x4d:
            this.color = new Color(179, 209, 255); // sea / ocean / river / lake
            break;
        case 0x53:
            this.color = new Color(255, 225, 104); // sand
            break;
        default:
            this.color = new Color(233, 229, 220);
        }
        // I guess there is a much better looking way than this one to set colors
    }

    /**
     * This method draws the polygon.
     * @param g    the passed Graphics object
     * @param currentOrigin    the current origin Location
     * @param currentScale    the current scale
     * @param d    the current dimension of the display panel
     */
    public void draw(Graphics g, Location currentOrigin, double currentScale, Dimension d) {
        // the coordinate of display panel center
        int centrXCoord = (int) (d.getWidth() / 2);
        int centrYCoord = (int) (d.getHeight() / 2);
        
        for (ArrayList<Location> list : locations) {
            // two arrays for polygon vertices
            int size = list.size();
            int[] xPoints = new int[size];
            int[] yPoints = new int[size];
            for (int i = 0; i < size; i++) {
                Point point = list.get(i).asPoint(currentOrigin, currentScale);
                // move the point towards right-bottom, so that zoom in is nicer
                Point newP = new Point(point.x + centrXCoord, point.y + centrYCoord);
                xPoints[i] = (int) newP.getX();
                yPoints[i] = (int) newP.getY();
            }
            g.setColor(this.color);
            g.fillPolygon(xPoints, yPoints, size);
        }
    }
}
