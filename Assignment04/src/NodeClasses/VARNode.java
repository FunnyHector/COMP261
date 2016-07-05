package NodeClasses;

import core.Robot;

public class VARNode implements EXPNode {

    private final String name;
    private int value;

    public VARNode(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int evaluate(Robot robot) {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}
