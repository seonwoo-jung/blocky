package players;

import game.*;

public class SimpleAI extends Player {

	public SimpleAI(int id, Game game, Goal goal) {
		super(id, game, goal);
	}

	@Override
	public void makeMove() {

		Block block = null;
		for (int attempts = 0; attempts < 30; attempts++) {
			block = randomBlock();
			if (block != null) break;
		}

		if (block == null) return;

		Action action = randomAction();
		action.runAction(block);

		game.getBoard().updateBlockLocations();
	}
}
