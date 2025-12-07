/*
Author: Mr. Meyer
Updated by: ChatGPT
Purpose: Refined UI to enhance Undo/Unsmash visibility
*/

package ui;

import static game.Action.*;
import static game.Game.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;

import game.Game;

public class BlockyMain {

	protected static JFrame window;
	protected static Game game;
	protected static JPanel controlPanel;
	protected static JTextPane status;
	protected static JLabel turnsTaken;

	protected static int max_depth;
	protected static BufferedImage image = null;

	private static final int BOARDSIZE = 768;

	public static void main(String[] args) {

		SwingUtilities.invokeLater(BlockyMain::initGraphics);

		try {
			File imgFile = new File("Images/blocky.png");
			if (imgFile.exists()) {
				image = ImageIO.read(imgFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		new StartScreen(BOARDSIZE, image);
	}

	public static void launchGame(int numHuman, int numSmartAI, int numSmartAI2,
		int numSimpleAI, int levels, int turns) {

		max_depth = levels;
		initControls();

		game = new Game(levels, numHuman, numSmartAI,
			numSmartAI2, numSimpleAI, turns);

		while (window == null) {
			try { Thread.sleep(200); } catch (InterruptedException ignored) {}
		}

		updatePlayers();
		window.add(game);
		window.pack();
		window.setVisible(true);
		game.startGame();
	}


	public static void initGraphics() {
		window = new JFrame("Blocky");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new FlowLayout(FlowLayout.LEFT));
		window.setLocation(100, 100);

		controlPanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(Color.BLACK);

				if (image != null) {
					g.drawImage(image, 0, 0, 300, 100, null);
				}

				g.setColor(Color.LIGHT_GRAY);
				g.drawString("Level:", 25, 350);
				g.drawString("Actions:", 25, 400);
			}
		};

		controlPanel.setPreferredSize(new Dimension(300, BOARDSIZE));
		controlPanel.setLayout(null);
		window.add(controlPanel);
	}

	public static void updatePlayers() {
		GameView.drawPlayers(
			game.players,
			game.board,
			game.getPlayerTurn(),
			controlPanel,
			new Point(25, 130)
		);
	}

	public static void updateTurns(int rounds, int maxRounds) {
		turnsTaken.setText("Turn " + rounds + " of " + maxRounds);
	}

	public static void initControls() {

		turnsTaken = new JLabel("Turn 0 of 20");
		turnsTaken.setForeground(MELON_MAMBO);
		turnsTaken.setBounds(25, 270, 150, 50);
		turnsTaken.setFont(new Font("Arial", Font.BOLD, 15));
		controlPanel.add(turnsTaken);

		// LEVEL BUTTONS
		JButton[] levels = new JButton[max_depth];
		for (int i = 0; i < max_depth; i++) {
			JButton b = new JButton("" + i);
			b.setBounds(120 + (i * 25), 330, 25, 25);
			controlPanel.add(b);
			levels[i] = b;
			b.addActionListener(e -> {
				for (JButton l : levels)
					l.setSelected(false);
				b.setSelected(true);
				game.setLevel(Integer.parseInt(e.getActionCommand()));
				game.selectClick();
			});
		}

		// ACTION BUTTONS
		createActionButton("Smash", 145, 385, () -> game.nextTurn(SMASH));
		createActionButton("Swap Vertical", 20, 415, () -> game.nextTurn(SWAP_VERTICALLY));
		createActionButton("Swap Horizontal", 145, 415, () -> game.nextTurn(SWAP_HORIZONTALLY));
		createActionButton("Rotate CW", 20, 445, () -> game.nextTurn(TURN_CW));
		createActionButton("Rotate CCW", 145, 445, () -> game.nextTurn(TURN_CCW));
		createActionButton("Undo", 20, 475, () -> game.nextTurn(UNDO));
		createActionButton("Unsmash", 145, 475, () -> game.nextTurn(UNSMASH));

		status = new JTextPane();
		status.setBounds(20, 510, 240, 120);
		status.setEditable(false);
		status.setFont(new Font("Arial", Font.PLAIN, 12));
		status.setForeground(Color.WHITE);
		status.setBackground(Color.BLACK);

		controlPanel.add(status);
		window.setVisible(true);
	}

	private static void createActionButton(String text, int x, int y, Runnable act) {
		JButton btn = new JButton(text);
		btn.setBounds(x, y, 130, 25);
		controlPanel.add(btn);
		btn.addActionListener(e -> {
			if (!text.equals("Undo") && !text.equals("Unsmash")) {
				if (game.activeBlock == null) {
					updateStatus("Select a block first!", false, MELON_MAMBO);
					return;
				}
			}

			act.run();
		});
	}

	public static void updateStatus(String message, boolean append, Color bg) {
		if (append) {
			try {
				status.getDocument().insertString(status.getDocument().getLength(),
					message + "\n", null);
			} catch (BadLocationException ignored) {
			}
		} else {
			status.setText(message + "\n");
		}
		status.setBackground(bg);
	}

	public static void enableActions(boolean enable) {
		for (Component c : controlPanel.getComponents()) {
			if (c instanceof JButton) {
				c.setEnabled(enable);
			}
		}
	}
}
