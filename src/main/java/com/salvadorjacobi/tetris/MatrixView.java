package com.salvadorjacobi.tetris;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class MatrixView extends JPanel implements Observer {
	private final GameModel model;
	private final int width;
	private final int height;

	private final Composite alphaComposite;

	public MatrixView(GameModel model) {
		this.model = model;
		this.width = model.width;
		this.height = model.height;

		Dimension dimension = new Dimension(this.width * model.scale, (this.height - GameModel.SKY_HEIGHT) * model.scale);

		setMinimumSize(dimension);
		setMaximumSize(dimension);
		setPreferredSize(dimension);

		alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;


		// Background
		g2d.setBackground(Color.BLACK);
		g2d.clearRect(0, 0, getWidth(), getHeight());

		// Grid
		g.setColor(Color.decode("#1a1a1a"));

		for (int i = 1; i < width; i++) {
			g2d.drawLine(model.scale * i, 0, model.scale * i, height * model.scale);
		}

		for (int i = 1; i < height; i++) {
			g2d.drawLine(0, model.scale * i, width * model.scale, model.scale * i);
		}

		// Matrix
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Block block = model.getBlock(i, j);
				if (block == null) {
					continue;
				}

				BufferedImage blockImage = Constants.blockImages.get(block.getParentShape());

				if (model.isPaused()) {
					blockImage = Constants.blockBaseImage;
				}

				g2d.drawImage(blockImage, i * model.scale, (j - GameModel.SKY_HEIGHT) * model.scale, model.scale, model.scale, null);
			}
		}

		// Falling tetrimino
		Tetrimino tetrimino = model.getFallingTetrimino();
		Tetrimino.Shape shape = tetrimino.getShape();
		Point position = tetrimino.getPosition();
		int rotation = tetrimino.getRotation();

		int[][] pattern = Constants.trueRotation.get(shape)[rotation];
		BufferedImage blockImage = Constants.blockImages.get(shape);

		if (model.isPaused()) {
			blockImage = Constants.blockBaseImage;
		}

		// Ghost tetrimino
		Tetrimino ghost = new Tetrimino(tetrimino);
		model.drop(ghost);
		Point ghostPosition = ghost.getPosition();

		Composite originalComposite = g2d.getComposite();

		int size = pattern.length;
		int radius = (size - 1) / 2;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (pattern[j][i] == 1) {
					int x, y;

					x = (i + ghostPosition.x - radius) * model.scale;
					y = (j + ghostPosition.y - radius - GameModel.SKY_HEIGHT) * model.scale;

					g2d.setComposite(alphaComposite);
					g2d.drawImage(blockImage, x, y, model.scale, model.scale, null);

					x = (i + position.x - radius) * model.scale;
					y = (j + position.y - radius - GameModel.SKY_HEIGHT) * model.scale;

					g2d.setComposite(originalComposite);
					g2d.drawImage(blockImage, x, y, model.scale, model.scale, null);
				}
			}
		}

		if (model.isGameOver()) {
			g2d.setColor(Color.RED);
			g2d.fillRect(100, 275, 110, 40);

			g2d.setColor(Color.WHITE);
			g2d.drawString("GAME OVER", 120, 300);
		} else if (model.isPaused()) {
			g2d.setColor(Color.BLUE);
			g2d.fillRect(100, 275, 120, 40);

			g2d.setColor(Color.WHITE);
			g2d.drawString("GAME PAUSED", 118, 300);
		}
	}

	public void update(Observable o, Object arg) {
		if (o == model) {
			repaint();
		}
	}
}
