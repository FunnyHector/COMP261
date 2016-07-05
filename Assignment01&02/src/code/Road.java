package code;

import java.util.HashSet;
import java.util.Set;
/**
 * This class represents a road.
 * @author Hector
 *
 */
public class Road {
    // all properties of roads read from the roadIO-roadInfo.tab
    public final int roadID, type, speedLimit;
    public final double roadClass;
    public final boolean isOneWay, notForCar, notForPedestrian, notForBicycle;
    public final String label, city;
    public final Set<Segment> roadSegments;
    
    /**
     * Constructor. Note that the field joinedSegments is empty
     * when an object is constructed
     * @param line
     */
    public Road(String line) {
        String[] values = line.split("\t");
        this.roadID = Integer.parseInt(values[0]);
        this.type = Integer.parseInt(values[1]);
        this.label = values[2];
        this.city = values[3];
        this.isOneWay = Integer.parseInt(values[4]) == 1 ? true : false;
        this.notForCar = Integer.parseInt(values[7]) == 1 ? true : false;
        this.notForPedestrian = Integer.parseInt(values[8]) == 1 ? true : false;
        this.notForBicycle = Integer.parseInt(values[9]) == 1 ? true : false;
        roadSegments = new HashSet<>();
        
        // speed limit, for A* search
        int speed = Integer.parseInt(values[5]);
        switch (speed) {
        case 0:
            this.speedLimit = 5;
            break;
        case 1:
            this.speedLimit = 20;
            break;
        case 2:
            this.speedLimit = 40;
            break;
        case 3:
            this.speedLimit = 60;
            break;
        case 4:
            this.speedLimit = 80;
            break;
        case 5:
            this.speedLimit = 100;
            break;
        case 6:
            this.speedLimit = 110;
            break;
        case 7:
        default:  // let's assume a car's maximum speed is 120km/h
            this.speedLimit = 120;
            break;
        }
        
        /*
         * road class, for A* search. Assume on a better class road, cars can
         * drive faster. e.g. on a class 4 road, car can drive at full speed
         * (subject to speed limit); on a class 0 road, even the speed limit is
         * 100km/h, the condition is not so good that the car can only reach 80%
         * of speed limit.
         */
        int value6 = Integer.parseInt(values[6]);
        switch (value6) {
        case 0:
            this.roadClass = 0.80;
            break;
        case 1:
            this.roadClass = 0.85;
            break;
        case 2:
            this.roadClass = 0.90;
            break;
        case 3:
            this.roadClass = 0.95;
            break;
        case 4:
        default:
            this.roadClass = 1.00;
            break;
        }
    }
}
