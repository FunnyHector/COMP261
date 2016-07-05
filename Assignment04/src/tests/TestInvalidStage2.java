package tests;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import core.Parser;
import core.ParserFailureException;

public class TestInvalidStage2 {
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

    /*
     * Move options
     */

    @Test
    public void moveOptions() {
        s = new Scanner("move 1);");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Missing the opening bracket");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED moveOptions: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void moveOptions2() {
        s = new Scanner("move (1;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Missing the closing bracket");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED moveOptions2: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void moveOptions3() {
        s = new Scanner("move (1, 1);");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Move takes exactly 1 parameter");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED moveOptions3: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void moveOptions4() {
        s = new Scanner("move\n()\n;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Move is missing a parameter but has brackets");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED moveOptions4: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void moveOptions5() {
        s = new Scanner("move\n(move)\n;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Move is not EXP, it's an ACT");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED moveOptions5: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Wait options
     */

    @Test
    public void waitOptions() {
        s = new Scanner("wait 1);");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Missing the opening bracket");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED waitOptions: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void waitOptions2() {
        s = new Scanner("wait (1;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Missing the closing bracket");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED waitOptions2: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void waitOptions3() {
        s = new Scanner("wait (1, 1);");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Wait takes exactly 1 parameter");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED waitOptions3: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void waitOptions4() {
        s = new Scanner("wait\n()\n;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Wait is missing a parameter but has brackets");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED waitOptions4: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void waitOptions5() {
        s = new Scanner("wait\n(move)\n;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Wait is not EXP, it's an ACT");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED waitOptions5: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * If else statements
     */

    @Test
    public void elseWithoutIf() {
        s = new Scanner("else{move;}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Else clauses must have an if");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED elseWithoutIf: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void elseNoBlock() {
        s = new Scanner("if (eq(1,1)){turnAround;}\nelse move;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Else must contain a block");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED elseNoBlock: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void elseBlockSemicolon() {
        s = new Scanner("if (eq(1,1)){turnAround;}\nelse{move;};");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Blocks cannot have a semi colon after the closing brace");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED elseBlockSemicolon: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void ifElseInsideBlock() {
        s = new Scanner("if (eq(1,1)){ else{move;} }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Else must contain a block");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED ifElseInsideBlock: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Numbers
     */

    @Test
    public void zeroLeadingNumber() {
        s = new Scanner("if (eq(1,012)){ move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Numbers (as of stage 2) must start with 1-9 instead of 0-9");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED zeroLeadingNumber: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void negativeZero() {
        s = new Scanner("if (eq(1,-0)){ move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("The regular expression definition for NUM forbids negative 0");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED negativeZero: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void doubleNegativeNumbers() {
        s = new Scanner("if (eq(1,--1)){ move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("A number is allowed either 0 or 1 negative sign.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED doubleNegativeNumbers: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * COND tests
     */

    @Test
    public void nestedConditionals() {
        s = new Scanner("if (and ( not(1), gt(add(10,4),mul(0,2)))){ shieldOn; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("not must contain a COND");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED nestedConditionals: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void divisionFloats() {
        s = new Scanner("wait(div(6.0,2.0));");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Floats are not allowed as a number definition.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED divisionFloats: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void multiplicationFloats() {
        s = new Scanner("wait(mul(1.0,2.0));");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Floats are not allowed as a number definition.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED multiplicationFloats: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void additionFloats() {
        s = new Scanner("wait(add(1.0,2.0));");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Floats are not allowed as a number definition.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED additionFloats: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void subtractionFloats() {
        s = new Scanner("wait(sub(1.0,2.0));");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Floats are not allowed as a number definition.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED subtractionFloats: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Provided tests
     */

    @Test
    public void bad1() {
        s = new Scanner("move(eq(1, 1));");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("eq is a COND, not a NUM, SEN or OP(EXP,EXP)");
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
        s = new Scanner("or(shieldOn, shieldOff);");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("or is not a STMT so is not valid on it's own");
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
        s = new Scanner("not(5);");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("not requires a COND, NUM is not a COND");
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
        s = new Scanner("turnL(2);");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("turnL does not take parameters");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED bad4: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void bad5() {
        s = new Scanner("add(1, 2, 3);");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("add takes exactly two CONDS, three are presented here");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED bad5: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void bad6() {
        s = new Scanner("if(1){\n" + "\tmove;\n" + "\tturnL;\n" + "}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("if takes one COND. NUM is not a COND.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED bad6: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void bad7() {
        s = new Scanner("if(eq(numBarrels, 0)){\n" + "\tmove;\n" + "} else(gt(numBarrels, 1)){\n" + "\tturnL;" + "}");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            fail("Else does not take a COND.");
        } catch (ParserFailureException e) {
            if (printOutput) {
                System.out.println("PASSED bad7: " + e.getMessage());
            }
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

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