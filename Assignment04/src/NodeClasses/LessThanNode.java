package NodeClasses;

import core.Robot;

public class LessThanNode implements CONDNode {

    private EXPNode exp1;
    private EXPNode exp2;

    public LessThanNode(EXPNode exp1, EXPNode exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public boolean evaluate(Robot robot) {
        return exp1.evaluate(robot) < exp2.evaluate(robot);
    }

    @Override
    public String toString() {
        return "lt(" + exp1.toString() + ", " + exp2.toString() + ")";
    }
}
