package NodeClasses;

import core.Robot;

public class WallDistNode implements SENNode {

    @Override
    public int evaluate(Robot robot) {
        return robot.getDistanceToWall();
    }

    @Override
    public String toString() {
        return "wallDist";
    }
}
