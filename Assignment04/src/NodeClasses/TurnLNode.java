package NodeClasses;

import core.Robot;

public class TurnLNode extends STMTNode {

    @Override
    public void execute(Robot robot) {
        robot.turnLeft();
    }

    @Override
    public String toString() {
        return "turnL;";
    }
}
