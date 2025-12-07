
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

	public Player(int playerID, Game game, Goal goal) {
		this.playerID = playerID;
		this.game = game;
		this.goal = goal;
	}

	public int getScore() {
		Block board = game.getBoard();
		return goal.score(board);
	}

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

	protected int randomLevel() {
		return (int)(Math.random() * (game.getBoard().getMaxDepth() + 1));
	}

	protected Action randomAction() {
		Action[] list = Action.values();
		return list[(int)(Math.random() * list.length)];
	}

	protected Block randomBlock() {
		int x = (int)(Math.random() * game.getBoard().getSize());
		int y = (int)(Math.random() * game.getBoard().getSize());
		int level = randomLevel();
		return game.getBoard().getSelectedBlock(x, y, level);
	}
}
