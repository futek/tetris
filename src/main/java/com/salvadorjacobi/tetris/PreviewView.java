package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

public class PreviewView extends TetriminoView implements Observer {
	private final GameModel model;

	public PreviewView(GameModel model) {
		super(model);

		this.model = model;

		this.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createEmptyBorder(),
						"NEXT",
						TitledBorder.CENTER,
						TitledBorder.TOP,
						Constants.baseFont,
						Color.WHITE)
				);
	}

	public void update(Observable o, Object arg) {
		if (o == model) {
			setTetrimino(model.getNextTetrimino());
			repaint();
		}
	}
}
