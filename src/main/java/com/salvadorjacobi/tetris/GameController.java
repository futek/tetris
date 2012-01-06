package com.salvadorjacobi.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class GameController {
	public GameController(final GameModel model, WellView wellView, PreviewView previewView, TetriminoView holdView, ScoreView scoreView) {
		model.addObserver(wellView);
		model.addObserver(previewView);
		model.addObserver(scoreView);

		model.notifyObservers();

		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "space");

		wellView.getActionMap().put("left", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.move(-1, 0);
			}
		});

		wellView.getActionMap().put("right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.move(1, 0);
			}
		});

		wellView.getActionMap().put("up", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.rotate(true);
			}
		});

		wellView.getActionMap().put("down", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.softDrop();
			}
		});

		wellView.getActionMap().put("space", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.hardDrop();

			}
		});
	}
}
