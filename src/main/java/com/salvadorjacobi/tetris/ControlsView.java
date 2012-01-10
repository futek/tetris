package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ControlsView extends JLabel {
	private static final String FORMAT = 
			"<html>" +
			"pil op - roter mod uret" + "<br><br>" +
			"z - roter med uret" + "<br><br>" +
			"pil venstre - ryk tetrimino til venstre" + "<br><br>" +
			"pil højre - ryk tetrimino til højre" + "<br><br>" +
			"pil ned - øg falde hastigheden" + "<br><br>" +
			"skift - hold på tetrimino" + "<br><br>" +
			"mellemrum - hårdt fald" + "<br><br>" +
			"tilbage - nyt spil" +
			"</html>";


	public ControlsView() {
		super();

		setPreferredSize(new Dimension(200, 450));

		setForeground(Color.WHITE);

		setFont(Constants.baseFont);
		setText(String.format(FORMAT));
	}
}
