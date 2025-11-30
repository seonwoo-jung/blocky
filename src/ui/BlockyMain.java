/*
Author: Mr. Meyer 
Last Updated: 12/1/2024

starter code for a DSA project.
This class contains the Main method for the game.  

Additionally, this code sets up the Window, and core controls for the game including
the control panel on the left side of the game. 
*/
package ui;

import game.*;
import players.*;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.Font;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class BlockyMain {

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    protected static JFrame window;
    protected static Game game;
    protected static JPanel controlPanel;
    protected static JTextPane status;
    protected static JLabel turnsTaken;

    // protected static controls controlPanel;
    protected static int max_depth;
    protected static BufferedImage image = null;;

    private static final int BOARDSIZE = 768;
    private static boolean isFading = false;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initGraphics();
            }
        });

        try {
            File imgFile = new File("Images/blocky.png");
            if (imgFile.exists()) {
                image = ImageIO.read(imgFile);
            } else {
                System.out.println("Warning: Could not find Images/blocky.png");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // start screen to get the parameters:
        new StartScreen(BOARDSIZE, image);
    }

    public static void launchGame(int numHuman, int AIplayers, int levels, int turns) {
        max_depth = levels;
        initControls();
        game = new Game(levels, numHuman, AIplayers, turns);
        while (window == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // controlPanel.initControls();
        updatePlayers();
        window.add(game);
        window.pack();
        window.setVisible(true);
        game.startGame();
    }

    public static void initGraphics() {
        // make the window for the game:
        window = new JFrame("Blocky");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new FlowLayout(FlowLayout.LEFT));
        window.setLocation(100, 100);

        // create a left panel that will have controls, and messages that show the game
        // flow
        controlPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.BLACK);

                g.drawImage(image, 0, 0, 300, 100, null);

                g.setColor(Color.gray);
                g.drawString("Level:", 25, 350);
                g.drawString("Actions:", 25, 400);
            }
        };
        controlPanel.setPreferredSize(new Dimension(300, BOARDSIZE));
        controlPanel.setLayout(null);
        window.add(controlPanel);
    }

    public static void updatePlayers() {
        // game.drawPlayers(new Point(25, 130), controlPanel);
        GameView.drawPlayers(game.players, game.board, game.getPlayerTurn(), controlPanel, new Point(25, 130));
    }

    public static void updateTurns(int rounds, int maxRounds) {
        turnsTaken.setText("Turn " + rounds + " of " + maxRounds);
    }

    /**
     * This method initializes the control panel for the game on the left side of
     * the screen.
     * 
     * Most of the code below sets up the buttons for the actions that the player
     * can take.
     * It also creates the TextArea that will be used to display messages to the
     * player.
     * 
     */
    public static void initControls() {

        // set up turn label:
        turnsTaken = new JLabel("Turn 0 of 20");
        turnsTaken.setForeground(Game.MELON_MAMBO);
        turnsTaken.setBounds(25, 270, 150, 50);
        turnsTaken.setFont(new Font("Arial", Font.BOLD, 15));
        controlPanel.add(turnsTaken);

        // ==== LEVEL BUTTONS:
        JButton[] levels = new JButton[max_depth];
        int iter = 0;
        for (int i = 0; i < levels.length; i++) {
            levels[i] = new JButton();
            levels[i].setText("" + iter);
            levels[i].setBounds(120 + (iter * 25), 330, 25, 25);
            controlPanel.add(levels[i]);
            iter++;

            levels[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    for (JButton l : levels) {
                        if (l.getText().equals(e.getActionCommand())) {
                            l.setSelected(true);
                            game.setLevel(Integer.parseInt(e.getActionCommand()));
                        } else
                            l.setSelected(false);
                    }
                    game.selectClick();
                }
            });
        }

        // ===== ACTION BUTTONS:

        // SMASH
        createActionButton("Smash", 145, 385, e -> {
            if (game.activeBlock != null) {
                if (game.activeBlock.smash()) {
                    new Thread(() -> game.nextTurn("SMASHED!")).start();
                    System.out.println("SMASHED!  next turn");
                } else {
                    updateStatus("That block can't be Smashed!", false, Game.REAL_RED);
                    System.out.println("can't be smashed");
                }
            } else {
                updateStatus("please select a block", false, Game.MELON_MAMBO);
            }
        });

        // SWAP VERTICAL
        createActionButton("Swap Vertical", 20, 415, e -> {
            if (game.activeBlock != null) {
                if (game.activeBlock.swap(true)) {
                    new Thread(() -> game.nextTurn("Vertically Swapped!")).start();
                    System.out.println("Vertically Swapped!  next turn");
                } else {
                    updateStatus("That block can't be swapped", false, Game.REAL_RED);
                    System.out.println("can't be swapped");
                }
            } else {
                updateStatus("please select a block", false, Game.MELON_MAMBO);
            }
        });

        // SWAP HORIZONTAL
        createActionButton("Swap Horizontal", 145, 415, e -> {
            if (game.activeBlock != null) {
                if (game.activeBlock.swap(false)) {
                    new Thread(() -> game.nextTurn("Horizontally Swapped! ")).start();
                    System.out.println("Horizontally Swapped!  next turn");
                } else {
                    updateStatus("That block can't be swapped", false, Game.REAL_RED);
                    System.out.println("can't be swapped");
                }
            } else {
                updateStatus("please select a block", false, Game.MELON_MAMBO);
            }
        });

        // ROTATE CW
        createActionButton("Rotate CW", 20, 445, e -> {
            if (game.activeBlock != null) {
                if (game.activeBlock.rotate(true)) {
                    new Thread(() -> game.nextTurn("Rotated Clockwise!")).start();
                    System.out.println("Rotated Clockwise!  next turn");
                } else {
                    updateStatus("can't be rotated", true, Game.MELON_MAMBO);
                    System.out.println("can't be rotated");
                }
            } else {
                updateStatus("please select a block", false, Game.MELON_MAMBO);
            }
        });

        // ROTATE CCW
        createActionButton("Rotate CCW", 145, 445, e -> {
            if (game.activeBlock != null) {
                if (game.activeBlock.rotate(false)) {
                    new Thread(() -> game.nextTurn("Rotated Counterclockwise!")).start();
                    System.out.println("Rotated Counterclockwise!  next turn");
                } else {
                    updateStatus("That block can't be rotated", false, Game.REAL_RED);
                    System.out.println("can't be rotated");
                }
            } else {
                updateStatus("please select a block", false, Game.MELON_MAMBO);
            }
        });

        // Add textArea for messages
        status = new JTextPane();
        status.setBounds(20, 475, 255, 250);
        status.setEditable(false);
        controlPanel.add(status);

        window.setVisible(true);
    }

    private static void createActionButton(String text, int x, int y, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 130, 25);
        controlPanel.add(btn);
        btn.addActionListener(action);
    }

    /**
     * this updates the text box at the bottom. It is a complex StyledDocument as
     * that
     * offers more styling options. this all went a little overboard.
     */
    public static void updateStatus(String message, boolean append, Color rgb) {
        status.setForeground(Color.white);
        status.setEditable(false);
        StyledDocument doc = status.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        if (append) {
            try {
                doc.insertString(doc.getLength(), message + "\n", center);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        } else {
            status.setText(message + "\n");
        }

        if (isFading)
            return; // Prevent starting a new fade if one is already running

        final int[] opacity = { 0 };
        isFading = true;
        executorService.submit(() -> {
            try {
                while (opacity[0] < 255) {
                    status.setBackground(new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), opacity[0]));
                    opacity[0] += 10;
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                isFading = false; // Reset the flag when the fade effect is done
            }
        });
    }
}
