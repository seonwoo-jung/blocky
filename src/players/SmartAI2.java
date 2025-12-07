package players;

import java.util.Random;

import game.Action;
import game.Block;
import game.Game;
import game.Goal;

public class SmartAI2 extends Player {

	private final Random random = new Random();

	public SmartAI2(int id, Game game, Goal goal) {
		super(id, game, goal);
	}

	@Override
	public Action makeMove() {

		Block board = game.getBoard();
		Action[] actions = Action.values();

		Action bestAction = null;
		int bestX = -1;
		int bestY = -1;
		int bestLevel = -1;
		int bestScore = -1;

		int boardSize = board.getRect().width;
		int maxLevel = board.getMaxDepth();

		for (int i = 0; i < 60; i++) {

			int level = random.nextInt(maxLevel + 1);
			int x = random.nextInt(boardSize);
			int y = random.nextInt(boardSize);

			Block realCandidate = board.getSelectedBlock(x, y, level);
			if (realCandidate == null) {
				continue;
			}

			for (Action action : actions) {

				Block boardCopy = board.copyBlock(board);

				Block targetOnCopy = boardCopy.getSelectedBlock(x, y, level);
				if (targetOnCopy == null) {
					continue;
				}

				action.runAction(targetOnCopy);
				boardCopy.updateBlockLocations();

				int score = goal.score(boardCopy);

				if (score > bestScore) {
					bestScore = score;
					bestAction = action;
					bestX = x;
					bestY = y;
					bestLevel = level;
				}
			}
		}

		if (bestAction != null && bestX >= 0) {
			Block target = board.getSelectedBlock(bestX, bestY, bestLevel);
			if (target != null) {
				game.activeBlock = target;
				return bestAction;
			}
		}

		return randomAction();
	}
}
