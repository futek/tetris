package com.salvadorjacobi.tetris;

import java.awt.Point;
import java.util.Observable;

public class GameModel extends Observable {
	public static final int SOFTDROP_MULTIPLIER = 1;
	public static final int HARDDROP_MULTIPLIER = 2;

	public final int width;
	public final int height;
	public final int scale;

	private final Block[][] well;
	private int score;
	private Tetrimino fallingTetrimino;
	private Tetrimino nextTetrimino;
	private Tetrimino heldTetrimino;
	private boolean swapped;

	public GameModel(int width, int height, int scale) {
		this.width = width;
		this.height = height;
		this.scale = scale;

		well = new Block[width][height];
		score = 0;
		nextTetrimino = new Tetrimino(Tetrimino.Shape.randomShape(), new Point(width / 2 - 2, -1));
		swapped = false;

		next();

		setChanged();
	}

	public Block getBlock(int x, int y) {
		return well[x][y];
	}

	public int getScore() {
		return score;
	}

	public Tetrimino getFallingTetrimino() {
		return fallingTetrimino;
	}

	public Tetrimino getNextTetrimino() {
		return nextTetrimino;
	}

	public Tetrimino getHeldTetrimino() {
		return heldTetrimino;
	}

	public boolean clear() {
		int lines = 0;

		for (int j = 0; j < height; j++) {
			boolean cleared = true;

			for (int i = 0; i < width; i++) {
				if (getBlock(i, j) == null) {
					cleared = false;
					break;
				}
			}

			if (!cleared) {
				continue;
			}

			lines++;

			// Move blocks down
			for (int k = j; k > 0; k--) {
				for (int i = 0; i < width; i++) {
					well[i][k] = well[i][k - 1];
				}
			}

			// Clear top row
			for (int i = 0; i < width; i++) {
				well[i][0] = null;
			}
		}

		int points = 0;

		switch (lines) {
			case 0:
				return false;
			case 1:
				points = 100;
				break;
			case 2:
				points = 300;
				break;
			case 3:
				points = 500;
				break;
			case 4:
				points = 800;
				break;
		}

		score += points;

		setChanged();
		notifyObservers();

		return true;
	}

	public boolean move(int x, int y) {
		Point previousPosition = (Point) fallingTetrimino.position.clone();

		fallingTetrimino.position.translate(x, y);

		if (isOutOfBounds(fallingTetrimino)) {
			fallingTetrimino.position.setLocation(previousPosition);
			return false;
		}

		if (isOverlapping(fallingTetrimino)) {
			fallingTetrimino.position.setLocation(previousPosition);
			return false;
		}

		clear();

		setChanged();
		notifyObservers();

		return true;
	}

	public boolean rotate(boolean direction) {
		fallingTetrimino.rotate(direction);

		if (isOutOfBounds(fallingTetrimino)) {
			fallingTetrimino.rotate(!direction);
			return false;
		}

		if (isOverlapping(fallingTetrimino)) {
			fallingTetrimino.rotate(!direction);
			return false;
		}

		clear();

		setChanged();
		notifyObservers();

		return true;
	}

	public void softDrop() {
		if (move(0, 1)) {
			score += SOFTDROP_MULTIPLIER;
		}

		setChanged();
		notifyObservers();
	}

	public int dropToFloor(Tetrimino tetrimino) {
		int lines = 0;

		while (!isOutOfBounds(tetrimino) && !isOverlapping(tetrimino)) {
			tetrimino.position.translate(0, 1);

			lines++;
		}

		tetrimino.position.translate(0, -1);

		return lines - 1;
	}

	public void hardDrop() {
		int lines = dropToFloor(fallingTetrimino);

		score += lines * HARDDROP_MULTIPLIER;

		next();
	}

	public void next() {
		if (fallingTetrimino != null) {
			// Embed falling tetrimino blocks into well
			Tetrimino.Shape shape = fallingTetrimino.getShape();
			int rotation = fallingTetrimino.getRotation();
			int[][] pattern = Constants.tetriminoShapes.get(shape)[rotation];

			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (pattern[j][i] == 1) {
						well[i + fallingTetrimino.position.x][j + fallingTetrimino.position.y] = new Block(shape);
					}
				}
			}

			clear();
		}

		fallingTetrimino = nextTetrimino;
		nextTetrimino = new Tetrimino(Tetrimino.Shape.randomShape(), new Point(width / 2 - 2, -1));
		swapped = false;

		setChanged();
		notifyObservers();
	}

	public void swap() {
		if (swapped) return;

		if (heldTetrimino != null) {
			Tetrimino temporary = heldTetrimino;
			heldTetrimino = fallingTetrimino;
			fallingTetrimino = temporary;

			fallingTetrimino.position.setLocation(width / 2 - 2, -1);
			fallingTetrimino.setRotation(0);
		} else {
			heldTetrimino = fallingTetrimino;

			fallingTetrimino = nextTetrimino;
			nextTetrimino = new Tetrimino(Tetrimino.Shape.randomShape(), new Point(width / 2 - 2, -1));
		}

		swapped = true;

		setChanged();
		notifyObservers();
	}

	public boolean isOverlapping(Tetrimino tetrimino) {
		Tetrimino.Shape shape = tetrimino.getShape();
		int rotation = tetrimino.getRotation();
		int[][] pattern = Constants.tetriminoShapes.get(shape)[rotation];

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (pattern[j][i] == 1) {
					if (well[i + tetrimino.position.x][j + tetrimino.position.y] != null)
						return true;
				}
			}
		}

		return false;
	}

	public boolean isOutOfBounds(Tetrimino tetrimino) {
		Tetrimino.Shape shape = tetrimino.getShape();
		int rotation = tetrimino.getRotation();
		int[][] pattern = Constants.tetriminoShapes.get(shape)[rotation];

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int x = i + tetrimino.position.x;
				int y = j + tetrimino.position.y;

				if (pattern[j][i] == 1) {
					if (x < 0 || x >= width || y < 0 || y >= height)
						return true;
				}
			}
		}

		return false;
	}
}
