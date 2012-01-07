package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class TetriminoView extends JPanel {
	private final GameModel model;

	private Tetrimino tetrimino;
	private final int scale;

	public TetriminoView(GameModel model) {
		this.model = model;

		scale = model.scale / 2;

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
		g2d.setBackground(Color.BLACK);
		g2d.clearRect(0, 0, getWidth(), getHeight());

		// Return if shape is null
		if (tetrimino == null) return;

		// Tetrimino
		Tetrimino.Shape shape = tetrimino.getShape();
		Point position = new Point(0, 0);
		int[][] pattern = Constants.trueRotation.get(shape)[0];
		BufferedImage blockImage = Constants.blockImages.get(shape);

		int size = pattern.length;

		double dx = 0;
		double dy = 0;

		switch (shape) {
			case I:
				dx -= 0.5;
				break;
			case O:
				dx += 0.5;
				dy += 1.5;
				break;
			default:
				dx += 1.0;
				dy += 1.5;
		}

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (pattern[j][i] == 1) {
					int x = (int) ((position.x + i + dx) * scale);
					int y = (int) ((position.y + j + dy) * scale);

					g2d.drawImage(blockImage, x, y, scale, scale, null);
				}
			}
		}
	}

	public void setTetrimino(Tetrimino tetrimino) {
		this.tetrimino = tetrimino;
	}
}
