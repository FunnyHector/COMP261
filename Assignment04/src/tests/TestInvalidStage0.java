package tests;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import core.Parser;
import core.ParserFailureException;

public class TestInvalidStage0 {
    /*
     * It should be noted that just because these tests pass, it doesn't mean
     * that your program behaves correctly, just that it throws a
     * ParserFailureException at some point, and you return non-null in the
     * parseProgram method.
     */

    /*
     * I would highly recommend keeping printOutput on so you can examine why
     * the code has thrown the ParserFailureException. I had these tests pass
     * because I forgot to check for a semicolon at the end of a ART non
     * terminal. They passed, but for the wrong reason.
     */
    private final boolean printOutput = true;

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
    public void parseProgramNotNull() {
        s = new Scanner("test");
        s.useDelimiter(delimiter);

        try {
            assertNotNull("FIX THIS FIRST. For the other tests to pass this one must also pass.",
                    Parser.parseProgram(s));
        } catch (ParserFailureException e) {
            // This is expected, as "test" is not a valid string
            if (printOutput) {
                System.out.println("PASSED parseProgramNotNull: " + e.getMessage());
            }
        }
    }

    /*
     * BLOCK TESTS
     */

    @Test
    public void blockWithoutOpeningBrace() {
        s = new Scanner("loop\nmove;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("We're missing the opening brace for the block.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED blockWithoutOpeningBrace: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void blockWithoutLoop() {
        s = new Scanner("{move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("We're missing the loop keyword for the block.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED blockWithoutLoop: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void blockWithoutOpeningBraceOrLoop() {
        s = new Scanner("move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("We're missing the loop keyword and opening brace for the block.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED blockWithoutOpeningBraceOrLoop: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * SEMICOLON TESTS
     */

    @Test
    public void semicolonMissingLastItem() {
        s = new Scanner("move;\nturnL;\nmove;\nmove;\nturnR;\nwait");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Missing semicolon on last item, there should be one there.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED semicolonMissingLastItem: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void semicolonMissingFirstItem() {
        s = new Scanner("move\nturnL;\nmove;\nmove;\nturnR;\nwait;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Missing semicolon on the first item, there should be one there.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED semicolonMissingFirstItem: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void semicolonPresentAfterBlock() {
        s = new Scanner("move;\nturnL;\nloop{move;};");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Semicolon present after block closing brace, there should not be one there.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED semicolonPresentAfterBlock: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void semicolonPresentAfterLoop() {
        s = new Scanner("move;\nturnL;\nloop;{move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Semicolon present after loop keyword, there should not be one there.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED semicolonPresentAfterLoop: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * The following are the invalid programs provided. This means you don't
     * have to run them manually!
     * 
     * They are named ``bad?'' after their program names ``s0_bad?.prog'' where
     * ? is 1..4.
     */

    @Test
    public void bad1() {
        s = new Scanner("turnL;\nmove;\nturnR\nmove;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("turnR is missing a semicolon. You'll need to check that there's one on the end of each ArtNode.");
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
        s = new Scanner("turnL;\nmove;\nmove;\nturnRight;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("turnRight isn't in our grammar, it should be turnR instead.");
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
        s = new Scanner("loop{}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Remember that Blocks must have //at least one// STMT --  BLOCK ::= { STMT+ }");
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
        s = new Scanner("loop{\n\tmove;\n\tturnL;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("We're missing a closing brace for our block. Remember to check for that brace.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED bad4: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Sentinel test: When first writing these tests I didn't use the correct
     * delimiter so ALL tests caused a ParserFailureException regardless of
     * whether they should or not. This test is here to ensure that a similar
     * problem doesn't occur again.
     * 
     * It's a test for the tests, as it were. Ironically I initially missed
     * the @Test annotation so this never actually ran...
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