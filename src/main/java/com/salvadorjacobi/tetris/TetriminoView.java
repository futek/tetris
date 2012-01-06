package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class TetriminoView extends JPanel {
	private final GameModel model;

	private Tetrimino.Shape shape;
	private final int rotation;
	private final int scale;

	public TetriminoView(GameModel model) {
		this.model = model;

		rotation = 0;
		scale = model.scale / 2;

		Dimension dimension = new Dimension(scale * 6, scale * 6);

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
		if (shape == null) return;

		// Tetrimino
		int[][] pattern = Constants.tetriminoShapes.get(shape)[rotation];
		BufferedImage blockImage = Constants.blockImages.get(shape);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (pattern[j][i] == 1) {
					g2d.drawImage(blockImage, (i + 1) * scale, (j + 1) * scale, scale, scale, null);
				}
			}
		}
	}

	public void setShape(Tetrimino.Shape shape) {
		this.shape = shape;
	}
}
