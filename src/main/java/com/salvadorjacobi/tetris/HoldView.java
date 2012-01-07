package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class HoldView extends TetriminoView implements Observer {
	private final GameModel model;

	public HoldView(GameModel model) {
		super(model);

		this.model = model;

		this.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createEmptyBorder(),
						"HOLD",
						TitledBorder.CENTER,
						TitledBorder.TOP,
						null,
						Color.WHITE)
				);
	}

	public void update(Observable o, Object arg) {
		if (o == model) {
			Tetrimino heldTetrimino = model.getHeldTetrimino();

			if (heldTetrimino == null) return;

			setTetrimino(model.getHeldTetrimino());
			repaint();
		}
	}
}
