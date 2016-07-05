package tests;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import core.Parser;
import core.ParserFailureException;

public class TestValidStage3 {
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
    public void instructionAssign() {
        s = new Scanner("$myVariable = 2;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionAssign");
            }
        } catch (ParserFailureException e) {
            fail("ASSGN starts with a VAR, and then an equals sign, and finally an EXP" + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionAssignNegative() {
        s = new Scanner("$myVariable = -2;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionAssign");
            }
        } catch (ParserFailureException e) {
            fail("ASSGN starts with a VAR, and then an equals sign, and finally an EXP" + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionAssignAssign() {
        // This test will fail in Stage 4: $v3 hasn't been defined.

        // Variables can legally be assigned to other variables.
        // Variables don't need to be assigned before they're used, so $v3 =
        // $v3; is legal.
        s = new Scanner("$v1 = 2;$v2 = $v1; $v3 = $v3;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionAssignAssign");
            }
        } catch (ParserFailureException e) {
            fail("Not valid in stage 4. Remember variables do not need to be defined before they're used, and they can be assigned to each other.."
                    + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionAssignNumbers() {
        s = new Scanner("$M3yVar5iab8l6e60 = 6;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionAssignNumbers");
            }
        } catch (ParserFailureException e) {
            fail("Variables can have any number of numbers after the first character, which must be a letter."
                    + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionBarrelFBParameter() {
        s = new Scanner("while (eq(barrelFB(1), 3)){ move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionBarrelFBParameter");
            }
        } catch (ParserFailureException e) {
            fail("BarrelFB can take a parameter now." + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionBarrelLRParameter() {
        s = new Scanner("while (eq(barrelLR(1), 3)){ move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionBarrelLRParameter");
            }
        } catch (ParserFailureException e) {
            fail("BarrelLR can take a parameter now." + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionBarrelLR() {
        s = new Scanner("while (eq(barrelLR, 3)){ move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionBarrelLR");
            }
        } catch (ParserFailureException e) {
            fail("BarrelLR does not need a parameter." + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void instructionBarrelFB() {
        s = new Scanner("while (eq(barrelFB, 3)){ move; }");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED instructionBarrelFB");
            }
        } catch (ParserFailureException e) {
            fail("BarrelFB does not need a parameter." + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * If and else if, and else.
     */

    @Test
    public void ifStatement() {
        // The robot should move forward twice.
        s = new Scanner("$movement3VAR = 3;if \n(\n\n\neq(3,$movement3VAR))\t{\nmove\n;\n} move;");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifElseIfStatementNoElse");
            }
        } catch (ParserFailureException e) {
            fail("If statements can survive on their own" + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void ifElseIfStatementNoElse() {
        // The robot should move forward 3 times.
        s = new Scanner(
                "$movement3VAR = 3;if \n(\n\n\nand\n (eq(0,1),lt(0,5)))\t{\nmove\n;\n} elif(eq($movement3VAR,$movement3VAR\n)){move($movement3VAR);}\n");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifElseIfStatementNoElse");
            }
        } catch (ParserFailureException e) {
            fail("If statements can have else if statements now!" + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void ifElseIfStatementNoElseFalse() {
        // Nothing should happen as $movement3VAR has been initialised.
        s = new Scanner(
                "$movement3VAR = 3;if \n(\n\n\nand\n (eq(0,1),lt(0,5)))\t{\nmove\n;\n} elif(eq($movement3VAR,0\n)){move($movement3VAR);}\n");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifElseIfStatementNoElseFalse");
            }
        } catch (ParserFailureException e) {
            fail("Else statements are optional" + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void ifElseIfElseIfElseIfElseStatement() {
        // The second else if should occur (turning around twice), and nothing
        // else
        s = new Scanner("if \n(\n\n\nand\n (eq(1,1),lt(0,5)))\t{\nmove\n;\n}" + "elif(lt(2,0)\n){move;}"
                + "elif(lt(0,1)\n){turnAround;turnAround;}" + "elif(gt(2,0)\n){move(2);}" // This
                                                                                          // is
                                                                                          // true,
                                                                                          // but
                                                                                          // should
                                                                                          // not
                                                                                          // happen
                + "else{\n\tturnAround;}\n");
        s.useDelimiter(delimiter);

        try {
            Parser.parseProgram(s);
            if (printOutput) {
                System.out.println("PASSED ifElseIfElseIfElseIfElseStatement");
            }
        } catch (ParserFailureException e) {
            fail("." + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    /*
     * Provided programs
     */

    @Test
    public void full() {
        // This test will fail in Stage 4: $empty hasn't been defined.

        s = new Scanner("while(or(gt(fuelLeft, sub(mul(div(5, add(1, 4)), -1), -1)), eq(1, 0))){\n" + "\tshieldOff;\n"
                + "\t$numBar = numBarrels;\n" + "\t$spurious = add($numBar, $empty);\n"
                + "\t$spurious = mul($spurious, $empty);\n" + "\tif(not(not(eq($numBar, 0)))){\n"
                + "\t\tif(and(eq(oppLR, $thresh), eq(oppFB, 1))){\n" + "\t\t\ttakeFuel;\n" + "\t\t} else{\n"
                + "\t\t\tif(and(eq(oppLR, 0), eq(oppFB, -1))){\n" + "\t\t\t\tif(gt(wallDist, 0)){\n"
                + "\t\t\t\t\tmove;\n" + "\t\t\t\t} else{\n" + "\t\t\t\t\tshieldOn;\n" + "\t\t\t\t}\n"
                + "\t\t\t} else{\n" + "\t\t\t\twait(1);\n" + "\t\t\t}\n" + "\t\t}\n" + "\t} else{\n"
                + "\t        $first = 1;\n" + "\t\tif(and(eq(barrelFB(1), 0), eq(barrelLR($first), 0))){\n"
                + "\t\t\ttakeFuel;\n" + "\t\t} elif (gt($numBar, $numBar )){\n" + "\t\t     move(10);\n"
                + "\t\t} elif (lt(barrelLR(sub(numBarrels, 1)), -45) ){\n" + "\t\t     move(barrelFB(3));\n"
                + "\t\t} else{\n" + "\t\t\tif(eq(0, barrelFB)){\n" + "\t\t\t\tif(lt(barrelLR, 0)){\n"
                + "\t\t\t\t\tturnL;\n" + "\t\t\t\t} else{\n" + "\t\t\t\t\tturnR;\n" + "\t\t\t\t}\n" + "\t\t\t} else{\n"
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
            fail("Not valid in stage 4. Failed full" + e.getMessage());
        } catch (Exception e) {
            fail("Shouldn't have thrown exception: " + e);
        }
    }

    @Test
    public void simple() {
        s = new Scanner("$count = 5; \n" + "while (gt($count, 0)){\n" + "  move;\n" + "  $count = sub($count, 1);\n"
                + "}\n" + "turnL;\n");

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
    public void handoutExample() {
        s = new Scanner("while(gt(fuelLeft, 0)){      \n" + "    if(eq(numBarrels, 0)){   \n" + "        wait;\n"
                + "    } elif ( lt(add(oppFB,oppLR), 3) ) {  \n" + "        move(oppFB);                      \n"
                + "    } else{            \n" + "        $lr = barrelLR;       \n" + "        $fb = barrelFB;       \n"
                + "        if(and(eq($lr, 0), eq($fb, 0))){   \n" + "            takeFuel;                    \n"
                + "        } else{\n" + "            if(eq($fb, 0)){        \n"
                + "                if(lt($lr, 0)){    \n" + "                    turnL; \n"
                + "                } else{\n" + "                    turnR;\n" + "                }\n"
                + "            } else{\n" + "                if(gt($fb, 0)){\n" + "                    move;\n"
                + "                } else{\n" + "                    turnAround;\n" + "                }\n"
                + "            }\n" + "        }\n" + "    }\n" + "}");

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