package com.salvadorjacobi.tetris;

import java.util.Observable;
import java.util.Observer;

public class HoldView extends TetriminoView implements Observer {
	private final GameModel model;

	public HoldView(GameModel model) {
		super(model);

		this.model = model;
	}

	public void update(Observable o, Object arg) {
		if (o == model) {
			Tetrimino heldTetrimino = model.getHeldTetrimino();

			if (heldTetrimino == null) return;

			setShape(model.getHeldTetrimino().getShape());
			repaint();
		}
	}
}
