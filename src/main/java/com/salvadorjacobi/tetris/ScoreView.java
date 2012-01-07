package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

public class ScoreView extends JLabel implements Observer {
	private static final String FORMAT = "<html>SCORE<br>%d</div></html>";

	private final GameModel model;
	private final int scale;

	public ScoreView(GameModel model) {
		super();

		this.model = model;
		scale = model.scale / 2;

		setPreferredSize(new Dimension(scale * 5, scale * 3));

		setBackground(Color.DARK_GRAY);
		setForeground(Color.WHITE);

		setText(String.format(FORMAT, 0));
		setOpaque(true);
	}

	public void update(Observable o, Object arg) {
		if (o == model) {
			setText(String.format(FORMAT, model.getScore()));
		}
	}
}
