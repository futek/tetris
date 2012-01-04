package com.salvadorjacobi.tetris;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

public class ScoreView extends JLabel implements Observer {
	private static final String PREFIX = "SCORE: ";

	private final GameModel model;

	public ScoreView(GameModel model) {
		super();

		this.model = model;

		setPreferredSize(new Dimension(model.scale * 6, model.scale * 3));

		setText(PREFIX + 0);
		setOpaque(true);
	}

	public void update(Observable o, Object arg) {
		if (o == model) {
			setText(PREFIX + model.getScore());
		}
	}
}
