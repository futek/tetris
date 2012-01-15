package com.salvadorjacobi.tetris;

import java.awt.Point;
import java.util.Collections;
import java.util.Observable;
import java.util.Stack;

public class GameModel extends Observable {
	public static final int TICK_INTERVAL = 1000 / 60;
	public static final int LOCK_DELAY = 30;
	public static final int SOFTDROP_SPEEDUP = 20;

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
	private int comboCounter;
	private boolean difficultClear;
	private boolean previousDifficultClear;
	private int linesCleared;
	private int level;
	private boolean tSpinKick;
	private boolean lastMoveRotation;
	private int hangTime;
	private int surfaceTime;
	private boolean softDrop;

	public GameModel(int width, int height, int scale) {
		this.width = width;
		this.height = height + SKY_HEIGHT;
		this.scale = scale;

		reset();
	}

	/**
	 * Get a block from the matrix
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @return the block at the specified coordinates
	 */
	public Block getBlock(int x, int y) {
		return matrix[x][y];
	}

	/**
	 * Get the current score
	 * @return the current score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Get the currently falling tetrimino
	 * @return the currently falling tetrimino
	 */
	public Tetrimino getFallingTetrimino() {
		return fallingTetrimino;
	}

	/**
	 * Get the next tetrimino in the queue
	 * @return the next tetrimino
	 */
	public Tetrimino getNextTetrimino() {
		return bag.peek();
	}

	/**
	 * Get the currently held tetrimino
	 * @return the currently held tetrimino
	 */
	public Tetrimino getHeldTetrimino() {
		return heldTetrimino;
	}

	/**
	 * Get the current
	 * @return the current level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Calculate the fall delay of the falling tetrimino in ticks
	 * @return ticks to wait before dropping the falling tetrimino
	 */
	public int getFallDelay() {
		return (int) (60 * Math.exp(-0.2 * (level - 1)));
	}

	/**
	 * Update the game by one tick
	 */
	public void tick() {
		hangTime += (softDrop ? SOFTDROP_SPEEDUP : 1);

		Point down = new Point(0, 1);
		Point originalPosition = fallingTetrimino.getPosition();

		boolean shouldFall = false;

		if (hangTime >= getFallDelay()) {
			hangTime = 0;
			shouldFall = true;
		}

		if (translate(down)) {
			if (shouldFall) {
				if (softDrop) {
					score += SOFTDROP_MULTIPLIER;
				}

				setChanged();
			} else {
				fallingTetrimino.setPosition(originalPosition);
			}
		} else {
			surfaceTime++;

			if (surfaceTime >= LOCK_DELAY) {
				surfaceTime = 0;

				next();

				setChanged();
			}
		}
	}

	/**
	 * Rotate falling tetrimino clockwise or counterclockwise
	 * @param clockwise clockwise if true, counterclock wise if false
	 * @return true if rotation was successful
	 */
	public boolean rotate(boolean clockwise) {
		if (gameOver || paused) return false;

		Tetrimino.Shape shape = fallingTetrimino.getShape();
		int rotation = fallingTetrimino.getRotation();
		Point originalPosition = fallingTetrimino.getPosition();
		int nextRotation;
		int[][] kickTranslations;

		fallingTetrimino.rotate(clockwise);

		nextRotation = fallingTetrimino.getRotation();

		int[][][] offsetData = Constants.offsetData.get(shape);
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

			// Detect T-spin kick
			if (shape == Tetrimino.Shape.T) {
				tSpinKick = (i != 0); // T-tetrimino did not use first offset, i.e. it kicked
			}

			surfaceTime = 0; // Reset surface time
			lastMoveRotation = true;

			setChanged();

			return true;
		}

		fallingTetrimino.rotate(!clockwise);

