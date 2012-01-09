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

		public static final List<Shape> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static Shape randomShape() {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}

		public Color getColor() {
			return Constants.blockColors.get(this);
		}
	}

	private final Shape shape;
	private final Point position;
	private int rotation;

	public Tetrimino(Shape shape, Point position, int rotation) {
		if (position == null)
			throw new IllegalArgumentException("Position cannot be null");
		if (rotation < 0 || rotation > 4)
			throw new IllegalArgumentException("Rotation should be in range 0 to 3");

		this.shape = shape;
		this.position = position;
		this.rotation = rotation;
	}

	public Tetrimino(Tetrimino tetrimino) {
		this.shape = tetrimino.shape;
		this.position = (Point) tetrimino.position.clone();
		this.rotation = tetrimino.rotation;
	}

	public void setRotation(int rotation) {
		if (rotation >= 0 && rotation < 4) {
			this.rotation = rotation;
		}
	}

	public Point getPosition() {
		return new Point(position.x, position.y);
	}

	public Tetrimino.Shape getShape() {
		return shape;
	}

	public int getRotation() {
		return rotation;
	}

	public void setPosition(Point position) {
		this.position.setLocation(position);
	}

	public void translate(Point position) {
		this.position.translate(position.x, position.y);
	}

	public void rotate(boolean direction) {
		rotation = (direction ? rotation + 1 : rotation - 1) % 4;

		if (rotation < 0) {
			rotation = 3;
		}
	}
}
