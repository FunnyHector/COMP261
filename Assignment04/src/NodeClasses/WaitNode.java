package NodeClasses;

import core.Robot;

public class WaitNode extends STMTNode {

    private EXPNode exp;

    public WaitNode(EXPNode exp) {
        this.exp = exp;
    }

    @Override
    public void execute(Robot robot) {
        int argument = exp != null ? exp.evaluate(robot) : 1;
        for (int i = 0; i < argument; i++) {
            robot.idleWait();
        }
    }

    @Override
    public String toString() {
        String s = "wait";
        if (exp != null) {
            s = s + "(" + exp.toString() + ")";
        }
        return s + ";";
    }
}
