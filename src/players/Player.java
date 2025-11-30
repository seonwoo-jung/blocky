
package players;

import game.*;
/* === Module Description ===

This file contains the player class hierarchy.
 */

public abstract class Player {

    protected int playerID; // player 1, 2, 3,or 4
    protected Goal playerGoal;
    protected Game game;

    /**
     * Constructor for player
     * 
     * @param playerID - ID for player, usually 1-4
     * @param game     - an instance of the game the player is in
     */
    public Player(int playerID, Game game) {
        this.playerID = playerID;
        this.game = game;
    }

    public Goal getPlayerGoal() {
        return playerGoal;
    }

    public int getPlayerID() {
        return playerID;
    }

    public abstract void makeMove();

}
