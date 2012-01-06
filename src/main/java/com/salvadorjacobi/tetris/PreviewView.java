package com.salvadorjacobi.tetris;

import java.util.Observable;
import java.util.Observer;

public class PreviewView extends TetriminoView implements Observer {
	private final GameModel model;

	public PreviewView(GameModel model) {
		super(model);

		this.model = model;
	}

	public void update(Observable o, Object arg) {
		if (o == model) {
			setShape(model.getNextTetrimino().getShape());
			repaint();
		}
	}
}
