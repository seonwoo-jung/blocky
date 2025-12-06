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
		int maxLevel = board.getMaxDepth();   // Block에 getMaxDepth() 있다고 가정 (없다면 getMax_depth())

		for (int i = 0; i < TRIALS; i++) {
			// 1. 보드 전체 복사
			Block boardCopy = board.copyBlock(board);

			// 2. 복사본에서 랜덤 블록 하나 뽑기 (좌표 + 레벨 랜덤)
			int level = rand.nextInt(maxLevel + 1);
			int x = rand.nextInt(boardSize);
			int y = rand.nextInt(boardSize);

			Block targetOnCopy = boardCopy.getSelectedBlock(x, y, level);
			if (targetOnCopy == null) {
				continue;
			}

			// 3. 랜덤 액션 선택
			Action action = randomAction();

			// 4. 복사본에만 액션 적용해서 시뮬레이션
			action.runAction(targetOnCopy);
			boardCopy.updateBlockLocations();

			// 5. 점수 계산 (복사본 기준)
			int score = goal.score(boardCopy);

			// 6. 최고 점수 갱신
			if (score > bestScore) {
				bestScore = score;
				bestAction = action;
				bestX = x;
				bestY = y;
				bestLevel = level;
			}
		}

		// 7. 실제 보드에 최고 액션 한 번만 반영
		if (bestAction != null && bestX >= 0) {
			Block realTarget = board.getSelectedBlock(bestX, bestY, bestLevel);
			if (realTarget != null) {
				bestAction.runAction(realTarget);
				board.updateBlockLocations();
			}
		}
	}
}
