/*
 * === Module Description ===
 * 
 * This file contains the GameView class, which handles the creation of Swing UI components
 * for the game, separating the view logic from the game model.
 */

package ui;

import game.*;
import players.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameView {

    private static JPanel playerPanel;

    /**
     * Draws the player information panel.
     * 
     * @param players    The list of players in the game.
     * @param board      The current game board (for scoring).
     * @param playerTurn The index of the current player's turn (0-based).
     * @param controls   The parent panel to add the player info to.
     * @param position   The position to place the panel.
     */
    public static void drawPlayers(List<Player> players, Block board, int playerTurn, JPanel controls, Point position) {
        // Remove the old panel if it exists to prevent stacking
        if (playerPanel != null) {
            controls.remove(playerPanel);
        }

        // make a panel for the players information
        playerPanel = new JPanel(new GridBagLayout());
        playerPanel.setBounds(position.x, position.y, 250, 150);
        playerPanel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();

        // --- Header Row ---
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(0, 5, 5, 5); // Add some bottom padding for header

        // Player Header
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        JLabel playerTitle = new JLabel("<html><u><b>Player</b></u></html>");
        playerTitle.setForeground(Color.gray);
        playerPanel.add(playerTitle, gbc);

        // Goal Header
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.0;
        JLabel goalLabel = new JLabel("<html><u><b>Goal</b></u></html>");
        goalLabel.setForeground(Color.gray);
        playerPanel.add(goalLabel, gbc);

        // Score Header
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        JLabel scoreLabel = new JLabel("<html><u><b>Score</b></u></html>");
        scoreLabel.setForeground(Color.gray);
        playerPanel.add(scoreLabel, gbc);

        // Color Header
        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        JLabel colorLabel = new JLabel("<html><u><b>Color</b></u></html>");
        colorLabel.setForeground(Color.gray);
        playerPanel.add(colorLabel, gbc);

        // --- Player Rows ---
        int row = 1;
        for (Player p : players) {
            gbc.gridy = row;
            gbc.insets = new java.awt.Insets(2, 5, 2, 5); // Smaller vertical padding for rows

            boolean isTurn = p.getPlayerID() == playerTurn + 1;
            Color rowColor = isTurn ? Game.TEMPTING_TURQUOISE : Color.BLACK;
            Color textColor = isTurn ? Game.PACIFIC_POINT : Color.GRAY;

            if (isTurn)
                textColor = Color.GRAY;

            // Player Name
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL; // Fill to show background
            JLabel pName = new JLabel(p.getClass().getSimpleName() + " " + p.getPlayerID());
            pName.setForeground(textColor);
            pName.setOpaque(true);
            pName.setBackground(rowColor);
            playerPanel.add(pName, gbc);

            // Goal
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            String goalDesc = (p.getPlayerGoal() != null) ? p.getPlayerGoal().goalDescription() : "No Goal";
            JLabel pGoal = new JLabel(goalDesc);
            pGoal.setForeground(textColor);
            pGoal.setOpaque(true);
            pGoal.setBackground(rowColor);
            pGoal.setHorizontalAlignment(JLabel.CENTER);
            playerPanel.add(pGoal, gbc);

            // Score
            gbc.gridx = 2;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            int score = (p.getPlayerGoal() != null && board != null) ? p.getPlayerGoal().score(board) : 0;
            JLabel pScore = new JLabel("" + score);
            pScore.setForeground(textColor);
            pScore.setOpaque(true);
            pScore.setBackground(rowColor);
            pScore.setHorizontalAlignment(JLabel.RIGHT);
            playerPanel.add(pScore, gbc);

            // Color Swatch
            gbc.gridx = 3;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.NONE; // Don't stretch the color box
            JPanel colorSwatch = new JPanel();
            if (p.getPlayerGoal() != null) {
                colorSwatch.setBackground(p.getPlayerGoal().targetColor);
            } else {
                colorSwatch.setBackground(Color.GRAY);
            }
            colorSwatch.setPreferredSize(new Dimension(25, 25));
            colorSwatch.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JPanel swatchContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            swatchContainer.setBackground(rowColor);
            swatchContainer.add(colorSwatch);

            playerPanel.add(swatchContainer, gbc);

            row++;
        }

        controls.add(playerPanel);
        controls.revalidate(); // Ensure layout updates
        controls.repaint();
    }
}
