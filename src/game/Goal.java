package game;

/*
 * === Module Description ===
 *
 * This file contains the Goal class hierarchy.
 */

import java.awt.Color;

/**
 * A player goal in the game of Blocky.
 *
 * 각 Goal 은 특정 targetColor 에 대해 점수를 계산한다.
 */
public abstract class Goal {

	// 이 Goal 이 현재 대상으로 삼는 색
	private Color targetColor;

	public Goal(Color targetColor) {
		this.targetColor = targetColor;
	}

	/** 현재 타깃 색 반환 */
	public Color getTargetColor() {
		return targetColor;
	}

	/** 타깃 색 변경 */
	public void setTargetColor(Color targetColor) {
		this.targetColor = targetColor;
	}

	/**
	 * 현재 설정된 targetColor 를 기준으로 board 의 점수를 계산.
	 */
	public abstract int score(Block board);

	/**
	 * 편의 메서드: 외부에서 색을 넘겨주어 그 색으로 점수를 계산.
	 * Game 이 사용하는 형태: goal.score(board, player.getTargetColor()).
	 */
	public int score(Block board, Color targetColor) {
		setTargetColor(targetColor);
		return score(board);
	}

	/**
	 * 이 Goal 에 대한 설명 문자열.
	 */
	public abstract String goalDescription();
}
