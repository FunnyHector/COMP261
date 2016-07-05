package NodeClasses;

import core.Robot;

public class OppLRNode implements SENNode {

    @Override
    public int evaluate(Robot robot) {
        return robot.getOpponentLR();
    }

    @Override
    public String toString() {
        return "oppLR";
    }
}
