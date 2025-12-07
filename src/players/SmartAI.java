package players;

import java.util.Random;

import game.Action;
import game.Block;
import game.Game;
import game.Goal;

public class SmartAI extends Player {

	private static final int TRIALS = 12;
	private final Random rand = new Random();

	public SmartAI(int id, Game game, Goal goal) {
		super(id, game, goal);
	}

	@Override
	public void makeMove() {
		Block board = game.getBoard();

		Action bestAction = null;
		int bestScore = -1;

		// 실제 보드에서 적용할 좌표/레벨
		int bestX = -1;
		int bestY = -1;
		int bestLevel = -1;

		int boardSize = board.getRect().width;
		int maxLevel = board.getMaxDepth();

		for (int i = 0; i < TRIALS; i++) {
			Block boardCopy = board.copyBlock(board);

			int level = rand.nextInt(maxLevel + 1);
			int x = rand.nextInt(boardSize);
			int y = rand.nextInt(boardSize);

			Block targetOnCopy = boardCopy.getSelectedBlock(x, y, level);
			if (targetOnCopy == null) {
				continue;
			}

			Action action = randomAction();

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

		if (bestAction != null && bestX >= 0) {
			Block realTarget = board.getSelectedBlock(bestX, bestY, bestLevel);
			if (realTarget != null) {
				bestAction.runAction(realTarget);
				board.updateBlockLocations();
			}
		}
	}
}
