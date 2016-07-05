package NodeClasses;

import java.util.ArrayList;

import core.Robot;

public class IFNode extends STMTNode {

    private CONDNode condition;
    private BLOCKNode ifBlock;
    private ArrayList<CONDNode> elifConditions = null;
    private ArrayList<BLOCKNode> elifBlocks = null;
    private BLOCKNode elseBlock = null;

    public IFNode(CONDNode condition, BLOCKNode ifBlock, ArrayList<CONDNode> elifConditions,
            ArrayList<BLOCKNode> elifBlocks, BLOCKNode elseBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elifConditions = elifConditions;
        this.elifBlocks = elifBlocks;
        this.elseBlock = elseBlock;
    }

    @Override
    public void execute(Robot robot) {
        if (condition.evaluate(robot)) {
            ifBlock.execute(robot);
        } else {
            if (!elifConditions.isEmpty() && !elifBlocks.isEmpty()) {
                boolean elifExcuted = false;
                for (int i = 0; i < elifConditions.size(); i++) {
                    if (elifConditions.get(i).evaluate(robot)) {
                        elifBlocks.get(i).execute(robot);
                        elifExcuted = true;
                        break;
                    }
                }

                if (!elifExcuted) {
                    if (elseBlock != null) {
                        elseBlock.execute(robot);
                    }
                }
            } else {
                if (elseBlock != null) {
                    elseBlock.execute(robot);
                }
            }
        }
    }

    @Override
    public String toString() {
        String s = "if (" + condition.toString() + ") " + ifBlock.toString();

        if (!elifConditions.isEmpty() && !elifBlocks.isEmpty()) {
            for (int i = 0; i < elifConditions.size(); i++) {
                s = s + "elif (" + elifConditions.get(i).toString() + ") " + elifBlocks.get(i).toString();
            }
        }

        if (elseBlock != null) {
            s = s + "else " + elseBlock.toString();
        }
        return s;
    }

}
