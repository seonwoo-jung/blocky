package game;

import java.awt.*;

public class DiagonalGoal extends Goal {
	public DiagonalGoal(Color targetColor) {
		super(targetColor);
	}

	@Override
	public int score(Block board) {
		Color[][] g = board.flatten();
		int n = g.length;
		int score = 0;
		Color t = getTargetColor();

		for (int i = 0; i < n; i++) {
			if (g[i][i].equals(t))
				score++; // Main diagonal
			if (g[i][n - i - 1].equals(t))
				score++; // Anti-diagonal
		}
		return score;
	}

	@Override
	public String goalDescription() {
		return "Diagonal goal: Maximize target colour on both diagonals.";
	}

	@Override
	public String getGoalName() {
		return "Diagonal Goal";
	}

}
