package NodeClasses;

import core.Robot;

public class MoveNode extends STMTNode {

    private EXPNode exp;

    public MoveNode(EXPNode exp) {
        this.exp = exp;
    }

    @Override
    public void execute(Robot robot) {
        int argument = exp != null ? exp.evaluate(robot) : 1;
        for (int i = 0; i < argument; i++) {
            robot.move();
        }
    }

    @Override
    public String toString() {
        String s = "move";
        if (exp != null) {
            s = s + "(" + exp.toString() + ")";
        }
        return s + ";";
    }
}
