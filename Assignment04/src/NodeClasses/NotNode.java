package NodeClasses;

import core.Robot;

public class NotNode implements CONDNode {

    private CONDNode cond;

    public NotNode(CONDNode cond) {
        this.cond = cond;
    }

    @Override
    public boolean evaluate(Robot robot) {
        return !cond.evaluate(robot);
    }

    @Override
    public String toString() {
        return "not(" + cond.toString() + ")";
    }
}
