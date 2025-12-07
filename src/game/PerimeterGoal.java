package game;

import java.awt.Color;

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

		for (int x = 0; x < n; x++) {
			if (grid[x][0].equals(targetColor)) {
				score++;
			}
			if (grid[x][last].equals(targetColor)) {
				score++;
			}
		}

		for (int y = 1; y < last; y++) {
			if (grid[0][y].equals(targetColor)) {
				score++;
			}
			if (grid[last][y].equals(targetColor)) {
				score++;
			}
		}

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

	@Override
	public String getGoalName() {
		return "Perimeter Goal";
	}
}
