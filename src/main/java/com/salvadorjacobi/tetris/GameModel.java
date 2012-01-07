package com.salvadorjacobi.tetris;

import java.awt.Point;
import java.util.Observable;

public class GameModel extends Observable {
	public static final int SKY_HEIGHT = 2;
	public static final int SOFTDROP_MULTIPLIER = 1;
	public static final int HARDDROP_MULTIPLIER = 2;

	public final int width;
	public final int height;
	public final int scale;

	private final Block[][] matrix;
	private int score;
	private Tetrimino fallingTetrimino;
	private Tetrimino nextTetrimino;
	private Tetrimino heldTetrimino;
	private boolean swapped;

	public GameModel(int width, int height, int scale) {
		this.width = width;
		this.height = height + SKY_HEIGHT;
		this.scale = scale;

		matrix = new Block[this.width][this.height];
		score = 0;
		swapped = false;

		next();

		setChanged();
	}

	public Block getBlock(int x, int y) {
		return matrix[x][y];
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

	public void next() {
		if (fallingTetrimino != null) {
			embed(fallingTetrimino);

			if (clear()) {
				Constants.sounds.get("clear").play();
			}
		}

		if (nextTetrimino == null) {
			nextTetrimino = new Tetrimino(Tetrimino.Shape.randomShape(), new Point(0, 0), 0);
		}

		spawn(nextTetrimino);

		nextTetrimino = new Tetrimino(Tetrimino.Shape.randomShape(), new Point(0, 0), 0);
		swapped = false;

		setChanged();
	}

	public boolean translate(Point delta) {
		Point originalPosition = fallingTetrimino.getPosition();

		fallingTetrimino.translate(delta);

		if (isOutOfBounds(fallingTetrimino, false) || isOverlapping(fallingTetrimino)) {
			fallingTetrimino.setPosition(originalPosition);
			return false;
		}

		setChanged();

		return true;
	}

	public boolean rotate(boolean direction) {
		int rotation = fallingTetrimino.getRotation();
		Point originalPosition = fallingTetrimino.getPosition();
		int nextRotation;
		int[][] kickTranslations;

		fallingTetrimino.rotate(direction);

		nextRotation = fallingTetrimino.getRotation();

		int[][][] offsetData = Constants.offsetData.get(fallingTetrimino.getShape());
		int offsetCount = offsetData[rotation].length;

		kickTranslations = new int[offsetCount][];

		for (int i = 0; i < offsetCount; i++) {
			int[] offset = offsetData[rotation][i];
			int[] nextOffset = offsetData[nextRotation][i];

			kickTranslations[i] = new int[] {offset[0] - nextOffset[0], offset[1] - nextOffset[1]};
		}

		for (int i = 0; i < kickTranslations.length; i++) {
			int dx = kickTranslations[i][0];
			int dy = kickTranslations[i][1];

			fallingTetrimino.translate(new Point(dx, dy));

			if (isOutOfBounds(fallingTetrimino, false) || isOverlapping(fallingTetrimino)) {
				fallingTetrimino.setPosition(originalPosition);

				continue;
			}

			setChanged();

			return true;
		}

		fallingTetrimino.rotate(!direction);

		return false;
	}

	public void softDrop() {
		Point down = new Point(0, 1);

		if (translate(down)) {
			score += SOFTDROP_MULTIPLIER;
		}

		setChanged();
	}

	public void hardDrop() {
		int lines = drop(fallingTetrimino);

		score += lines * HARDDROP_MULTIPLIER;

		next();
	}

	public void swap() {
		if (swapped) return;

		if (heldTetrimino != null) {
			Tetrimino temporary = heldTetrimino;
			heldTetrimino = fallingTetrimino;

			spawn(temporary);
		} else {
			heldTetrimino = fallingTetrimino;

			spawn(nextTetrimino);

			nextTetrimino = new Tetrimino(Tetrimino.Shape.randomShape(), new Point(0, 0), 0);
		}

		swapped = true;

		setChanged();
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
					matrix[i][k] = matrix[i][k - 1];
				}
			}

			// Clear top row
			for (int i = 0; i < width; i++) {
				matrix[i][0] = null;
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

	public void spawn(Tetrimino tetrimino) {
		Point startPosition = new Point(width / 2 - 1, 1);

		tetrimino.setPosition(startPosition);
		tetrimino.setRotation(0);

		fallingTetrimino = tetrimino;
	}

	public int drop(Tetrimino tetrimino) {
		int lines = 0;

		Point down = new Point(0, 1);
		Point up = new Point(0, -1);

		while (!isOutOfBounds(tetrimino, false) && !isOverlapping(tetrimino)) {
			tetrimino.translate(down);

			lines++;
		}

		tetrimino.translate(up);

		return lines - 1;
	}

	public void embed(Tetrimino tetrimino) {
		Tetrimino.Shape shape = tetrimino.getShape();
		Point position = tetrimino.getPosition();
		int rotation = tetrimino.getRotation();
		int[][] pattern = Constants.trueRotation.get(shape)[rotation];

		int size = pattern.length;
		int radius = (size - 1) / 2;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (pattern[j][i] == 1) {
					matrix[position.x - radius + i][position.y - radius + j] = new Block(shape);
				}
			}
		}
	}

	public boolean isOverlapping(Tetrimino tetrimino) {
		Tetrimino.Shape shape = tetrimino.getShape();
		Point position = tetrimino.getPosition();
		int rotation = tetrimino.getRotation();
		int[][] pattern = Constants.trueRotation.get(shape)[rotation];

		int size = pattern.length;
		int radius = (size - 1) / 2;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (pattern[j][i] == 1) {
					int x = position.x - radius + i;
					int y = position.y - radius + j;

					if (matrix[x][y] != null)
						return true;
				}
			}
		}

		return false;
	}

	public boolean isOutOfBounds(Tetrimino tetrimino, boolean cutAtSkyline) {
		Tetrimino.Shape shape = tetrimino.getShape();
		Point position = tetrimino.getPosition();
		int rotation = tetrimino.getRotation();
		int[][] pattern = Constants.trueRotation.get(shape)[rotation];

		int size = pattern.length;
		int radius = (size - 1) / 2;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (pattern[j][i] == 1) {
					int x = i + position.x - radius;
					int y = j + position.y - radius;

					if (pattern[j][i] == 1) {
						if (x < 0 || x >= width || y < (cutAtSkyline ? SKY_HEIGHT : 0) || y >= height)
							return true;
					}
				}
			}
		}

		return false;
	}
}
