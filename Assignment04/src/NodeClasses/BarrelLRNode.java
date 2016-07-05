package NodeClasses;

import core.Robot;

public class BarrelLRNode implements SENNode {

    private EXPNode exp;

    public BarrelLRNode(EXPNode exp) {
        this.exp = exp;
    }

    @Override
    public int evaluate(Robot robot) {
        int argument = exp != null ? exp.evaluate(robot) : 0;
        return robot.getBarrelLR(argument);
    }

    @Override
    public String toString() {
        String s = "barrelLR";
        if (exp != null) {
            s = s + "(" + exp.toString() + ")";
        }
        return s;
    }
}
