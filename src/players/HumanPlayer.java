package players;

import game.Action;
import game.Game;
import game.Goal;

public class HumanPlayer extends Player {

	public HumanPlayer(int playerID, Game game, Goal goal) {
		super(playerID, game, goal);
	}

    @Override
    public Action makeMove() {
		return null;
    }
}
