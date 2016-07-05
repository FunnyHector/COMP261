package NodeClasses;

import core.Robot;

public class ASSGNNode extends STMTNode {

    private VARNode varNode;
    private EXPNode expNode;

    public ASSGNNode(VARNode varNode, EXPNode expNode) {
        this.varNode = varNode;
        this.expNode = expNode;
    }

    @Override
    public void execute(Robot robot) {
        varNode.setValue(expNode.evaluate(robot));
    }

    @Override
    public String toString() {
        return varNode.toString() + " = " + expNode.toString() + ";";
    }
}
