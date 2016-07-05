package NodeClasses;

import core.Robot;

public class NUMNode implements EXPNode {

    private final int num;

    public NUMNode(int num) {
        this.num = num;
    }

    @Override
    public int evaluate(Robot robot) {
        return this.num;
    }

    @Override
    public String toString() {
        return "" + num;
    }

}
