package game;

/**
 * Enumeration of the five basic actions that can be applied to a Block.
 */
public enum Action {
	SMASH,
	SWAP_VERTICALLY,
	SWAP_HORIZONTALLY,
	TURN_CW,
	TURN_CCW;

	/**
	 * 이 Action 을 주어진 Block 에 적용한다.
	 *
	 * @param b 변경할 Block
	 * @return 액션이 정상적으로 수행되었으면 true, 아니면 false
	 */
	public boolean runAction(Block b) {
		if (b == null) {
			return false;
		}

		return switch (this) {
			case SMASH -> b.smash();
			case SWAP_VERTICALLY -> b.swap(true);   // 세로 축 기준 swap
			case SWAP_HORIZONTALLY -> b.swap(false); // 가로 축 기준 swap
			case TURN_CW -> b.rotate(true);         // 시계 방향
			case TURN_CCW -> b.rotate(false);       // 반시계 방향
		};
	}

	/**
	 * 이 Action 의 inverse 를 주어진 Block 에 적용한다.
	 *
	 * - SMASH 는 정보가 소실되므로 되돌릴 수 없다고 가정하고 false 반환.
	 * - swap 은 swap 을 한 번 더 하면 원상 복구되므로 똑같이 적용.
	 * - 회전은 반대 방향으로 한 번 더 회전.
	 */
	public boolean runInverse(Block b) {
		if (b == null) {
			return false;
		}

		return switch (this) {
			case SMASH -> false;  // unsmash 기능은 정의되어 있지 않음
			case SWAP_VERTICALLY -> b.swap(true);
			case SWAP_HORIZONTALLY -> b.swap(false);
			case TURN_CW -> b.rotate(false);
			case TURN_CCW -> b.rotate(true);
		};
	}
}
