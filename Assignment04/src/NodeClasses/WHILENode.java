package NodeClasses;

import core.Robot;

public class WHILENode extends STMTNode {

    private CONDNode condition;
    private BLOCKNode block;

    public WHILENode(CONDNode condition, BLOCKNode block) {
        this.condition = condition;
        this.block = block;
    }

    @Override
    public void execute(Robot robot) {
        while (condition.evaluate(robot)) {
            block.execute(robot);
        }
    }

    @Override
    public String toString() {
        return "while (" + condition.toString() + ") " + block.toString();
    }

}
