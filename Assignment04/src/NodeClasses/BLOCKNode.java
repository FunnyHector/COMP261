package NodeClasses;

import java.util.ArrayList;

import core.Robot;

public class BLOCKNode implements RobotProgramNode {

    private ArrayList<STMTNode> statements;

    public BLOCKNode() {
        statements = new ArrayList<>();
    }

    @Override
    public void execute(Robot robot) {
        for (STMTNode stmt : statements) {
            stmt.execute(robot);
        }
    }

    public void addSTMTNode(STMTNode node) {
        statements.add(node);
    }

    public boolean isEmpty() {
        return statements.isEmpty();
    }

    @Override
    public String toString() {
        String s = "{\n";
        for (STMTNode n : statements) {
            s += "\t" + n.toString() + '\n';
        }
        return s + "}";
    }
}
