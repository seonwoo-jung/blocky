package game;
/*
=== Module Description ===

This file contains the Block class, the main data structure used in the game.
 */

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
 *   - their max_depth is the same as that of this Block,
 *   - their size is half that of this Block,
 *   - their level is one greater than that of this Block,
 *   - their position is determined by the position and size of this Block, and
 *   - this Block's color is null
 * - If this Block has no children, its color is not null
 * - level <= max_depth
 */
public class Block {

	private Block parent;
	// children[0] = UR, children[1] = UL, children[2] = LL, children[3] = LR
	private Block[] children;
	private Rectangle rect;

	private static Block highlighted;

	private int level;
	private int max_depth;

	private Color color;
	private Game game;

	/**
	 * Constructor for a Block
	 *
	 * @param p         is the location of the block (x,y)
	 * @param max_depth is the depth limit for this game
	 * @param parent    is a parent of this node if one exists
	 * @param maxSize   is the maxSize of the board (used only for root)
	 */
	public Block(Point p, int max_depth, Block parent, int maxSize) {
		this.max_depth = max_depth;
		this.parent = parent;

		if (parent == null) {
			this.level = 0;
			this.rect = new Rectangle(p.x, p.y, maxSize, maxSize);
		} else {
			this.level = parent.level + 1;
			int half = parent.rect.width / 2;
			this.rect = new Rectangle(p.x, p.y, half, half);
		}

		this.children = new Block[0];
		this.color = randomColor();
	}

	private Color randomColor() {
		Color[] palette = game.getColorList();
		return palette[new Random().nextInt(palette.length)];
	}

	public boolean hasChildren() {
		return children != null && children.length == 4;
	}

	/**
	 * Draw this Block and all its descendants.
	 */
	public void draw(Graphics2D g) {
		if (!hasChildren()) {
			g.setColor(color);
			g.fill(rect);
			g.setColor(Color.BLACK);
			g.draw(rect);
		} else {
			for (Block child : children) {
				child.draw(g);
			}
		}
	}

	/**
	 * Swap the child Blocks of this Block.
	 *
	 * If this Block has no children, do nothing, return false.
	 * Return true if you successfully swap the blocks.
	 *
	 * @param isVertical provides the direction of the swap
	 */
	public boolean swap(boolean isVertical) {
		if (!hasChildren()) {
			return false;
		}

		Block ur = children[0], ul = children[1], ll = children[2], lr = children[3];

		if (isVertical) {
			// after vertical: 0=YELLOW(LR), 1=GREEN(LL), 2=BLUE(UL), 3=RED(UR)
			children[0] = lr; // UR <= LR
			children[3] = ur; // LR <= UR
			children[1] = ll; // UL <= LL
			children[2] = ul; // LL <= UL
		} else {
			// after horizontal: 0=BLUE(UL),1=RED(UR),2=YELLOW(LR),3=GREEN(LL)
			children[0] = ul; // UR <= UL
			children[1] = ur; // UL <= UR
			children[2] = lr; // LL <= LR
			children[3] = ll; // LR <= LL
		}

		updateBlockLocations();
		return true;
	}

	/**
	 * Rotate this Block and all its descendants.
	 *
	 * @param isClockwise determines which direction to rotate
	 */
	public boolean rotate(boolean isClockwise) {
		if (!hasChildren()) {
			return false;
		}

		for (Block c : children) {
			c.rotate(isClockwise);
		}

		// 0=UR, 1=UL, 2=LL, 3=LR
		Block ur = children[0], ul = children[1], ll = children[2], lr = children[3];

		if (isClockwise) {
			// UL -> UR, LL -> UL, LR -> LL, UR -> LR
			children[0] = ul; // new UR
			children[1] = ll; // new UL
			children[2] = lr; // new LL
			children[3] = ur; // new LR
		} else {
			// LR -> UR, UR -> UL, UL -> LL, LL -> LR
			children[0] = lr; // new UR
			children[1] = ur; // new UL
			children[2] = ul; // new LL
			children[3] = ll; // new LR
		}

		updateBlockLocations();
		return true;
	}

