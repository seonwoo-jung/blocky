package game;
/*
=== Module Description ===

This file contains the Block class, the main data structure used in the game.
 */

import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import java.util.Random;
import java.awt.Rectangle;

/**
 * A square block in the Blocky game.
 * 
 * === Public Attributes ===
 * position:
 * The (x, y) coordinates of the upper left corner of this Block.
 * Note that (0, 0) is the top left corner of the window.
 * 
 * size:
 * The height and width of this Block. Since all blocks are square,
 * we needn't represent height and width separately.
 * 
 * color:
 * If this block is not subdivided, <color> stores its color.
 *
 * level:
 * The level of this block within the overall block structure.
 * The outermost block, corresponding to the root of the tree,
 * is at level zero. If a block is at level i, its children are at
 * level i+1.
 * 
 * max_depth:
 * The deepest level allowed in the overall block structure.
 * 
 * highlighted:
 * True iff the user has selected this block for action.
 * 
 * children:
 * The blocks into which this block is subdivided. The children are
 * stored in this order: upper-right child, upper-left child,
 * lower-left child, lower-right child.
 * 
 * parent:
 * The block that this block is directly within.
 * 
 * === Representation Invariations ===
 * - len(children) == 0 or len(children) == 4
 * - If this Block has children,
 * - - their max_depth is the same as that of this Block,
 * - - their size is half that of this Block,
 * - - their level is one greater than that of this Block,
 * - - their position is determined by the position and size of this Block, and
 * - - this Block's color is None
 * - If this Block has no children,
 * - its color is not None
 * - level <= max_depth
 */
public class Block {

    private Rectangle rect;
    private Color color;
    private int level;
    private int max_depth;
    private static Block highlighted;
    private Block[] children;
    private Block parent;

    /**
     * 
     * Constructor for a Block
     * 
     * @param p         is the location of the block (x,y)
     * @param max_depth is the depth limit for this game
     * @param parent    is a parent of this node if one exists
     * @param maxSize   is the maxSize of the boar
     */
    public Block(Point p, int max_depth, Block parent, int maxSize) {
        // TODO: implement constructor

    }

    public boolean hasChildren() {
        // TODO: implement hasChildren
    }

    /**
     * Draw this Block and all its descendants.
     * 
     * If this Block has no children, fill it with its color and draw
     * a border around it. If it does have children, draw them instead.
     * 
     * @param g
     */
    public void draw(Graphics2D g) {
        // TODO: implement draw
    }

    /**
     * Swap the child Blocks of this Block.
     * 
     * If this Block has no children, do nothing, return false
     * return true if you successfully swap the blocks
     * 
     * @param isVertical provides the direction of the swap
     */
    public boolean swap(boolean isVertical) {
        // TODO: implement swap
    }

    /**
     * Rotate this Block and all its descendants.
     * 
     * If this Block has no children, do nothing, return false.
     * return true if you successfully swap the blocks
     * 
     * @param isClockwise determines which direction to rotate
     */
    public boolean rotate(boolean isClockwise) {
        // TODO: implement rotate
    }

    /**
     * Smash this block.
     * 
     * If this Block can be smashed,
     * randomly generating four new child Blocks for it.
     * Ensure that the RI's of the Blocks remain satisfied.
     * 
     * A Block can be smashed iff it is not the top-level Block and it
     * is not already at the level of the maximum depth.
     * 
     * Return True if this Block was smashed and False otherwise.
     */
    public boolean smash() {
        // TODO: implement smash
    }

    /**
     * Update the position and size of each of the Blocks within this Block.
     * 
     * Ensure that each is consistent with the position and size of its
     * parent Block.
     */
    public void updateBlockLocations() {
        // TODO: implement updateBlockLocations
    }

    /**
     * Return the Block within this Block that includes the given location
     * and is at the given level. If the level specified is lower than
     * the lowest block at the specified location, then return null
     * 
     * (x, y) coordinates of the location on the window whose corresponding block is
     * to be returned. (e.g. the click location)
     * <level> is the level of the desired Block. Note that
     * if a Block includes the location (x, y), and that Block is subdivided,
     * then one of its four children will contain the location (x, y) also;
     * this is why <level> is needed.
     * 
     * Preconditions:
     * - 0 <= level <= max_depth
     * 
     * @return
     */
    public Block getSelectedBlock(int x, int y, int level) {
        // TODO: implement getSelectedBlock
    }

    /**
     * Return a two-dimensional Color array representing this Block as rows
     * and columns of unit cells.
     * 
     * Return a list of lists L, where,
     * for 0 <= i, j < 2^{max_depth - self.level}
     * - L[i] represents column i and
     * - L[i][j] represents the unit cell at column i and row j.
     * 
     * L[0][0] represents the unit cell in the upper left corner of the Block.
     */
    public Color[][] flatten() {
        // TODO: implement flatten
    }

    public Rectangle getRect() {
        return rect;
    }

    public int getMax_depth() {
        return max_depth;
    }

    public Block[] getChildren() {
        return children;
    }

    public Block getChild(Quadrant q) {
        return children[q.getIndex()];
    }

    public Block getRandomBlock() {
        Random r = new Random();

        Block b = null;
        while (b == null) {
            int x = r.nextInt(768); // Assuming board size 768
            int y = r.nextInt(768);
            int l = r.nextInt(max_depth);
            b = getSelectedBlock(x, y, l);
        }
        return b;
    }

    public Block copyBlock(Block b) {
        // used by the test file
        Block newBlock = new Block(new Point(b.rect.x, b.rect.y), b.max_depth, b.parent, b.rect.width);
        newBlock.color = b.color;
        if (b.hasChildren()) {
            newBlock.children = new Block[4];
            for (int i = 0; i < 4; i++) {
                newBlock.children[i] = copyBlock(b.children[i]);
            }
        }
        return newBlock;
    }

    public int getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void setPoint(Point p) {
        this.rect.x = p.x;
        this.rect.y = p.y;
    }
}