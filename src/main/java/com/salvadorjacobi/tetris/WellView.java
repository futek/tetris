package com.salvadorjacobi.tetris;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class WellView extends JPanel implements Observer {
	private final GameModel model;
	private final int width;
	private final int height;

	public WellView(GameModel model) {
		this.model = model;
		this.width = model.width;
		this.height = model.height;

		setPreferredSize(new Dimension(width * model.scale, height * model.scale));
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

		// Well
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Block block = model.getBlock(i, j);
				if (block == null) {
					continue;
				}

				BufferedImage blockImage = Constants.blockImages.get(block.getParentShape());
				g2d.drawImage(blockImage, i * model.scale, j * model.scale, model.scale, model.scale, null);
			}
		}

		// Falling tetrimino and ghost
		Tetrimino tetrimino = model.getFallingTetrimino();
		Tetrimino.Shape shape = tetrimino.getShape();
		int[][] pattern = Constants.tetriminoShapes.get(shape)[tetrimino.getRotation()];
		BufferedImage blockImage = Constants.blockImages.get(shape);

		Tetrimino ghost = new Tetrimino(tetrimino);
		model.dropToFloor(ghost);

		Composite originalComposite = g2d.getComposite();
		Composite translucentComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);


		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (pattern[j][i] == 1) {
					// Ghost tetrimino
					g2d.setComposite(translucentComposite);
					g2d.drawImage(Constants.blockBaseImage, (i + ghost.position.x) * model.scale, (j + ghost.position.y) * model.scale, model.scale, model.scale, null);

					// Falling tetrimino
					g2d.setComposite(originalComposite);
					g2d.drawImage(blockImage, (i + tetrimino.position.x) * model.scale, (j + tetrimino.position.y) * model.scale, model.scale, model.scale, null);
				}
			}
		}
	}

	public void update(Observable o, Object arg) {
		if (o == model) {
			repaint();
		}
	}
}
