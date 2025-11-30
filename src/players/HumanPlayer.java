package players;

import game.Game;

public class HumanPlayer extends Player {

    public HumanPlayer(int playerID, Game game) {
        super(playerID, game);
    }

    @Override
    public void makeMove() {
        // Human moves are handled by UI events, so this might be empty or handle
        // specific turn logic
    }
}
