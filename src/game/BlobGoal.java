package game;

import java.awt.Color;

/**
 * Blob goal:
 * 타깃 색으로 이루어진 가장 큰 연결된 blob 의 크기를 최대화하는 목표.
 *
 * blob 정의:
 * - 유닛 셀 단위에서
 * - 상하좌우(4-connectivity) 로 인접한 같은 색 셀들의 최대 집합.
 */
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

		// 모든 셀을 시작점으로 시도
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

	/**
	 * (x, y) 위치에서 시작하는 blob 의 크기를 DFS 로 계산.
	 * - grid[x][y] 가 targetColor 가 아니면 0.
	 * - 이미 방문한 셀이면 0.
	 */
	private int blobSizeFrom(Color[][] grid,
		boolean[][] visited,
		int x, int y,
		Color targetColor) {

		int n = grid.length;

		// 범위 밖
		if (x < 0 || x >= n || y < 0 || y >= n) {
			return 0;
		}

		// 이미 방문
		if (visited[x][y]) {
			return 0;
		}

		// 색 다르면 blob 에 포함 안 됨
		if (!grid[x][y].equals(targetColor)) {
			return 0;
		}

		// 이 셀을 blob 에 포함
		visited[x][y] = true;

		int size = 1;
		// 상하좌우 4방향 탐색
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
}
