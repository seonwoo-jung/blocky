package game;

import java.awt.Color;

/**
 * Perimeter goal:
 * 보드의 바깥 둘레(perimeter)에 있는 타깃 색 유닛 셀의 수를 최대화하는 목표.
 *
 * 규칙:
 * - 테두리에 있는 타깃 색 셀은 1점
 * - 네 모서리 셀은 1점이 추가로 더해져서 총 2점
 */
public class PerimeterGoal extends Goal {

	public PerimeterGoal(Color targetColor) {
		super(targetColor);
	}

	@Override
	public int score(Block board) {
		if (board == null || getTargetColor() == null) {
			return 0;
		}

		Color[][] grid = board.flatten();
		int n = grid.length;
		if (n == 0) {
			return 0;
		}

		Color targetColor = getTargetColor();
		int last = n - 1;
		int score = 0;

		// 위/아래 행 (top & bottom edges)
		for (int x = 0; x < n; x++) {
			if (grid[x][0].equals(targetColor)) {
				score++;
			}
			if (grid[x][last].equals(targetColor)) {
				score++;
			}
		}

		// 좌/우 열 (left & right edges), 모서리는 제외
		for (int y = 1; y < last; y++) {
			if (grid[0][y].equals(targetColor)) {
				score++;
			}
			if (grid[last][y].equals(targetColor)) {
				score++;
			}
		}

		// 모서리 4개는 한 번 더 카운트 (corner premium)
		if (grid[0][0].equals(targetColor)) {
			score++;
		}
		if (grid[0][last].equals(targetColor)) {
			score++;
		}
		if (grid[last][0].equals(targetColor)) {
			score++;
		}
		if (grid[last][last].equals(targetColor)) {
			score++;
		}

		return score;
	}

	@Override
	public String goalDescription() {
		return "Perimeter goal: maximize the target colour on the perimeter (corners count twice).";
	}
}
