package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class ScoreView extends JPanel implements Observer {
	private final GameModel model;

	public ScoreView(GameModel model) {
		super();

		this.model = model;

		int scale = model.scale / 2;
		Dimension dimension = new Dimension(scale * 5, scale * 5);

		setMinimumSize(dimension);
		setMaximumSize(dimension);
		setPreferredSize(dimension);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		// Background
		g2d.setBackground(Color.DARK_GRAY);
		g2d.clearRect(0, 0, getWidth(), getHeight());

		// Font settings
		g2d.setFont(Constants.baseFont);
		g2d.setColor(Color.WHITE);

		String level = Integer.toString(model.getLevel());
		String score = Integer.toString(model.getScore());

		int maxAscent = g2d.getFontMetrics().getMaxAscent();
		int y = 0;
		int width;

		// Level label
		y += maxAscent;
		width = g2d.getFontMetrics().stringWidth("LEVEL");
		g2d.drawString("LEVEL", getWidth() / 2 - width / 2, y);

		// Actual level
		y += maxAscent;
		width = g2d.getFontMetrics().stringWidth(level);
		g2d.drawString(level, getWidth() - width, y);

		// Score label
		y += maxAscent * 2;
		width = g2d.getFontMetrics().stringWidth("SCORE");
		g2d.drawString("SCORE", getWidth() / 2 - width / 2, y);

		// Actual score
		y += maxAscent;
		width = g2d.getFontMetrics().stringWidth(score);
		g2d.drawString(score, getWidth() - width, y);
	}

	public void update(Observable o, Object arg) {
		if (o == model) {
			repaint();
		}
	}
}
