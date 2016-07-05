package tests;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import core.Parser;
import core.ParserFailureException;

public class TestInvalidStage1 {
    private final boolean printOutput = true;

    private final String delimiter = "\\s+|(?=[{}(),;])|(?<=[{}(),;])";

    private Scanner s;

    @After
    public void tearDown() {
        s.close();
    }

    @Test
    public void nestedWhileConflict() {
        // A similar test to nestedLoop, but the conditions conflict.
        // Note that although UNREACHABLE will never run, it should be parsed
        // anyway as the //entire// program is parsed and then passed to the
        // RoboGame.

        s = new Scanner("while (gt(fuelLeft,0)) { while ( eq ( fuelLeft, -1)) { UNREACHABLE } }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("See the comment inside this test.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED nestedWhileConflict: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * While loops
     */

    @Test
    public void whileEmptyParam() {
        s = new Scanner("while () { move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("While statements must have parameters.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED whileEmptyParam: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void whileEmptyLtParam() {
        s = new Scanner("while (lt()) { turnAround; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("lt statements must have parameters.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED whileEmptyLtParam: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void whileCommaLtParam() {
        s = new Scanner("while (lt(,)) { move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("lt statements must have parameters.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED whileCommaLtParam: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void whileEqTwoCommas() {
        s = new Scanner("while(eq(fuelLeft,2,)){ move ; } ");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("lt statements must have parameters.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED whileEqTwoCommas: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Numbers
     */

    @Test
    public void numberPositiveSign() {
        s = new Scanner("if(lt(numBarrels,+2)){move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Numbers can have negative signs, but not positive ones.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED numberPositiveSign: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void numberTwoNegativeSigns() {
        s = new Scanner("if(lt(numBarrels,--2)){move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Numbers can have exactly one negative sign.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED numberTwoNegativeSigns: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void numberRegexQuotes() {
        s = new Scanner("if(lt(numBarrels,\"-?[0-9]+\")){move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("\"-?[0-9]+\" is used as the regex, but in itself is not a valid number.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED numberRegexQuotes: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void numberRegexNoQuotes() {
        s = new Scanner("if(lt(numBarrels,-?[0-9]+)){move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("-?[0-9]+ is used as the regex, but in itself is not a valid number.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED numberRegexNoQuotes: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Conditionals
     */

    @Test
    public void nestedCondTerminals() {
        s = new Scanner("if(lt(gt(fuelLeft, 6),2)){move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("lt cannot take gt as paramter because gt is not a SEN.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED nestedCondTerminals: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void condWithoutIfOrWhile() {
        s = new Scanner("(lt(fuelLeft, 6)){move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("lt missing if or while.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED condWithoutIfOrWhile: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void stageZeroBrackets() {
        // This should have been in the stage zero invalid tests.
        s = new Scanner("(((((((((move;)))))))))");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("move cannot be surrounded by brackets, only whitespace or braces.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED manyBrackets: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * The following are the invalid programs provided.
     */

    @Test
    public void bad1() {
        s = new Scanner("while{\n\tmove;\n}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("While statement requires parameters.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED bad1: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void bad2() {
        s = new Scanner("if(shieldOn){\n\tshieldOff;\n}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("shieldOn is not a condition.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED bad2: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void bad3() {
        s = new Scanner("while(gt(wallDist, barrelFB)){\n\tmove; \n\twait;\n}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("barrelFB is not a number.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED bad3: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void bad4() {
        s = new Scanner("while(eq(1, 1)){\n\tmove;\n\tturnL;\n}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("eq takes SEN, NUM instead of NUM, NUM.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED bad4: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Sentinel test:
     */

    @Test
    public void sentinelTest() {
        s = new Scanner("move;\nturnL;\nloop\n{\nmove;\n}\n");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
        } catch (Exception e) {
            fail("The tests are broken: " + e);
        }
    }

}