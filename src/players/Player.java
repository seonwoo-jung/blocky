
package players;

import game.Action;
import game.Block;
import game.Game;
import game.Goal;
/* === Module Description ===

This file contains the player class hierarchy.
 */

public abstract class Player {

	protected int playerID; // player 1, 2, 3,or 4
	protected Goal goal;
	protected Game game;

	/**
	 * Constructor for player
	 *
	 * @param playerID - ID for player, usually 1-4
	 * @param game     - an instance of the game the player is in
	 */
	public Player(int playerID, Game game, Goal goal) {
		this.playerID = playerID;
		this.game = game;
		this.goal = goal;
	}

	public int getScore() {
		Block board = game.getBoard();
		return goal.score(board);
	}

	/**
	 * 현재 Blocky 보드 가져오기
	 */
	protected Block getBoard() {
		return game.getBoard();
	}

	public String getPlayerName() {
		return "Player " + playerID;
	}

	public Goal getGoal() {
		return goal;
	}

	public int getPlayerID() {
		return playerID;
	}

	public abstract void makeMove();

	/** 임의의 블록 단계를 0~maxDepth 범위에서 반환 */
	protected int randomLevel() {
		return (int)(Math.random() * (game.getBoard().getMaxDepth() + 1));
	}

	/** 임의의 액션 선택 */
	protected Action randomAction() {
		Action[] list = Action.values();
		return list[(int)(Math.random() * list.length)];
	}

	/** 임의 블록 선택 */
	protected Block randomBlock() {
		int x = (int)(Math.random() * game.getBoard().getSize());
		int y = (int)(Math.random() * game.getBoard().getSize());
		int level = randomLevel();
		return game.getBoard().getSelectedBlock(x, y, level);
	}
}