		return false;
	}

	/**
	 * Enable or disable softdrop
	 * @param enable true enables, false disables
	 */
	public void softDrop(boolean enable) {
		softDrop = enable;
	}

	/**
	 * Perform a harddrop
	 */
	public void hardDrop() {
		int lines = drop(fallingTetrimino);

		score += lines * HARDDROP_MULTIPLIER;

		if (lines > 0) {
			lastMoveRotation = false;
		}

		next();
	}

	/**
	 * Shift the falling tetrimino left or right
	 * @param left if true, right if false
	 * @return true if the shift was successful
	 */
	public boolean shift(boolean left) {
		Point offset = new Point((left ? -1 : 1), 0);

		surfaceTime = 0;
		lastMoveRotation = false;

		return translate(offset);
	}

	/**
	 * Perform a tetrimino swap if possible
	 */
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

	/**
	 * Determine if the game is running
	 * @return true if the game is not paused nor game over
	 */
	public boolean isRunning() {
		return !(gameOver || paused);
	}

	/**
	 * Determine if game over has been declared
	 * @return true if the game is over
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Determine if the game is paused
	 * @return true if the game is paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Pause the game
	 */
	public void pause() {
		paused = true;

		setChanged();
	}

	/**
	 * Resume the game from a paused state
	 */
	public void resume() {
		paused = false;

		setChanged();
	}

	/**
	 * Reset the game
	 */
	public void reset() {
		matrix = new Block[width][height];
		score = 0;
		heldTetrimino = null;
		fallingTetrimino = null;
		swapped = false;
		gameOver = false;
		paused = false;
		comboCounter = 0;
		difficultClear = false;
		previousDifficultClear = false;
		linesCleared = 0;
		level = 1;
		tSpinKick = false;
		lastMoveRotation = false;

		populateBag();
		next();

		setChanged();
	}

	/**
	 * Drops a tetrimino until it collides with something
	 * and returns the amount of lines it travelled in the process.
	 * @param	tetrimino	the tetrimino to drop
	 * @return	the amount of lines travelled in the drop.
	 * */
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

	/**
	 * Populate the random bag with one of each tetrimino and shuffle
	 */
	private void populateBag() {
		while (!bag.empty()) {
			bag.pop();
		}

		for (Tetrimino.Shape shape : Tetrimino.Shape.VALUES) {
			bag.push(new Tetrimino(shape, new Point(0,0), 0));
		}

		Collections.shuffle(bag);
	}

	/**
	 * Lock falling tetrimino in place,
	 * award points,
	 * and setup next tetrimino
	 */
	private void next() {
		if (fallingTetrimino != null) {
			Tetrimino.Shape shape = fallingTetrimino.getShape();

			// Embed falling tetrimino into matrix
			embed(fallingTetrimino);

			// Scoring
			int cornerSum = cornerSum(fallingTetrimino); // Must run prior to clear()
			boolean tSpin = (shape == Tetrimino.Shape.T && cornerSum >= 3 && lastMoveRotation);

			int lines = clear();
			int points = 0;

			// Award initial points
			switch (lines) {
				case 1:
					points += 100 * level;
					break;
				case 2:
					points += 300 * level;
					break;
				case 3:
					points += 500 * level;
					break;
				case 4:
					points += 800 * level;
					difficultClear = true;
					break;
			};

			if (lines > 0) {
				// Recognize a clearing, kicked T-spin as a difficult clear
				if (tSpin && tSpinKick) {
					difficultClear = true;
				}

				// Reward back-to-back difficult clear
				if (previousDifficultClear && difficultClear) {
					points += points / 2 * level;
				}

				previousDifficultClear = difficultClear;
				difficultClear = false;

				// Reward combos
				comboCounter++;
				points += (comboCounter - 1) * 50 * level;

				// Play sound
				Constants.sounds.get("clear").play();
			} else {
				// Reset combo counter
				comboCounter = 0;
			}

			// Reward T-spins
			if (tSpin) {
				switch (lines) {
					case 0:
						points += (tSpinKick ? 100 : 0) * level;
						break;
					case 1:
						points += (tSpinKick ? 200 : 100) * level;
						break;
					case 2:
						points += (tSpinKick ? 1200 : 300) * level;
						break;
					case 3:
						points += (tSpinKick ? 1600 : 500) * level;
						break;
				}
			}

			// Add points to score
			score += points;

			// Level up
			linesCleared += lines;

			int threshold = levelThreshold();

			while (linesCleared >= threshold) {
				level++;
				linesCleared -= threshold;

				threshold = levelThreshold(); // Update threshold
			}
		}

		// Spawn next tetrimino
		spawn(bag.pop());

		// Refill random bag if empty
		if (bag.empty()) {
			populateBag();
		}

		// Reset variables
		swapped = false;
		tSpinKick = false;

		setChanged();
	}

	/**
	 * Translate position of falling tetrimino
	 * @param delta translation offset
	 * @return true if translation was successful
	 */
	private boolean translate(Point delta) {
		Point originalPosition = fallingTetrimino.getPosition();

		fallingTetrimino.translate(delta);

		if (isOutOfBounds(fallingTetrimino, false) || isOverlapping(fallingTetrimino)) {
			fallingTetrimino.setPosition(originalPosition);
			return false;
		}

		setChanged();

		return true;
	}

	/**
	 * Attempt to clear lines
	 * @return amount of lines cleared
	 */
	private int clear() {
		int lines = 0;

		for (int j = 0; j < height; j++) {
			// Assume full row
			boolean full = true;

			for (int i = 0; i < width; i++) {
				if (getBlock(i, j) == null) {
					full = false;
					break;
				}
			}

			// Continue to next row if not full
			if (!full) {
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

		if (lines > 0) {
			setChanged();
		}

		return lines;
	}

	/**
	 * Calculates the amount of lines cleared required to level up
	 * @return lines cleared required for level up
	 */
	private int levelThreshold() {
		return (int) (10 + 5 * Math.log(level));
	}

	/**
	 * Spawn a tetrimino a the top of the matrix in its initial rotation
	 * @param tetrimino the tetrimino to spawn
	 */
	private void spawn(Tetrimino tetrimino) {
		Point startPosition = new Point(width / 2 - 1, 1);

		tetrimino.setPosition(startPosition);
		tetrimino.setRotation(0);

		fallingTetrimino = tetrimino;

		if (isOverlapping(fallingTetrimino)) {
			gameOver = true;
		}
	}

	/**
	 * Embed a tetrimino into the matrix
	 * @param tetrimino the tetrimino to embed
	 */
	private void embed(Tetrimino tetrimino) {
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

	/**
	 * Calculate the sum of blocks diagonally from the center of a tetrimino.
	 * Used to detect T-spins.
	 * @param tetrimino
	 * @return the corner sum
	 */
	private int cornerSum(Tetrimino tetrimino) {
		int sum = 0;
		Point position = tetrimino.getPosition();

		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				int x = position.x + i;
				int y = position.y + j;

				if (x < 0 || x >= width || y < 0 || y >= height) {
					sum++;
				} else if (getBlock(x, y) != null) {
					sum++;
				}
			}
		}

		return sum;
	}

	/**
	 * Determine if a tetrimino is overlapping with any blocks in the matrix
	 * @param tetrimino the tetrimino to test
	 * @return true if the tetrimino is overlapping
	 */
	private boolean isOverlapping(Tetrimino tetrimino) {
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

	/**
	 * Determine if a tetrimino is out of bounds
	 * @param tetrimino the tetrimino to test
	 * @param cutAtSkyline if true, consider anything above the skyline out of bounds
	 * @return true if the tetrimino is considered out of bounds
	 */
	private boolean isOutOfBounds(Tetrimino tetrimino, boolean cutAtSkyline) {
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
