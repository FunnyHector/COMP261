package NodeClasses;

import core.Robot;

public interface OPNode extends EXPNode {

    @Override
    public int evaluate(Robot robot);

    @Override
    public String toString();

}
