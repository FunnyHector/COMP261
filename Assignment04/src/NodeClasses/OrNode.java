package NodeClasses;

import core.Robot;

public class OrNode implements CONDNode {

    private CONDNode cond1;
    private CONDNode cond2;

    public OrNode(CONDNode cond1, CONDNode cond2) {
        this.cond1 = cond1;
        this.cond2 = cond2;
    }

    @Override
    public boolean evaluate(Robot robot) {
        return cond1.evaluate(robot) || cond2.evaluate(robot);
    }

    @Override
    public String toString() {
        return "or(" + cond1.toString() + ", " + cond2.toString() + ")";
    }
}
