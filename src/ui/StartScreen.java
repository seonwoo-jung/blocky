package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class StartScreen {

	private static final int MAX_PLAYERS = 4;

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

		Font labelFont = new Font("Arial", Font.BOLD, 16);

		JLabel levelLabel = new JLabel("Levels");
		levelLabel.setFont(labelFont);
		gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1;
		p.add(levelLabel, gbc);

		JLabel humanLabel = new JLabel("Human Players");
		humanLabel.setFont(labelFont);
		gbc.gridy = 3; p.add(humanLabel, gbc);

		JLabel ai1Label = new JLabel("Smart AI");
		ai1Label.setFont(labelFont);
		gbc.gridy = 4; p.add(ai1Label, gbc);

		JLabel ai2Label = new JLabel("Smart AI 2");
		ai2Label.setFont(labelFont);
		gbc.gridy = 5; p.add(ai2Label, gbc);

		JLabel ai3Label = new JLabel("Simple AI");
		ai3Label.setFont(labelFont);
		gbc.gridy = 6; p.add(ai3Label, gbc);

		JLabel turnsLabel = new JLabel("Turns");
		turnsLabel.setFont(labelFont);
		gbc.gridy = 7; p.add(turnsLabel, gbc);

		SpinnerNumberModel levelModel = new SpinnerNumberModel(3, 2, 6, 1);
		JSpinner levels = new JSpinner(levelModel);
		levels.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridx = 2; gbc.gridy = 2;
		p.add(levels, gbc);

		// Player spinners
		SpinnerNumberModel humanModel = new SpinnerNumberModel(1, 0, 4, 1);
		SpinnerNumberModel smartAIModel = new SpinnerNumberModel(0, 0, 4, 1);
		SpinnerNumberModel smartAI2Model = new SpinnerNumberModel(0, 0, 4, 1);
		SpinnerNumberModel simpleAIModel = new SpinnerNumberModel(0, 0, 4, 1);

		JSpinner human = new JSpinner(humanModel);
		JSpinner smartAI = new JSpinner(smartAIModel);
		JSpinner smartAI2 = new JSpinner(smartAI2Model);
		JSpinner simpleAI = new JSpinner(simpleAIModel);

		human.setFont(new Font("Arial", Font.PLAIN, 18));
		smartAI.setFont(new Font("Arial", Font.PLAIN, 18));
		smartAI2.setFont(new Font("Arial", Font.PLAIN, 18));
		simpleAI.setFont(new Font("Arial", Font.PLAIN, 18));

		ChangeListener playerLimiter = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int total = (int) human.getValue() + (int) smartAI.getValue()
					+ (int) smartAI2.getValue() + (int) simpleAI.getValue();

				if (total > MAX_PLAYERS) {
					((JSpinner) e.getSource()).setValue(((int) ((JSpinner) e.getSource()).getValue()) - 1);
				}
			}
		};

		human.addChangeListener(playerLimiter);
		smartAI.addChangeListener(playerLimiter);
		smartAI2.addChangeListener(playerLimiter);
		simpleAI.addChangeListener(playerLimiter);

		gbc.gridx = 2; gbc.gridy = 3; p.add(human, gbc);
		gbc.gridy = 4; p.add(smartAI, gbc);
		gbc.gridy = 5; p.add(smartAI2, gbc);
		gbc.gridy = 6; p.add(simpleAI, gbc);

		SpinnerNumberModel turnsModel = new SpinnerNumberModel(20, 8, 60, 1);
		JSpinner turns = new JSpinner(turnsModel);
		turns.setFont(new Font("Arial", Font.PLAIN, 20));
		gbc.gridy = 7; p.add(turns, gbc);

		JButton start = new JButton("Start");
		start.setForeground(Color.BLUE);
		start.setFont(new Font("Arial", Font.BOLD, 16));
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(() -> BlockyMain.launchGame(
					(int) human.getValue(),
					(int) smartAI.getValue(),
					(int) smartAI2.getValue(),
					(int) simpleAI.getValue(),
					(int) levels.getValue(),
					(int) turns.getValue()
				)).start();
				ss.setVisible(false);
			}
		});

		gbc.gridx = 2; gbc.gridy = 8; gbc.gridwidth = 2;
		p.add(start, gbc);

		ss.add(p);
		ss.pack();
		ss.setVisible(true);
	}
}
