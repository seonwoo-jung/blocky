package players;

import game.*;

public class SimpleAI extends Player {

	public SimpleAI(int id, Game game, Goal goal) {
		super(id, game, goal);
	}

	@Override
	public Action makeMove() {

		Block block = null;
		for (int attempts = 0; attempts < 30; attempts++) {
			block = randomBlock();
			if (block != null) break;
		}

		if (block == null) return null;

		Action action = randomAction();

		game.activeBlock = block;

		return action;
	}
}
