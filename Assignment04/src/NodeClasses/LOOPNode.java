package NodeClasses;

import core.Robot;

public class LOOPNode extends STMTNode {

    private BLOCKNode blockNode;

    public LOOPNode(BLOCKNode blockNode) {
        this.blockNode = blockNode;
    }

    @Override
    public void execute(Robot robot) {
        while (true) {
            blockNode.execute(robot);
        }
    }

    @Override
    public String toString() {
        return "loop " + blockNode.toString();
    }
}
