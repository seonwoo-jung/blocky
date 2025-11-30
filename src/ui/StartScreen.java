/**
 *  ============== Start Screen ================
 *
 *  This module will show a start screen allowing the user to choose the options
 * for the play of the game.  
 *
 *  
 */
package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class StartScreen {

    public StartScreen(int boardsize, BufferedImage image) {
        JFrame ss = new JFrame("Blocky");
        ss.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ss.setLocation(100, 100);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel p = new JPanel(gbl);

        JPanel titlePanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Graphics2D g2 = (Graphics2D)g;
                if (image != null)
                    g.drawImage(image, 0, 0, 425, 135, null);
                else
                    g.drawString("Blocky", 0, 0);
            }
        };
        titlePanel.setPreferredSize(new Dimension(425, 135));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10;
        gbc.gridwidth = 4;
        p.add(titlePanel, gbc);

        // Levels
        JLabel levelLabel = new JLabel("Levels");
        levelLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        p.add(levelLabel, gbc);

        JLabel playerLabel = new JLabel("Human Players");
        playerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        p.add(playerLabel, gbc);

        JLabel aiLabel = new JLabel("AI players");
        aiLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        p.add(aiLabel, gbc);

        JLabel turnsLabel = new JLabel("Turns");
        turnsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        p.add(turnsLabel, gbc);

        SpinnerNumberModel levelModel = new SpinnerNumberModel(3, 2, 6, 1);
        JSpinner levels = new JSpinner(levelModel);
        levels.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 2;
        gbc.gridy = 2;
        p.add(levels, gbc);

        // human & AI players
        SpinnerNumberModel plModel = new SpinnerNumberModel(2, 0, 4, 1);
        SpinnerNumberModel aiModel = new SpinnerNumberModel(0, 0, 2, 1);
        JSpinner players = new JSpinner(plModel);
        JSpinner bots = new JSpinner(aiModel);
        players.setFont(new Font("Arial", Font.PLAIN, 20));
        bots.setFont(new Font("Arial", Font.PLAIN, 20));
        players.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner spinner = (JSpinner) e.getSource();
                int ai = (int) players.getValue();
                aiModel.setMaximum(4 - ai);
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 3;
        p.add(players, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        p.add(bots, gbc);

        SpinnerNumberModel turnsModel = new SpinnerNumberModel(20, 8, 60, 1);
        JSpinner turns = new JSpinner(turnsModel);
        gbc.gridx = 2;
        gbc.gridy = 5;
        p.add(turns, gbc);

        //
        JButton start = new JButton("Start");
        start.setForeground(Color.BLUE);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> BlockyMain.launchGame((int) players.getValue(), (int) bots.getValue(),
                        (int) levels.getValue(), (int) turns.getValue())).run();
                ss.setVisible(false);
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        p.add(start, gbc);

        ss.add(p);
        ss.pack();
        ss.setVisible(true);
    }
}
