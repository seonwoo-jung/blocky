package game;

/*
 * === Data Structures and Algorithms ===
 * Todd Meyer based of the project by Diane Horton and David Liu
 * 
 * 
 * 
 * === Module Description ===
This file contains the Game class.  This class extends JPanel and handles the painting of the Blocks
portion of the game.  It also is in charge of handling player input (MouseListener),
player turns, and ending the game. 

 */

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import players.HumanPlayer;
import players.Player;
import players.SimpleAI;
import ui.BlockyMain;

public class Game extends JPanel implements MouseListener {
	// A game of Blocky.

	// === Public Attributes ===
	// board:
	// The Blocky board on which this game will be played.
	//
	// players:
	// The entities that are playing this game.

	// === Representation Invariants ===
	// - len(players) >= 1

	public Block board;
	public List<Player> players = new ArrayList<>();
	public GameState state;
	public Block activeBlock;

	private Point click;
	private int level;
	private static final int BOARDSIZE = 768;

	// Turn variables
	private int playerTurn = 0;
	private int turnsPlayed = 0;
	private int maxTurns = 0;

	// set up the colors
	public static Color WHITE = new Color(255, 255, 255);
	public static Color BLACK = new Color(0, 0, 0);
	public static Color PACIFIC_POINT = new Color(1, 128, 181);
	public static Color OLD_OLIVE = new Color(138, 151, 71);
	public static Color REAL_RED = new Color(199, 44, 58);
	public static Color MELON_MAMBO = new Color(234, 62, 112);
	public static Color DAFFODIL_DELIGHT = new Color(255, 211, 92);
	public static Color TEMPTING_TURQUOISE = new Color(75, 196, 213);
	public static Color[] COLOR_LIST = new Color[] {PACIFIC_POINT, REAL_RED, OLD_OLIVE, DAFFODIL_DELIGHT};
	public static String[] COLOR_NAMES = new String[] {"Pacific Point", "Real Red", "Old Olive", "Daffodil Delight"};

	public Game(int max_depth, int num_human, int random_players, int turns) {
		this.setPreferredSize(new Dimension(BOARDSIZE, BOARDSIZE));
		this.setBackground(Color.darkGray);
		this.addMouseListener(this);

		this.maxTurns = turns;
		state = GameState.start;

		board = new Block(new Point(0, 0), max_depth, null, BOARDSIZE);
		board.smash();

		initPlayers(num_human, random_players);
	}

	private void initPlayers(int numHuman, int numRandomAI) {
		int id = 1;

		// Human players
		for (int i = 0; i < numHuman; i++) {
			Color goalColor = COLOR_LIST[i % COLOR_LIST.length];
			Goal goal = randomGoal(goalColor);
			players.add(new HumanPlayer(id++, this, goal));
		}

		// Random AIs
		for (int i = 0; i < numRandomAI; i++) {
			Color goalColor = COLOR_LIST[(id - 1) % COLOR_LIST.length];
			Goal goal = randomGoal(goalColor);
			players.add(new SimpleAI(id++, this, goal));
		}
	}

	private Goal randomGoal(Color color) {
		return Math.random() < 0.5 ? new BlobGoal(color) : new PerimeterGoal(color);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		board.draw(g2);

		// 선택된 블록 강조 (선택한 경우)
		if (activeBlock != null) {
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(4));
			g2.draw(activeBlock.getRect());
		}
	}

	public void nextTurn(String action) {
		if (activeBlock == null)
			return;

		switch (action) {
			case "rotate CW" -> activeBlock.rotate(true);
			case "rotate CCW" -> activeBlock.rotate(false);
			case "swap H" -> activeBlock.swap(false);
			case "swap V" -> activeBlock.swap(true);
			case "smash" -> activeBlock.smash();
		}

		board.updateBlockLocations();
		repaint();

		Player current = players.get(playerTurn);
		BlockyMain.updateStatus(
			current.getPlayerName() + " score: " + current.getScore(),
			false, Color.WHITE
		);

		endOrContinueTurn();
	}

	private void endOrContinueTurn() {
		turnsPlayed++;
		if (turnsPlayed >= maxTurns) {
			endGame();
			return;
		}

		playerTurn = (playerTurn + 1) % players.size();
		Player next = players.get(playerTurn);

		BlockyMain.updateStatus("Turn: " + next.getPlayerName(),
			false, Color.WHITE);

		repaint();
	}

	public void selectClick() {
		if (click == null) {
			return;
		}
		activeBlock = this.board.getSelectedBlock(click.x, click.y, level);
		if (activeBlock == null) {
			BlockyMain.updateStatus("couldn't activate a block at that that location and level(" + level + ")",
				true, MELON_MAMBO);
			System.out.println("couldn't activate a block at that that location and level");
		}
		repaint();
	}

	public void endGame() {
		int best = Integer.MIN_VALUE;
		Player winner = null;

		for (Player p : players) {
			int score = p.getScore();
			if (score > best) {
				best = score;
				winner = p;
			}
		}

		BlockyMain.updateStatus(
			"Winner: " + winner.getPlayerName() + "  Score: " + best,
			false, TEMPTING_TURQUOISE
		);

		state = GameState.completed;
		repaint();
	}

	public void startGame() {
		state = GameState.playing;
		turnsPlayed = 0;
		playerTurn = 0;

		Player current = players.get(0);
		BlockyMain.updateStatus(
			"Game Start! First: " + current.getPlayerName(),
			false, Color.WHITE
		);

		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		click = new Point(e.getX(), e.getY());
		selectClick();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	public int getPlayerTurn() {
		return playerTurn;
	}

	public Block getBoard() {
		return board;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMaxTurns() {
		return maxTurns;
	}
}
