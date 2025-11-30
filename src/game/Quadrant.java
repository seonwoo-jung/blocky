/*
 * === Module Description ===
 * 
 * This file contains the Quadrant enum, used to identify the four children of a Block.
 */

package game;

public enum Quadrant {
    UR(0), UL(1), LL(2), LR(3);

    private final int index;

    private Quadrant(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static Quadrant fromIndex(int index) {
        for (Quadrant q : values()) {
            if (q.index == index) {
                return q;
            }
        }
        throw new IllegalArgumentException("Invalid index: " + index);
    }
}
