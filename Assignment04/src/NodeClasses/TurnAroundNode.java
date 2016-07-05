package NodeClasses;

import core.Robot;

public class TurnAroundNode extends STMTNode {

    @Override
    public void execute(Robot robot) {
        robot.turnAround();
    }

    @Override
    public String toString() {
        return "turnAround;";
    }
}
