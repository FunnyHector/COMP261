package tests;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import core.Parser;
import core.ParserFailureException;

public class TestValidStage0 {
    /*
     * It should be noted that just because these tests pass, it doesn't mean
     * that your program behaves correctly, just that it does not throw a
     * ParserFailureException at any point on valid input.
     * 
     * THIS DOES NOT MEAN THAT THE ROBOT BEHAVES CORRECTLY.
     */

    private final boolean printOutput = false; // You can turn this on if you'd
                                               // like, but failures don't
                                               // depend on it.

    private final String delimiter = "\\s+|(?=[{}(),;])|(?<=[{}(),;])";

    private Scanner s;

    @After
    public void tearDown() {
        s.close();
    }

    /*
     * DIY test cases are added here.
     */

    @Test
    public void minimallyValidProgram() {
        s = new Scanner("");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
        } catch (ParserFailureException e) {
            fail("An empty program is a valid program: PROG contains 0 or more STMT nodes.");
        }
    }

    @Test
    public void whitespaceValidProgram() {
        s = new Scanner("\n     \n\n\n\n      \t                                         \n \t\t\n\t \t\n\t");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
        } catch (ParserFailureException e) {
            fail("An program with no instructions (but other characters) is a valid program: PROG contains 0 or more STMT nodes.");
        }
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
            System.out.println("FAILED instructionMove: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionTurnL() {
        s = new Scanner("turnL;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionTurnL");
            }
        } catch (ParserFailureException e) {
            System.out.println("FAILED instructionTurnL: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionTurnR() {
        s = new Scanner("turnR;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionTurnR");
            }
        } catch (ParserFailureException e) {
            System.out.println("FAILED instructionTurnR: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionTakeFuel() {
        s = new Scanner("takeFuel;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionTakeFuel");
            }
        } catch (ParserFailureException e) {
            System.out.println("FAILED instructionTakeFuel: " + e.getMessage());
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
            System.out.println("FAILED instructionWait: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void simpleProgramNoWhitespace() {
        s = new Scanner("move;turnL;move;move;turnR;wait;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED simpleProgramNoWhitespace");
            }
        } catch (ParserFailureException e) {
            System.out.println("FAILED simpleProgramNoWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void simpleProgramTabWhitespace() {
        s = new Scanner("\t\t\tmove;\t\t\t\t\t\t\t\tturnL\t\t\t\t\t;\tmove\t;\tmove\t;turnR\t;wait\t;\t\t\t\t\t");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED simpleProgramTabWhitespace");
            }
        } catch (ParserFailureException e) {
            System.out.println("FAILED simpleProgramTabWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void simpleProgramNewLineWhitespace() {
        s = new Scanner("\n\n\nmove;\n\n\n\n\n\n\n\nturnL\n\n\n\n\n;\nmove\n;\nmove\n;turnR\n;wait\n;\n\n\n\n\n");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED simpleProgramNewLineWhitespace");
            }
        } catch (ParserFailureException e) {
            System.out.println("FAILED simpleProgramNewLineWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void simpleProgramSpaceWhitespace() {
        s = new Scanner("   move;        turnL     ; move ; move ;turnR ;wait ;     ");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED simpleProgramSpaceWhitespace");
            }
        } catch (ParserFailureException e) {
            System.out.println("FAILED simpleProgramSpaceWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * The following are the valid programs provided. This means you don't have
     * to run them manually!
     * 
     * They are named ``simple'' or ``full'' after their program names
     * ``s0_simple.prog'', or ``s0_full.prog''.
     */

    @Test
    public void simple() {
        s = new Scanner("move;\nturnL;\nmove;\nmove;\nturnR;\nwait;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED simple");
            }
        } catch (ParserFailureException e) {
            System.out.println("FAILED simple: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void full() {
        s = new Scanner("move;\nturnL;\nmove; move; takeFuel;\nloop{\n\tmove;\n\tturnR;\n\twait;\n}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED full");
            }
        } catch (ParserFailureException e) {
            System.out.println("FAILED full: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Sentinel test for a similar reason as TestInvalidStageZero.
     */

    @Test
    public void sentinelTest() {
        s = new Scanner("move;\nturnL\nloop\n{\nmove;\n}\n");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("The tests are broken: should have thrown a ParserFailureException");
        } catch (ParserFailureException e) {
            /* This is actually okay, the program is invalid. */ } catch (Exception e) {
            fail("The tests are broken, should not have thrown an exception: " + e);
        }
    }
}