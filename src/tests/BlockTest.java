package tests;

import game.Block;
import game.Quadrant;
import java.awt.Color;
import java.awt.Point;

/**
 * Comprehensive test suite for Block.java
 * Tests all major methods with known board states and validates results
 *
 * Acknowledgements:
 *  + Tests written by Gemini 3. 
 */
public class BlockTest {

    private static int passCount = 0;
    private static int failCount = 0;

    public static void main(String[] args) {
        System.out.println("=== Running Comprehensive Block Tests ===\n");

        // Run all test suites
        testConstructor();
        testSmash();
        testSwap();
        testRotate();
        testFlatten();
        testGetSelectedBlock();
        testUpdateBlockLocations();
        testCopyBlock();

        // Print summary
        System.out.println("\n=== Test Summary ===");
        System.out.println("Passed: " + passCount);
        System.out.println("Failed: " + failCount);
        System.out.println("Total:  " + (passCount + failCount));
    }

    // ===== HELPER METHODS =====

    /**
     * Create a known board state for testing
     * Returns a block with 4 children, each with a specific color
     */
    private static Block createKnownBoard() {
        Block root = new Block(new Point(0, 0), 2, null, 100);
        root.smash(); // Split into 4 children

        // Set known colors for testing
        // UR(0), UL(1), LL(2), LR(3)
        root.getChildren()[0].setColor(Color.RED); // UR
        root.getChildren()[1].setColor(Color.BLUE); // UL
        root.getChildren()[2].setColor(Color.GREEN); // LL
        root.getChildren()[3].setColor(Color.YELLOW); // LR

        return root;
    }

    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("✓ PASS: " + testName);
            passCount++;
        } else {
            System.out.println("✗ FAIL: " + testName);
            failCount++;
        }
    }

    private static void assertEquals(String testName, Object expected, Object actual) {
        if ((expected == null && actual == null) || (expected != null && expected.equals(actual))) {
            System.out.println("✓ PASS: " + testName);
            passCount++;
        } else {
            System.out.println("✗ FAIL: " + testName + " (expected: " + expected + ", got: " + actual + ")");
            failCount++;
        }
    }

    // ===== CONSTRUCTOR TESTS =====

    private static void testConstructor() {
        System.out.println("\n--- Testing Constructor ---");

        // Test root block
        Block root = new Block(new Point(0, 0), 3, null, 100);
        assertEquals("Root block level", 0, root.getLevel());
        assertEquals("Root block max_depth", 3, root.getMax_depth());
        assertEquals("Root block size", 100, root.getRect().width);
        assertEquals("Root block has no children initially", 0, root.getChildren().length);
        assertTrue("Root block has color", root.getColor() != null);

        // Test child block
        Block parent = new Block(new Point(0, 0), 3, null, 100);
        Block child = new Block(new Point(0, 0), 3, parent, 100);
        assertEquals("Child block level", 1, child.getLevel());
        assertEquals("Child block size is half parent", 50, child.getRect().width);
    }

    // ===== SMASH TESTS =====

    private static void testSmash() {
        System.out.println("\n--- Testing smash() ---");

        // Test normal smash
        Block b = new Block(new Point(0, 0), 2, null, 100);
        boolean result = b.smash();
        assertTrue("Smash succeeds on unsmashed block below max_depth", result);
        assertEquals("Has 4 children after smash", 4, b.getChildren().length);

        // Verify children positions and sizes
        assertEquals("UR child position x", 50, b.getChildren()[0].getRect().x);
        assertEquals("UR child position y", 0, b.getChildren()[0].getRect().y);
        assertEquals("Child size is half parent", 50, b.getChildren()[0].getRect().width);

        // Test cannot smash already smashed block
        result = b.smash();
        assertTrue("Cannot smash block that already has children", !result);

        // Test cannot smash at max depth
        Block deepBlock = new Block(new Point(0, 0), 0, null, 100);
        result = deepBlock.smash();
        assertTrue("Cannot smash block at max_depth", !result);

        // Test smashing at level 1 (one level below max)
        Block levelOne = new Block(new Point(0, 0), 2, null, 100);
        levelOne.smash();
        Block childToSmash = levelOne.getChildren()[0];
        result = childToSmash.smash();
        assertTrue("Can smash child block that's not at max depth", result);
        assertEquals("Grandchildren size is 1/4 of root", 25, childToSmash.getChildren()[0].getRect().width);
    }

    // ===== SWAP TESTS =====

    private static void testSwap() {
        System.out.println("\n--- Testing swap() ---");

        // Cannot swap block without children
        Block noChildren = new Block(new Point(0, 0), 2, null, 100);
        boolean result = noChildren.swap(true);
        assertTrue("Cannot swap block without children", !result);

        // Test vertical swap
        Block b = createKnownBoard();
        // Original: UR=RED, UL=BLUE, LL=GREEN, LR=YELLOW
        result = b.swap(true);
        assertTrue("Vertical swap succeeds", result);
        // After vertical swap: UL↔LL, UR↔LR
        assertEquals("After vertical swap: position 0 (UR) is YELLOW", Color.YELLOW, b.getChildren()[0].getColor());
        assertEquals("After vertical swap: position 1 (UL) is GREEN", Color.GREEN, b.getChildren()[1].getColor());
        assertEquals("After vertical swap: position 2 (LL) is BLUE", Color.BLUE, b.getChildren()[2].getColor());
        assertEquals("After vertical swap: position 3 (LR) is RED", Color.RED, b.getChildren()[3].getColor());

        // Test horizontal swap
        Block b2 = createKnownBoard();
        // Original: UR=RED, UL=BLUE, LL=GREEN, LR=YELLOW
        result = b2.swap(false);
        assertTrue("Horizontal swap succeeds", result);
        // After horizontal swap: UL↔UR, LL↔LR
        assertEquals("After horizontal swap: position 0 (UR) is BLUE", Color.BLUE, b2.getChildren()[0].getColor());
        assertEquals("After horizontal swap: position 1 (UL) is RED", Color.RED, b2.getChildren()[1].getColor());
        assertEquals("After horizontal swap: position 2 (LL) is YELLOW", Color.YELLOW, b2.getChildren()[2].getColor());
        assertEquals("After horizontal swap: position 3 (LR) is GREEN", Color.GREEN, b2.getChildren()[3].getColor());
    }

    // ===== ROTATE TESTS =====

    private static void testRotate() {
        System.out.println("\n--- Testing rotate() ---");

        // Cannot rotate block without children
        Block noChildren = new Block(new Point(0, 0), 2, null, 100);
        boolean result = noChildren.rotate(true);
        assertTrue("Cannot rotate block without children", !result);

        // Test clockwise rotation
        Block b = createKnownBoard();
        // Original: UR=RED, UL=BLUE, LL=GREEN, LR=YELLOW
        result = b.rotate(true);
        assertTrue("Clockwise rotation succeeds", result);
        // After CW rotation: UL→UR→LR→LL→UL
        assertEquals("After CW: position 0 (UR) is BLUE", Color.BLUE, b.getChildren()[0].getColor());
        assertEquals("After CW: position 1 (UL) is GREEN", Color.GREEN, b.getChildren()[1].getColor());
        assertEquals("After CW: position 2 (LL) is YELLOW", Color.YELLOW, b.getChildren()[2].getColor());
        assertEquals("After CW: position 3 (LR) is RED", Color.RED, b.getChildren()[3].getColor());

        // Test counter-clockwise rotation
        Block b2 = createKnownBoard();
        // Original: UR=RED, UL=BLUE, LL=GREEN, LR=YELLOW
        result = b2.rotate(false);
        assertTrue("Counter-clockwise rotation succeeds", result);
        // After CCW rotation: UL→LL→LR→UR→UL
        assertEquals("After CCW: position 0 (UR) is YELLOW", Color.YELLOW, b2.getChildren()[0].getColor());
        assertEquals("After CCW: position 1 (UL) is RED", Color.RED, b2.getChildren()[1].getColor());
        assertEquals("After CCW: position 2 (LL) is BLUE", Color.BLUE, b2.getChildren()[2].getColor());
        assertEquals("After CCW: position 3 (LR) is GREEN", Color.GREEN, b2.getChildren()[3].getColor());

        // Test 4 rotations return to original
        Block b3 = createKnownBoard();
        b3.rotate(true);
        b3.rotate(true);
        b3.rotate(true);
        b3.rotate(true);
        assertEquals("After 4 CW rotations: back to RED at UR", Color.RED, b3.getChildren()[0].getColor());
        assertEquals("After 4 CW rotations: back to BLUE at UL", Color.BLUE, b3.getChildren()[1].getColor());

        // Test recursive rotation with grandchildren
        testRecursiveRotation();
    }

    // ===== RECURSIVE ROTATION TEST =====

    private static void testRecursiveRotation() {
        System.out.println("\n--- Testing Recursive Rotation (with grandchildren) ---");

        // Create a root block with children and grandchildren
        Block root = new Block(new Point(0, 0), 3, null, 100);
        root.smash(); // Create 4 children at level 1

        // Set colors for children: UR=RED, UL=BLUE, LL=GREEN, LR=YELLOW
        root.getChildren()[0].setColor(Color.RED); // UR
        root.getChildren()[1].setColor(Color.BLUE); // UL
        root.getChildren()[2].setColor(Color.GREEN); // LL
        root.getChildren()[3].setColor(Color.YELLOW); // LR

        // Smash the UR child to create grandchildren at level 2
        root.getChildren()[0].smash();

        // Set distinguishable colors for the UR grandchildren
        // These are grandchildren of root, children of UR
        Block urChild = root.getChildren()[0];
        urChild.getChildren()[0].setColor(Color.MAGENTA); // UR of UR
        urChild.getChildren()[1].setColor(Color.CYAN); // UL of UR
        urChild.getChildren()[2].setColor(Color.ORANGE); // LL of UR
        urChild.getChildren()[3].setColor(Color.PINK); // LR of UR

        System.out.println("Before rotation:");
        System.out.println("  Root children: UR=RED(smashed), UL=BLUE, LL=GREEN, LR=YELLOW");
        System.out.println("  UR grandchildren: UR=MAGENTA, UL=CYAN, LL=ORANGE, LR=PINK");

        // Rotate root clockwise
        boolean result = root.rotate(true);
        assertTrue("Recursive rotation succeeds", result);

        System.out.println("\nAfter CW rotation:");

        // Verify children rotated: UL→UR→LR→LL→UL
        // Position 0 (UR) should now have what was at UL (BLUE, which was unsmashed)
        assertEquals("After CW: root[0] is BLUE (was UL)", Color.BLUE, root.getChildren()[0].getColor());

        // Position 1 (UL) should now have what was at LL (GREEN)
        assertEquals("After CW: root[1] is GREEN (was LL)", Color.GREEN, root.getChildren()[1].getColor());

        // Position 2 (LL) should now have what was at LR (YELLOW)
        assertEquals("After CW: root[2] is YELLOW (was LR)", Color.YELLOW, root.getChildren()[2].getColor());

        // Position 3 (LR) should now have what was at UR (RED, which had grandchildren)
        // This block should still have children (the grandchildren)
        Block movedUR = root.getChildren()[3];
        assertTrue("After CW: root[3] still has grandchildren", movedUR.hasChildren());

        // THE KEY TEST: Verify the grandchildren ALSO rotated within their parent
        // The UR block's children rotated: UL→UR→LR→LL→UL
        // (CYAN→MAGENTA→PINK→ORANGE→CYAN)
        assertEquals("After CW: grandchild[0] is CYAN (was UL of UR)",
                Color.CYAN, movedUR.getChildren()[0].getColor());
        assertEquals("After CW: grandchild[1] is ORANGE (was LL of UR)",
                Color.ORANGE, movedUR.getChildren()[1].getColor());
        assertEquals("After CW: grandchild[2] is PINK (was LR of UR)",
                Color.PINK, movedUR.getChildren()[2].getColor());
        assertEquals("After CW: grandchild[3] is MAGENTA (was UR of UR)",
                Color.MAGENTA, movedUR.getChildren()[3].getColor());

        System.out.println("  Root children: UR=BLUE, UL=GREEN, LL=YELLOW, LR=RED(smashed)");
        System.out.println("  LR grandchildren: UR=CYAN, UL=ORANGE, LL=PINK, LR=MAGENTA");
        System.out.println("  ✓ Both parent AND grandchildren rotated correctly!");

        // Additional test: Counter-clockwise should reverse it
        Block root2 = new Block(new Point(0, 0), 3, null, 100);
        root2.smash();
        root2.getChildren()[0].setColor(Color.RED);
        root2.getChildren()[1].setColor(Color.BLUE);
        root2.getChildren()[2].setColor(Color.GREEN);
        root2.getChildren()[3].setColor(Color.YELLOW);
        root2.getChildren()[0].smash();
        root2.getChildren()[0].getChildren()[0].setColor(Color.MAGENTA);
        root2.getChildren()[0].getChildren()[1].setColor(Color.CYAN);
        root2.getChildren()[0].getChildren()[2].setColor(Color.ORANGE);
        root2.getChildren()[0].getChildren()[3].setColor(Color.PINK);

        // CCW rotation
        root2.rotate(false);
        Block movedUR_CCW = root2.getChildren()[1]; // UR moved to UL in CCW

        assertEquals("After CCW: grandchild[0] is PINK (CCW rotation)",
                Color.PINK, movedUR_CCW.getChildren()[0].getColor());
        assertEquals("After CCW: grandchild[1] is MAGENTA (CCW rotation)",
                Color.MAGENTA, movedUR_CCW.getChildren()[1].getColor());
        assertEquals("After CCW: grandchild[2] is CYAN (CCW rotation)",
                Color.CYAN, movedUR_CCW.getChildren()[2].getColor());
        assertEquals("After CCW: grandchild[3] is ORANGE (CCW rotation)",
                Color.ORANGE, movedUR_CCW.getChildren()[3].getColor());
    }

    // ===== FLATTEN TESTS =====

    private static void testFlatten() {
        System.out.println("\n--- Testing flatten() ---");

        // Test simple unsmashed block
        Block simple = new Block(new Point(0, 0), 2, null, 100);
        simple.setColor(Color.RED);
        Color[][] grid = simple.flatten();

        assertEquals("Grid dimensions match (rows)", 4, grid.length);
        assertEquals("Grid dimensions match (cols)", 4, grid[0].length);

        // All cells should be RED
        boolean allRed = true;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!Color.RED.equals(grid[i][j])) {
                    allRed = false;
                }
            }
        }
        assertTrue("All cells are RED for unsmashed block", allRed);

        // Test smashed block
        Block smashed = new Block(new Point(0, 0), 1, null, 100);
        smashed.smash();
        smashed.getChildren()[0].setColor(Color.RED); // UR
        smashed.getChildren()[1].setColor(Color.BLUE); // UL
        smashed.getChildren()[2].setColor(Color.GREEN); // LL
        smashed.getChildren()[3].setColor(Color.YELLOW); // LR

        Color[][] grid2 = smashed.flatten();
        assertEquals("Smashed grid dimensions (rows)", 2, grid2.length);
        assertEquals("Smashed grid dimensions (cols)", 2, grid2[0].length);

        // Verify quadrant colors
        // Grid layout: [row][col]
        // [0,0]=UL(BLUE), [0,1]=UR(RED)
        // [1,0]=LL(GREEN), [1,1]=LR(YELLOW)
        assertEquals("UL quadrant is BLUE", Color.BLUE, grid2[0][0]);
        assertEquals("UR quadrant is RED", Color.RED, grid2[0][1]);
        assertEquals("LL quadrant is GREEN", Color.GREEN, grid2[1][0]);
        assertEquals("LR quadrant is YELLOW", Color.YELLOW, grid2[1][1]);
    }

    // ===== GET SELECTED BLOCK TESTS =====

    private static void testGetSelectedBlock() {
        System.out.println("\n--- Testing getSelectedBlock() ---");

        Block root = new Block(new Point(0, 0), 2, null, 100);
        root.smash();

        // Test selecting root at level 0
        Block selected = root.getSelectedBlock(50, 50, 0);
        assertEquals("Selected root block", root, selected);

        // Test selecting child at level 1
        // UR child is at (50, 0) to (100, 50)
        selected = root.getSelectedBlock(75, 25, 1);
        assertEquals("Selected UR child", root.getChildren()[0], selected);

        // UL child is at (0, 0) to (50, 50)
        selected = root.getSelectedBlock(25, 25, 1);
        assertEquals("Selected UL child", root.getChildren()[1], selected);

        // Test out of bounds
        selected = root.getSelectedBlock(150, 150, 0);
        assertEquals("Out of bounds returns null", null, selected);

        // Test level too deep
        selected = root.getSelectedBlock(25, 25, 5);
        assertEquals("Level too deep returns null", null, selected);
    }

    // ===== UPDATE BLOCK LOCATIONS TESTS =====

    private static void testUpdateBlockLocations() {
        System.out.println("\n--- Testing updateBlockLocations() ---");

        Block root = new Block(new Point(0, 0), 2, null, 100);
        root.smash();

        // Manually move root
        root.setPoint(new Point(50, 50));

        // Update all child locations
        root.updateBlockLocations();

        // Verify children moved correctly
        assertEquals("UR child x after update", 100, root.getChildren()[0].getRect().x);
        assertEquals("UR child y after update", 50, root.getChildren()[0].getRect().y);
        assertEquals("UL child x after update", 50, root.getChildren()[1].getRect().x);
        assertEquals("UL child y after update", 50, root.getChildren()[1].getRect().y);
    }

    // ===== COPY BLOCK TESTS =====

    private static void testCopyBlock() {
        System.out.println("\n--- Testing copyBlock() ---");

        Block original = createKnownBoard();
        Block copy = original.copyBlock(original);

        // Verify it's a different object
        assertTrue("Copy is different object", original != copy);

        // Verify structure is the same
        assertEquals("Copy has same number of children",
                original.getChildren().length, copy.getChildren().length);
        assertEquals("Copy has same level", original.getLevel(), copy.getLevel());
        assertEquals("Copy has same max_depth", original.getMax_depth(), copy.getMax_depth());

        // Verify children colors match
        for (int i = 0; i < 4; i++) {
            assertEquals("Child " + i + " color matches",
                    original.getChildren()[i].getColor(),
                    copy.getChildren()[i].getColor());
        }

        // Verify modifying copy doesn't affect original
        copy.getChildren()[0].setColor(Color.MAGENTA);
        assertTrue("Modifying copy doesn't affect original",
                !copy.getChildren()[0].getColor().equals(original.getChildren()[0].getColor()));
    }
}
