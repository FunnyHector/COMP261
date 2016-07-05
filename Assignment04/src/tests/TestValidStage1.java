package tests;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import core.Parser;
import core.ParserFailureException;

public class TestValidStage1 {
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
    public void instructionTurnAround() {
        s = new Scanner("turnAround;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionTurnAround");
            }
        } catch (ParserFailureException e) {
            fail("FAILED instructionTurnAround: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionShieldOn() {
        s = new Scanner("shieldOn;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionShieldOn");
            }
        } catch (ParserFailureException e) {
            fail("FAILED instructionShieldOn: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionShieldOff() {
        s = new Scanner("shieldOff;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionShieldOff");
            }
        } catch (ParserFailureException e) {
            fail("FAILED instructionShieldOff: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * While loop
     */

    @Test
    public void ifLtNoWhitespace() {
        s = new Scanner("if(lt(fuelLeft,3)){move;turnAround;move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifLtNoWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED ifLtNoWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void ifLtWhitespace() {
        s = new Scanner(
                "\nif\n\n\n\t(\n\n\n\nlt\n\n\n(\n\nfuelLeft\n,\n\t\t3\n)\n\n\n\t)\n{\nmove\n\n\n\n\n;\n\nturnAround\n;move\n          ;   }\n");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifLtWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED ifLtWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void ifEqNoWhitespace() {
        s = new Scanner("if(eq(barrelLR,-1)){move;turnAround;move;}"); // -1 is
                                                                       // valid,
                                                                       // but
                                                                       // this
                                                                       // loop
                                                                       // should
                                                                       // never
                                                                       // run as
                                                                       // we
                                                                       // can't
                                                                       // have
                                                                       // -1
                                                                       // barrels
                                                                       // around
                                                                       // us.
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifEqNoWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED ifEqNoWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void ifEqWhitespace() {
        s = new Scanner(
                "if\t(\teq(\tbarrelLR\t,-1)\t\t   \t     \t       \t\t){move;\tturnAround\t\t \t;\t   move; }\n \t");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifEqWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED ifEqWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void ifGtNoWhitespace() {
        s = new Scanner("if(gt(wallDist,1)){move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifGtNoWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED ifGtNoWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void ifGtWhitespace() {
        s = new Scanner("          if  (   gt ( wallDist           ,1)  )     {     move ; } ");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifGtWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED ifGtWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * If statement
     */

    @Test
    public void whileLtNoWhitespace() {
        s = new Scanner("while(lt(fuelLeft,3)){move;turnAround;move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED whileLtNoWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED whileLtNoWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void whileLtWhitespace() {
        s = new Scanner(
                "\nwhile\n\n\n\t(\n\n\n\nlt\n\n\n(\n\nfuelLeft\n,\n\t\t3\n)\n\n\n\t)\n{\nmove\n\n\n\n\n;\n\nturnAround\n;move\n          ;   }\n");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED whileLtWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED whileLtWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void whileEqNoWhitespace() {
        s = new Scanner("while(eq(barrelLR,-1)){move;turnAround;move;}"); // -1
                                                                          // is
                                                                          // valid,
                                                                          // but
                                                                          // this
                                                                          // loop
                                                                          // should
                                                                          // never
                                                                          // run
                                                                          // as
                                                                          // we
                                                                          // can't
                                                                          // have
                                                                          // -1
                                                                          // barrels
                                                                          // around
                                                                          // us.
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED whileEqNoWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED whileEqNoWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void whileEqWhitespace() {
        s = new Scanner(
                "while\t(\teq(\tbarrelLR\t,-1)\t\t   \t     \t       \t\t){move;\tturnAround\t\t \t;\t   move; }\n \t");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED whileEqWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED whileEqWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void whileGtNoWhitespace() {
        s = new Scanner("while(gt(wallDist,1)){move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED whileGtNoWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED whileGtNoWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void whileGtWhitespace() {
        s = new Scanner("          while  (   gt ( wallDist           ,1)  )     {     move ; } ");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED whileGtWhitespace");
            }
        } catch (ParserFailureException e) {
            fail("FAILED whileGtWhitespace: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Nested tests. Because you know the automatic marking system will check
     * for this.
     */

    @Test
    public void nestedLoop() {
        // I should have tested this in stage 0, but I've only just thought of
        // it.

        // Because loop is a STMT, and a block requires 1 or more STMT, you can
        // nest loops infinitely.
        // Of course, the last block must contain a statement other than loop
        // (otherwise we have an
        // empty block, which is invalid).

        // Note that you can't move this test into ValidStageZero because it
        // uses turnAround.
        // You could modify the test, but just be aware that you can't move it
        // as is.

        s = new Scanner("loop{loop{loop{loop{move;turnAround;move;}}}}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED nestedLoop");
            }
        } catch (ParserFailureException e) {
            fail("FAILED nestedLoop: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void nestedWhileConflict() {
        // A similar test to nestedLoop, but the conditions conflict.
        // Note that although move will never run, it should be parsed as the
        // //entire// program is parsed and then passed to the RoboGame.

        s = new Scanner("while (gt(fuelLeft,0)) { while ( eq ( fuelLeft, -1)) { move; } }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED nestedWhileConflict");
            }
        } catch (ParserFailureException e) {
            fail("FAILED nestedWhileConflict: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void nestedWhileNonConflict() {
        // The while equivalent of nestedLoop.
        s = new Scanner(
                "while (gt(fuelLeft,0)) { while ( gt ( fuelLeft, 0)) { while (gt(fuelLeft,0)) {while (gt(fuelLeft,0)) {while (gt(fuelLeft,0)) {while (gt(fuelLeft,0)) {while (gt(fuelLeft,0)) { move;turnAround; }  }}}}    }}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED nestedWhileNonConflict");
            }
        } catch (ParserFailureException e) {
            fail("FAILED nestedWhileNonConflict: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void nestedWhileIf() {
        // A mixing of while and if.
        s = new Scanner("while               \n(\ngt(fuelLeft, 66)\n){\tif(lt\n(numBarrels,10)) { move; takeFuel;  }}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED nestedWhileIf");
            }
        } catch (ParserFailureException e) {
            fail("FAILED nestedWhileIf: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void nestedIfWhile() {
        // The opposite mixing of if and while.
        // We can't have > 999999999 barrels but the block of the if still needs
        // to be evaluated.
        // This also brings into question what primitive is being used to store
        // the number of barrels in the NUM terminal.
        s = new Scanner("if(gt(numBarrels,999999999)) {   while ( gt(barrelFB, 0) ) { move; }  }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED nestedWhileIf");
            }
        } catch (ParserFailureException e) {
            fail("FAILED nestedWhileIf: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void nestedIf() {
        s = new Scanner(
                "if (eq(fuelLeft\t\n,  50   )) {if(eq(fuelLeft,50)){if (eq(fuelLeft\t\n,  50   )) { if (eq(fuelLeft\t\n,  50\n)) \n{\tif \t(eq(fuelLeft\t\n,\t50   )) \n{\nif (eq(fuelLeft\t\n,  50   )) { move; } \t\n }}} } }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED nestedIf");
            }
        } catch (ParserFailureException e) {
            fail("FAILED nestedIf: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * The following are the valid programs provided.
     */

    @Test
    public void handoutStageOneExample() {
        s = new Scanner("while ( gt(barrelFB, 0) ) { move; }\nif (eq(barrelLR, 0)) {\n   takeFuel;\n}"
                + "if (lt(barrelLR, 0)) {\n   turnL;\n   while ( gt(barrelFB,0) ){ move;}\n   takeFuel;\n}"
                + "if (gt(barrelLR, 0)) {\n   turnR;\n   while ( gt(barrelFB,0) ){ move;}\n   takeFuel;\n}"
                + "wait;\nloop {\n   if ( gt(fuelLeft, 0) ) {\n     move;\n     turnL;\n   }\n}");

        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED handoutStageOneExample");
            }
        } catch (ParserFailureException e) {
            fail("FAILED handoutStageOneExample: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void simple() {
        s = new Scanner("while(gt(fuelLeft, 0)){\n" + "\tif(eq(numBarrels, 0)){\n" + "\t\twait;\n" + "\t}\n"
                + "\tif(gt(numBarrels, 0)){\n" + "\t\tif(eq(barrelFB, 0)){\n" + "\t\t\tif(eq(barrelLR, 0)){\n"
                + "\t\t\t\ttakeFuel;\n" + "\t\t\t}\n" + "\t\t}\n" + "\t\tif(gt(barrelFB, 0)){ \n" // Yes,
                                                                                                  // there
                                                                                                  // is
                                                                                                  // a
                                                                                                  // space
                                                                                                  // at
                                                                                                  // the
                                                                                                  // end
                                                                                                  // of
                                                                                                  // the
                                                                                                  // line
                                                                                                  // in
                                                                                                  // the
                                                                                                  // file.
                + "\t\t\tmove; \n" // Same here.
                + "\t\t}\n" + "\t}\n" + "}\n");

        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED simple");
            }
        } catch (ParserFailureException e) {
            fail("FAILED simple: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void full() {
        s = new Scanner("while(gt(fuelLeft, 0)){\n" + "\tif(gt(fuelLeft, 95)){\n" + "\t\tshieldOn;\n" + "	\t}\n"
                + "\tif(lt(fuelLeft, 85)){\n" + "\t\tshieldOff;\n" + "\t}\n" + "\tif(gt(numBarrels, 0)){\n"
                + "\t\tif(eq(barrelFB, 0)){\n" + "\t\t\tif(eq(barrelLR, 0)){\n" + "\t\t\t\ttakeFuel;\n" + "\t\t\t}\n"
                + "\t\t}\n" + "\t\tif(gt(barrelFB, 0)){\n" + "\t\t\tmove; \n" + "\t\t}\n" + "\t\tif(lt(barrelFB, 0)){\n"
                + "\t\t\tturnAround;\n" + "\t\t}\n" + "\t\tif(lt(barrelLR, 0)){\n" + "\t\t\tturnL;\n" + "\t\t}\n"
                + "\t\tif(gt(barrelLR, 0)){\n" + "\t\t\tturnR;\n" + "\t\t}\n" + "\t}\n" + "\tif(eq(numBarrels, 0)){\n"
                + "\t\tif(lt(oppLR, 0)){\n" + "\t\t\tturnR;\n" + "\t\t}\n" + "\t\tif(gt(oppLR, 0)){\n"
                + "\t\t\tturnL;\n" + "\t\t}\n" + "\t\tif(gt(oppFB, 0)){\n" + "\t\t\tturnAround;\n" + "\t\t}\n"
                + "\t\tif(lt(oppFB, 0)){\n" + "\t\t\tif(gt(wallDist, 1)){\n" + "\t\t\t\tmove;\n" + "\t\t\t}\n"
                + "\t\t}\n" + "\t}\n" + "}");

        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED full");
            }
        } catch (ParserFailureException e) {
            fail("FAILED full: " + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Sentinel test for a similar reason as TestInvalidStageZero.
     */

    @Test
    public void sentinelTest() {
        s = new Scanner("move;\nturnL;\nwhile\n\n\n\n\t(COND){move;}\n\n");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("The tests are broken: should have thrown a ParserFailureException");
        } catch (ParserFailureException e) {
            /* This is actually okay, the program is indeed invalid. */ } catch (Exception e) {
            fail("The tests are broken, should not have thrown an exception: " + e);
        }
    }
}