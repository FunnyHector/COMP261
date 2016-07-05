package tests;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import core.Parser;
import core.ParserFailureException;

public class TestInvalidStage3 {
    private final boolean printOutput = true;

    private final String delimiter = "\\s+|(?=[{}(),;])|(?<=[{}(),;])";

    private Scanner s;

    @After
    public void tearDown() {
        s.close();
    }

    /*
     * Basic instructions
     */

    @Test
    public void instructionAssign() {
        s = new Scanner("$myVariable = and(5,3);");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Variables can only hold EXP, not COND");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED instructionAssign: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionAssignInvalidVariableName() {
        s = new Scanner("$1myVariable = 4;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Variables must start with a letter");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED instructionAssignInvalidVariableName: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionAssignPositive() {
        s = new Scanner("$myVariable = +4;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("NUM cannot start with +");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED instructionAssignPositive: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionVariable() {
        s = new Scanner("$myVariable;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Variables must be assigned a value");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED instructionVariable: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionBarrelFBParameter() {
        s = new Scanner("while (eq(barrelFB(and(4,3)), 3)){ move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("BarrelFB cannot take COND as a parameter, it requires NUM");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED instructionBarrelFBParameter: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionBarrelLRParameter() {
        s = new Scanner("while (eq(barrelLR(and(4,3)), 3)){ move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("BarrelLR cannot take COND as a parameter, it requires NUM");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED instructionBarrelLRParameter: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * If and else if, and else.
     */

    @Test
    public void elseIfStatement() {
        s = new Scanner("elif(eq(4,4)){ move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Else if statements cannot stand alone, and require an if prefacing them");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED elseIfStatement: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void elseIfStatement2() {
        s = new Scanner("if (gt(3,4)){move;}\nelseif(eq(4,4)){ move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("The else if statement is elif(cond) not elseif(cond)");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED elseIfStatement: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * There are no provided invalid tests for stage three.
     */

    /*
     * Sentinel test
     */

    @Test
    public void sentinelTest() {
        s = new Scanner("if (eq(1,1)){turnAround;}\nelse {move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
        } catch (Exception e) {
            fail("The tests are broken: " + e);
        }
    }

}