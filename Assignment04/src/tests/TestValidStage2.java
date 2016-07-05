package tests;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import core.Parser;
import core.ParserFailureException;

public class TestValidStage2 {
    private final boolean printOutput = false;

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
    public void instructionMove() {
        s = new Scanner("move;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionMove");
            }
        } catch (ParserFailureException e) {
            fail("Move can still be called without parameters, so this should still work." + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionMoveParamNumber() {
        s = new Scanner("move(100);");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionMoveParamNumber");
            }
        } catch (ParserFailureException e) {
            fail("You'll need to implement parameters for move." + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionWait() {
        s = new Scanner("wait;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionWait");
            }
        } catch (ParserFailureException e) {
            fail("Wait can still be called without parameters, so this should still work." + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionWaitParamNumber() {
        s = new Scanner("wait(43);");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionWaitParamNumber");
            }
        } catch (ParserFailureException e) {
            fail("You'll need to implement parameters for wait." + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * If and if else. Check to see if your if clauses are still working on
     * their own.
     */

    @Test
    public void ifStatementNoElse() {
        // The robot should move forward once.
        s = new Scanner("if \n(\n\n\nand\n (eq(1,1),lt(0,5)))\t{\nmove\n;\n}\n");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifStatementNoElse");
            }
        } catch (ParserFailureException e) {
            fail("You seem to have broken your if statements in adding the else. Remember that if statements don't need to have an else."
                    + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void ifElseStatement() {
        // The robot should turn around once.
        s = new Scanner("if \n(\n\n\nand\n (eq(1,1),lt(0,5)))\t{\nmove\n;\n}\nelse{\n\tturnAround;}\n");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifElseStatement");
            }
        } catch (ParserFailureException e) {
            fail("You don't seem to support the else statement yet." + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Expressions.
     */

    @Test
    public void nestedAndOrNot() {
        // In other words: (((not 0 == 0) or (3 < 2)) and (0 == 3)) which is
        // false on both sides.
        // The robot should not move at all.
        s = new Scanner("if (and(or(not(eq(0,0)),lt(3,2)),eq(0,3))) { move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED nestedAndOrNot");
            }
        } catch (ParserFailureException e) {
            fail("" + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void nestedNot() {
        // In other words: Always evaluates to true.
        // The robot should spin around until it runs out of fuel.
        s = new Scanner("while (not(not(not(not(not(not(not(not(not(not(gt(4,2)))))))))))) { turnAround; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED nestedNot");
            }
        } catch (ParserFailureException e) {
            fail("Nested `not' statements are legal, and the robot should turn around until it runs out of fuel."
                    + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Provided programs
     */

    @Test
    public void simple() {
        s = new Scanner("while(gt(fuelLeft, sub(5, 5))){\n" + "\tif(eq(numBarrels, 0)){\n" + "\t\twait;\n"
                + "\t} else{\n" + "\t\tif(and(eq(barrelFB, 0), eq(barrelLR, 0))){\n" + "\t\t\ttakeFuel;\n"
                + "\t\t} else{\n" + "\t\t\tif(eq(0, barrelFB)){\n" + "\t\t\t\tif(lt(barrelLR, 0)){\n"
                + "\t\t\t\t\tturnL;\n" + "\t\t\t\t} else{\n" + "\t\t\t\t\tturnR;\n" + "\t\t\t\t}\n" + "\t\t\t} else{\n"
                + "\t\t\t\tif(gt(barrelFB, 0)){\n" + "\t\t\t\t\tmove;\n" + "\t\t\t\t} else{\n"
                + "\t\t\t\t\tturnAround;\n" + "\t\t\t\t}\n" + "\t\t\t}\n" + "\t\t}\n" + "\t}\n" + "}\n");

        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED simple");
            }
        } catch (ParserFailureException e) {
            fail("Failed simple" + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void full() {
        s = new Scanner("while(or(gt(fuelLeft, sub(mul(div(5, add(1, 4)), -1), -1)), eq(1, 0))){\n" + "\tshieldOff;\n"
                + "\tif(not(not(eq(numBarrels, 0)))){\n" + "\t\tif(and(eq(oppLR, 0), eq(oppFB, 1))){\n"
                + "\t\t\ttakeFuel;\n" + "\t\t} else{\n" + "\t\t\tif(and(eq(oppLR, 0), eq(oppFB, -1))){\n"
                + "\t\t\t\tif(gt(wallDist, 0)){\n" + "\t\t\t\t\tmove;\n" + "\t\t\t\t} else{\n" + "\t\t\t\t\tshieldOn;\n"
                + "\t\t\t\t}\n" + "\t\t\t} else{\n" + "\t\t\t\twait(1);\n" + "\t\t\t}\n" + "\t\t}\n" + "\t} else{\n"
                + "\t\tif(and(eq(barrelFB, 0), eq(barrelLR, 0))){\n" + "\t\t\ttakeFuel;\n" + "\t\t} else{\n"
                + "\t\t\tif(eq(0, barrelFB)){\n" + "\t\t\t\tif(lt(barrelLR, 0)){\n" + "\t\t\t\t\tturnL;\n"
                + "\t\t\t\t} else{\n" + "\t\t\t\t\tturnR;\n" + "\t\t\t\t}\n" + "\t\t\t} else{\n"
                + "\t\t\t\tif(gt(barrelFB, 0)){\n" + "\t\t\t\t\tif(gt(barrelFB, 1)){\n"
                + "\t\t\t\t\t\tmove(div(barrelFB, 2));\n" + "\t\t\t\t\t} else{\n" + "\t\t\t\t\t\tmove;\n"
                + "\t\t\t\t\t}\n" + "\t\t\t\t} else{\n" + "\t\t\t\t\tturnAround;\n" + "\t\t\t\t}\n" + "\t\t\t}\n"
                + "\t\t}\n" + "\t}\n" + "}\n");

        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED full");
            }
        } catch (ParserFailureException e) {
            fail("Failed full" + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void handoutExample() {
        s = new Scanner("move(8);\n" + "turnL; \n" + "loop {\n" + "while ( or(eq(numBarrels, 0),\n"
                + "           lt(add(oppFB, oppLR), \n" + "add(barrelFB,barrelLR))) ) {\n"
                + "  if (lt(oppFB,0)) { turnAround; }\n"
                + "  else { if (gt(oppFB,0)) { move(add(1, div(oppFB, 2))); } \n"
                + "  else { if (lt(oppLR,0)) { turnL;}\n" + "  else { if (gt(oppLR,0)) { turnR;}\n"
                + "  else { if (eq(oppLR,0)) { takeFuel; }}}}}\n" + "}\n"
                + "if ( and(eq(barrelFB, 0),eq(barrelLR, 0))) { takeFuel; }\n"
                + "else { if ( lt(barrelFB, 0) ){ turnAround; }\n"
                + "else { if ( gt(barrelFB, 0) ) { move(barrelFB); }\n" + "else { if ( lt(barrelLR, 0) ) { turnL; }\n"
                + "else { if ( gt(barrelLR, 0) ) { turnR; }}}}}\n" + "}");

        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED handoutExample");
            }
        } catch (ParserFailureException e) {
            fail("FAILED handoutExample" + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }
}