package NodeClasses;

import core.Robot;

public class BarrelFBNode implements SENNode {

    private EXPNode exp;

    public BarrelFBNode(EXPNode exp) {
        this.exp = exp;
    }

    @Override
    public int evaluate(Robot robot) {
        int argument = exp != null ? exp.evaluate(robot) : 0;
        return robot.getBarrelFB(argument);
    }

    @Override
    public String toString() {
        String s = "barrelFB";
        if (exp != null) {
            s = s + "(" + exp.toString() + ")";
        }
        return s;
    }
}
