package NodeClasses;

import core.Robot;

public class NumBarrelsNode implements SENNode {

    @Override
    public int evaluate(Robot robot) {
        return robot.numBarrels();
    }

    @Override
    public String toString() {
        return "numBarrels";
    }
}
