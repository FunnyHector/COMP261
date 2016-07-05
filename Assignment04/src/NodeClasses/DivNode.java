package NodeClasses;

import core.Robot;

public class DivNode implements OPNode {

    private EXPNode exp1;
    private EXPNode exp2;

    public DivNode(EXPNode exp1, EXPNode exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public int evaluate(Robot robot) {
        if (exp2.evaluate(robot) == 0) {
            throw new ArithmeticException("divided by zero");
        }

        return exp1.evaluate(robot) / exp2.evaluate(robot);
    }

    @Override
    public String toString() {
        return "div(" + exp1.toString() + ", " + exp2.toString() + ")";
    }

}