	/**
	 * Smash this block.
	 *
	 * A Block can be smashed iff it has no children and level < max_depth.
	 */
	public boolean smash() {
		if (hasChildren()) {
			return false;
		}
		if (level >= max_depth) {
			return false;
		}

		int half = rect.width / 2;
		children = new Block[4];

		children[0] = new Block(new Point(rect.x + half, rect.y), max_depth, this, half);          // UR
		children[1] = new Block(new Point(rect.x, rect.y), max_depth, this, half);                 // UL
		children[2] = new Block(new Point(rect.x, rect.y + half), max_depth, this, half);          // LL
		children[3] = new Block(new Point(rect.x + half, rect.y + half), max_depth, this, half);   // LR

		this.color = null;
		return true;
	}

	/**
	 * Update the position and size of each of the Blocks within this Block.
	 */
	public void updateBlockLocations() {
		if (!hasChildren()) {
			return;
		}

		int half = rect.width / 2;

		// 0=UR, 1=UL, 2=LL, 3=LR
		children[0].rect = new Rectangle(rect.x + half, rect.y, half, half);           // UR
		children[1].rect = new Rectangle(rect.x, rect.y, half, half);                  // UL
		children[2].rect = new Rectangle(rect.x, rect.y + half, half, half);           // LL
		children[3].rect = new Rectangle(rect.x + half, rect.y + half, half, half);    // LR

		for (Block c : children) {
			c.updateBlockLocations();
		}
	}

	/**
	 * Return the Block within this Block that includes the given location
	 * and is at the given level.
	 */
	public Block getSelectedBlock(int x, int y, int level) {
		if (!rect.contains(x, y)) {
			return null;
		}
		if (this.level == level) {
			return this;
		}
		if (!hasChildren()) {
			return null;
		}

		for (Block c : children) {
			Block found = c.getSelectedBlock(x, y, level);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	/**
	 * Return a two-dimensional Color array representing this Block as rows
	 * and columns of unit cells.
	 */
	public Color[][] flatten() {
		int n = 1 << (max_depth - level);
		Color[][] grid = new Color[n][n];
		fillFlatten(grid, 0, 0, n);
		return grid;
	}

	private void fillFlatten(Color[][] grid, int col, int row, int size) {
		if (!hasChildren()) {
			for (int i = col; i < col + size; i++) {
				for (int j = row; j < row + size; j++) {
					grid[j][i] = color;
				}
			}
		} else {
			int half = size / 2;
			children[1].fillFlatten(grid, col, row, half);              // UL
			children[0].fillFlatten(grid, col + half, row, half);       // UR
			children[2].fillFlatten(grid, col, row + half, half);       // LL
			children[3].fillFlatten(grid, col + half, row + half, half);// LR
		}
	}

	// ===== Getters / Setters =====

	public Rectangle getRect() {
		return rect;
	}

	public int getLevel() {
		return level;
	}

	public int getMaxDepth() {
		return max_depth;
	}

	public int getSize() {
		return rect.width;
	}

	public Color getColor() {
		return color;
	}

	public Block[] getChildren() {
		return children;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public void setPoint(Point p) {
		this.rect.x = p.x;
		this.rect.y = p.y;
	}

	// ===== Deep copy & restore (SmartAI / SmartAI2 용) =====

	public Block copyBlock(Block b) {
		Block copy = new Block(
			new Point(b.rect.x, b.rect.y),
			b.max_depth,
			null,
			b.rect.width
		);
		copy.level = b.level;
		copy.color = b.color;

		if (b.hasChildren()) {
			copy.children = new Block[4];
			for (int i = 0; i < 4; i++) {
				copy.children[i] = deepCopyChild(b.children[i], copy);
			}
		}
		return copy;
	}

	private Block deepCopyChild(Block original, Block parentCopy) {
		Block newB = new Block(
			new Point(original.rect.x, original.rect.y),
			parentCopy.max_depth,
			parentCopy,
			original.rect.width
		);

		newB.level = original.level;
		newB.color = original.color;

		if (original.hasChildren()) {
			newB.children = new Block[4];
			for (int i = 0; i < 4; i++) {
				newB.children[i] = deepCopyChild(original.children[i], newB);
			}
		}
		return newB;
	}

	public boolean unsmash() {
		if (!hasChildren())
			return false;

		Map<Color, Integer> count = new HashMap<>();
		for (Block c : children) {
			if (!c.hasChildren()) {
				count.put(c.getColor(), count.getOrDefault(c.getColor(), 0) + 1);
			}
		}

		Color chosen = Color.GRAY; // fallback
		int max = 0;
		for (Color c : count.keySet()) {
			if (count.get(c) > max) {
				chosen = c;
				max = count.get(c);
			}
		}

		children = new Block[0];
		this.color = chosen;
		return true;
	}
}
