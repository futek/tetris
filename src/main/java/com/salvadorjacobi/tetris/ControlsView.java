package com.salvadorjacobi.tetris;

import java.awt.Color;

import javax.swing.JLabel;

public class ControlsView extends JLabel {
	private static final String TEXT = "<html>" +
			"LEFT ARROW<br>SHIFT LEFT<br><br>" +
			"RIGHT ARROW<br>SHIFT RIGHT<br><br>" +
			"UP ARROW<br>ROTATE CLOCKWISE<br><br>" +
			"Z<br>ROTATE COUNTERCLOCKWISE<br><br>" +
			"DOWN ARROW<br>SOFT DROP<br><br>" +
			"SPACE<br>HARD DROP<br><br>" +
			"SHIFT<br>SWAP<br><br>" +
			"BACKSPACE<br>RESET" +
			"</html>";

	public ControlsView() {
		super();

		setForeground(Color.WHITE);
		setFont(Constants.baseFont);
		setText(String.format(TEXT));
	}
}
