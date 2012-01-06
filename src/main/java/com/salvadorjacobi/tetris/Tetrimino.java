package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Tetrimino {
	public enum Shape {
		I, J, L, O, S, T, Z;

		private static final List<Shape> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static Shape randomShape() {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}

		public Color getColor() {
			return Constants.blockColors.get(this);
		}
	}

	public Point position;

	private final Shape shape;
	private int rotation;

	public Tetrimino(Shape shape, Point position) {
		this.shape = shape;
		this.position = position;

		position = new Point(0, -1);
		rotation = 0;
	}

	public Tetrimino(Tetrimino tetrimino) {
		this.shape = tetrimino.shape;
		this.position = (Point) tetrimino.position.clone();
		this.rotation = tetrimino.rotation;
	}

	public Tetrimino.Shape getShape() {
		return shape;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		if (rotation >= 0 && rotation < Constants.tetriminoShapes.get(shape).length) {
			this.rotation = rotation;
		}
	}

	public void rotate(boolean direction) {
		if (direction) {
			rotation = (rotation + 1) % Constants.tetriminoShapes.get(shape).length;
		} else {
			rotation--;

			if (rotation < 0) {
				rotation = Constants.tetriminoShapes.get(shape).length - 1;
			}
		}
	}
}
