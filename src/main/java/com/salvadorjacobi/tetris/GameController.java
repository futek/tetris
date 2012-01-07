package com.salvadorjacobi.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class GameController {
	@SuppressWarnings("serial")
	public GameController(final GameModel model, final WellView wellView, PreviewView previewView, HoldView holdView, ScoreView scoreView) {
		//playList(); //baggrundsmusik
		
		model.addObserver(wellView);
		model.addObserver(previewView);
		model.addObserver(holdView);
		model.addObserver(scoreView);

		model.notifyObservers();

		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "rotate");
		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "softdrop");
		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "harddrop");
		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "reset");
		wellView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK, false), "swap");
		
		
		wellView.getActionMap().put("left", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.move(-1, 0);

				if (model.hasChanged()) {
					model.notifyObservers();
				}
			}
		});

		wellView.getActionMap().put("right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.move(1, 0);

				if (model.hasChanged()) {
					model.notifyObservers();
				}
			}
		});

		wellView.getActionMap().put("rotate", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (model.rotate(true)) {
					Constants.sounds.get("rotate").play();
				}

				if (model.hasChanged()) {
					model.notifyObservers();
				}
			}
		});

		wellView.getActionMap().put("softdrop", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.softDrop();

				if (model.hasChanged()) {
					model.notifyObservers();
				}
			}
		});

		wellView.getActionMap().put("harddrop", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.hardDrop();
				Constants.sounds.get("drop").play();

				if (model.hasChanged()) {
					model.notifyObservers();
				}
			}
		});

		wellView.getActionMap().put("swap", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.swap();

				if (model.hasChanged()) {
					model.notifyObservers();
				}
			}
		});
		
		wellView.getActionMap().put("reset", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.reset();
			}
		});
	}
	
	public void playList() {
		Constants.sounds.get("rickroll").loop();
	}
}
