package game;

import java.awt.Color;

public class BlobGoal extends Goal {

	public BlobGoal(Color targetColor) {
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

		boolean[][] visited = new boolean[n][n];
		int maxSize = 0;

		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				int size = blobSizeFrom(grid, visited, x, y, getTargetColor());
				if (size > maxSize) {
					maxSize = size;
				}
			}
		}

		return maxSize;
	}

	private int blobSizeFrom(Color[][] grid,
		boolean[][] visited,
		int x, int y,
		Color targetColor) {

		int n = grid.length;

		if (x < 0 || x >= n || y < 0 || y >= n) {
			return 0;
		}

		if (visited[x][y]) {
			return 0;
		}

		if (!grid[x][y].equals(targetColor)) {
			return 0;
		}

		visited[x][y] = true;

		int size = 1;
		size += blobSizeFrom(grid, visited, x + 1, y, targetColor);
		size += blobSizeFrom(grid, visited, x - 1, y, targetColor);
		size += blobSizeFrom(grid, visited, x, y + 1, targetColor);
		size += blobSizeFrom(grid, visited, x, y - 1, targetColor);

		return size;
	}

	@Override
	public String goalDescription() {
		return "Blob goal: create the largest connected blob of the target colour.";
	}

	@Override
	public String getGoalName() {
		return "Blob Goal";
	}
}
