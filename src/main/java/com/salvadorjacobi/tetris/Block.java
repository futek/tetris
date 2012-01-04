package com.salvadorjacobi.tetris;

public class Block {
	private Tetrimino.Shape parentShape;

	public Block(Tetrimino.Shape shapeOrigin) {
		this.parentShape = shapeOrigin;
	}

	public Tetrimino.Shape getParentShape() {
		return parentShape;
	}
}
