package com.salvadorjacobi.tetris;

import java.awt.Point;
import java.util.Collections;
import java.util.Observable;
import java.util.Stack;

public class GameModel extends Observable {
	public static final int SKY_HEIGHT = 2;
	public static final int SOFTDROP_MULTIPLIER = 1;
	public static final int HARDDROP_MULTIPLIER = 2;

	public final int width;
	public final int height;
	public final int scale;

	private Block[][] matrix;
	private int score;
	private Tetrimino fallingTetrimino;
	private Tetrimino heldTetrimino;
	private boolean swapped;
	private boolean gameOver;
	private boolean paused;
	private final Stack<Tetrimino> bag = new Stack<Tetrimino>();
	private int linesClearedPerLevel;
	private int level;

	public GameModel(int width, int height, int scale) {
		this.width = width;
		this.height = height + SKY_HEIGHT;
		this.scale = scale;

		reset();
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
		return bag.peek();
	}

	public Tetrimino getHeldTetrimino() {
		return heldTetrimino;
	}

	public int getLevel() {
		return level;
	}

	public int getTickInterval() {
		return (int) (1200 * Math.exp(-0.2 * level));
	}

	public void tick() {
		if (gameOver || paused) return;

		Point down = new Point(0, 1);

		if (!translate(down)) {
			next();
		}

		setChanged();
	}

	public void populateBag() {
		while (!bag.empty()) {
			bag.pop();
		}

		for (Tetrimino.Shape shape : Tetrimino.Shape.VALUES) {
			bag.push(new Tetrimino(shape, new Point(0,0), 0));
		}

		Collections.shuffle(bag);
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

		spawn(bag.pop());

		swapped = false;

		if (bag.empty()) {
			populateBag();
		}

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

			spawn(bag.pop());
		}

		swapped = true;

		if (bag.empty()) {
			populateBag();
		}

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
		linesClearedPerLevel += lines;

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

		int threshold = levelThreshold();

		while (linesClearedPerLevel >= threshold) {
			level++;
			linesClearedPerLevel -= threshold;
		}

		setChanged();
		notifyObservers();

		return true;
	}

	public int levelThreshold() {
		return (int) (10 + 5 * Math.log(level));
	}

	public void spawn(Tetrimino tetrimino) {
		Point startPosition = new Point(width / 2 - 1, 1);

		tetrimino.setPosition(startPosition);
		tetrimino.setRotation(0);

		fallingTetrimino = tetrimino;

		if (isOverlapping(fallingTetrimino)) {
			gameOver = true;
		}
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

		boolean potentialGameOver = true;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (pattern[j][i] == 1) {
					int x = position.x - radius + i;
					int y = position.y - radius + j;

					matrix[x][y] = new Block(shape);

					if (y >= GameModel.SKY_HEIGHT) {
						potentialGameOver = false;
					}
				}
			}
		}

		if (potentialGameOver) {
			gameOver = true;
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

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isPaused() {
		return paused;
	}

	public void pause() {
		paused = true;
	}

	public void resume() {
		paused = false;
	}

	public void reset() {
		matrix = new Block[width][height];
		score = 0;
		heldTetrimino = null;
		fallingTetrimino = null;
		swapped = false;
		gameOver = false;
		linesClearedPerLevel = 0;
		level = 1;

		populateBag();
		next();

		setChanged();
	}
}
