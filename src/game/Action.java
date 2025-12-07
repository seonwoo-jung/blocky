package game;

/**
 * Enumeration of the five basic actions that can be applied to a Block.
 */
public enum Action {
	SMASH,
	UNSMASH,
	SWAP_VERTICALLY,
	SWAP_HORIZONTALLY,
	TURN_CW,
	TURN_CCW,
	UNDO;

	public boolean runAction(Block b) {
		if (b == null) {
			return false;
		}

		return switch (this) {
			case SMASH -> b.smash();
			case UNSMASH -> b.unsmash(); // 구현 추가
			case SWAP_VERTICALLY -> b.swap(true);
			case SWAP_HORIZONTALLY -> b.swap(false);
			case TURN_CW -> b.rotate(true);
			case TURN_CCW -> b.rotate(false);
			case UNDO -> false;
		};
	}
}
