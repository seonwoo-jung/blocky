package players;

import java.util.Random;

import game.Action;
import game.Block;
import game.Game;
import game.Goal;

/**
 * SmartAI2:
 * 여러 랜덤 위치/레벨에 대해
 * 모든 액션(Action.values())을 시뮬레이션해서
 * 가장 높은 점수를 내는 수를 실제 보드에 1번 적용한다.
 */
public class SmartAI2 extends Player {

	private final Random random = new Random();

	public SmartAI2(int id, Game game, Goal goal) {
		super(id, game, goal);
	}

	@Override
	public void makeMove() {

		Block board = game.getBoard();
		Action[] actions = Action.values();

		Action bestAction = null;
		int bestX = -1;
		int bestY = -1;
		int bestLevel = -1;
		int bestScore = -1;

		int boardSize = board.getRect().width; // getRect()는 이미 존재
		int maxLevel = board.getMaxDepth();   // 네 Block에 있는 메서드 이름

		// 랜덤한 블록 위치를 60번 뽑아서 조사
		for (int i = 0; i < 60; i++) {

			int level = random.nextInt(maxLevel + 1);
			int x = random.nextInt(boardSize);
			int y = random.nextInt(boardSize);

			// 실제 보드에서 이 좌표/레벨에 블록이 있는지 먼저 확인
			Block realCandidate = board.getSelectedBlock(x, y, level);
			if (realCandidate == null) {
				continue;
			}

			// 이 블록에 대해 가능한 모든 액션을 시뮬레이션
			for (Action action : actions) {

				// 1) 보드 전체 복사 (기존 copyBlock 사용)
				Block boardCopy = board.copyBlock(board);

				// 2) 복사본에서 동일 좌표/레벨의 블록 찾기
				Block targetOnCopy = boardCopy.getSelectedBlock(x, y, level);
				if (targetOnCopy == null) {
					continue;
				}

				// 3) 복사본에서만 액션 실행
				action.runAction(targetOnCopy);
				boardCopy.updateBlockLocations();

				// 4) 복사본 기준 점수 계산
				int score = goal.score(boardCopy);

				// 5) 최고 점수 갱신
				if (score > bestScore) {
					bestScore = score;
					bestAction = action;
					bestX = x;
					bestY = y;
					bestLevel = level;
				}
			}
		}

		// 실제 보드에 최종 한 수만 적용
		if (bestAction != null && bestX >= 0) {
			Block target = board.getSelectedBlock(bestX, bestY, bestLevel);
			if (target != null) {
				bestAction.runAction(target);
				board.updateBlockLocations();
			}
		}
	}
}
