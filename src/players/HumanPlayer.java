package players;

import game.Game;
import game.Goal;

public class HumanPlayer extends Player {

	public HumanPlayer(int playerID, Game game, Goal goal) {
		super(playerID, game, goal);
	}

    @Override
    public void makeMove() {
        // Human moves are handled by UI events, so this might be empty or handle
        // specific turn logic
    }
}
