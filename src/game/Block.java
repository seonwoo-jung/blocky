package game;
/*
=== Module Description ===

This file contains the Block class, the main data structure used in the game.
 */

import java.awt.*;
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

	// 트리 구조
	private Block parent;
	// children[0] = UR, children[1] = UL, children[2] = LL, children[3] = LR
	private Block[] children;
	private Rectangle rect;

	private static Block highlighted;

	// 레벨 정보
	private int level;
	private int max_depth;

	// 리프일 때만 색을 가진다
	private Color color;

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
			// 루트 블록
			this.level = 0;
			this.rect = new Rectangle(p.x, p.y, maxSize, maxSize);
		} else {
			// 자식 블록: 항상 부모의 절반 크기
			this.level = parent.level + 1;
			int half = parent.rect.width / 2;
			this.rect = new Rectangle(p.x, p.y, half, half);
		}

		// 처음에는 자식 없음
		this.children = new Block[0];
		// 리프면 색을 가진다
		this.color = randomColor();
	}

	private Color randomColor() {
		Color[] palette = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
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

		// children 인덱스: 0=UR, 1=UL, 2=LL, 3=LR
		Block ur = children[0], ul = children[1], ll = children[2], lr = children[3];

		if (isVertical) {
			// 테스트 기대 패턴에 맞춘 vertical swap:
			// after vertical: 0=YELLOW(LR), 1=GREEN(LL), 2=BLUE(UL), 3=RED(UR)
			children[0] = lr; // UR <= LR
			children[3] = ur; // LR <= UR
			children[1] = ll; // UL <= LL
			children[2] = ul; // LL <= UL
		} else {
			// horizontal swap:
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
			// 시계 방향:
			// UL -> UR, LL -> UL, LR -> LL, UR -> LR
			children[0] = ul; // new UR
			children[1] = ll; // new UL
			children[2] = lr; // new LL
			children[3] = ur; // new LR
		} else {
			// 반시계 방향:
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
		// 이미 자식이 있으면 불가
		if (hasChildren()) {
			return false;
		}
		// max depth면 불가
		if (level >= max_depth) {
			return false;
		}

		int half = rect.width / 2;
		children = new Block[4];

		// children 인덱스: 0=UR, 1=UL, 2=LL, 3=LR
		children[0] = new Block(new Point(rect.x + half, rect.y), max_depth, this, half);          // UR
		children[1] = new Block(new Point(rect.x, rect.y), max_depth, this, half);                 // UL
		children[2] = new Block(new Point(rect.x, rect.y + half), max_depth, this, half);          // LL
		children[3] = new Block(new Point(rect.x + half, rect.y + half), max_depth, this, half);   // LR

		// 내부 노드는 색 없음
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
			// BlockTest는 grid[row][col] 형태로 접근하므로 j,i 순서로 채운다.
			for (int i = col; i < col + size; i++) {
				for (int j = row; j < row + size; j++) {
					grid[j][i] = color;
				}
			}
		} else {
			int half = size / 2;
			// children[1] = UL, children[0] = UR, children[2] = LL, children[3] = LR
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

	// ===== Random block selection (for AI 등) =====

	public Block getRandomBlock() {
		Random r = new Random();

		Block b = null;
		while (b == null) {
			int x = r.nextInt(rect.width) + rect.x;
			int y = r.nextInt(rect.height) + rect.y;
			int l = r.nextInt(max_depth + 1);
			b = getSelectedBlock(x, y, l);
		}
		return b;
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

	public void restoreFrom(Block backup) {
		this.color = backup.color;
		this.level = backup.level;
		this.rect = new Rectangle(backup.rect);

		if (backup.hasChildren()) {
			this.children = new Block[4];
			for (int i = 0; i < 4; i++) {
				this.children[i] = deepCopyChild(backup.children[i], this);
			}
		} else {
			this.children = null;
		}
	}
}
