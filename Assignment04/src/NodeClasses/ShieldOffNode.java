package NodeClasses;

import core.Robot;

public class ShieldOffNode extends STMTNode {

    @Override
    public void execute(Robot robot) {
        robot.setShield(false);
    }

    @Override
    public String toString() {
        return "shieldOff;";
    }
}
