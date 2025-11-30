package game;

/*
 * === Module Description ===

This file contains the Goal class hierarchy.
 */

import java.awt.Color;
import java.util.Random;

public abstract class Goal {
    public Color targetColor;

    public abstract int score(Block board);

    public abstract String goalDescription();

}
