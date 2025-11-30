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
import players.*;
import ui.BlockyMain;
import ui.GameView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

enum GameState {
    start,
    playing,
    completed
}

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
    public List<Player> players;
    public GameState state;
    public Block activeBlock;

    public int getPlayerTurn() {
        return playerTurn;
    }

    // operational variables
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
    public static Color[] COLOR_LIST = new Color[] { PACIFIC_POINT, REAL_RED, OLD_OLIVE, DAFFODIL_DELIGHT };
    public static String[] COLOR_NAMES = new String[] { "Pacific Point", "Real Red", "Old Olive", "Daffodil Delight" };

    public Game(int max_depth, int num_human, int random_players, int turns) {
        this.setPreferredSize(new Dimension(BOARDSIZE, BOARDSIZE));
        this.setBackground(Color.darkGray);
        this.addMouseListener(this);

        state = GameState.start;
        board = new Block(new Point(0, 0), max_depth, null, BOARDSIZE);
        board.smash();

        // TODO:Initialize players

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gd = (Graphics2D) g;
        // TODO: implement paintComponent
    }

    public void nextTurn(String action) {
        // TODO: implement nextTurn

    }

    public void selectClick() {
        if (click != null) {
            activeBlock = this.board.getSelectedBlock(click.x, click.y, level);
            if (activeBlock == null) {
                BlockyMain.updateStatus("couldn't activate a block at that that location and level(" + level + ")",
                        true, MELON_MAMBO);
                System.out.println("couldn't activate a block at that that location and level");
            }
            repaint();
        }
    }

    public void startGame() {
        // TODO: implement startGame
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        click = new Point(e.getX(), e.getY());
        selectClick();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
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
