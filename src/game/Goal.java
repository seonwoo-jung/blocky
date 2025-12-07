package game;

/*
 * === Module Description ===
 *
 * This file contains the Goal class hierarchy.
 */

import java.awt.*;

public abstract class Goal {
	private Color targetColor;

	public Goal(Color targetColor) {
		this.targetColor = targetColor;
	}

	public Color getTargetColor() {
		return targetColor;
	}

	public void setTargetColor(Color targetColor) {
		this.targetColor = targetColor;
	}

	public abstract int score(Block board);

	public abstract String goalDescription();

	public abstract String getGoalName();
}
