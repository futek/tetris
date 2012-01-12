package com.salvadorjacobi.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.KeyStroke;

public class GameController implements ActionListener {
	public final GameModel model;

	private final MatrixView matrixView;
	private final JButton resetButton;
	private final JButton pauseButton;

	public GameController(final GameModel model, MatrixView matrixView, PreviewView previewView, HoldView holdView, ScoreView scoreView, JButton resetButton, final JButton pauseButton) {
		this.model = model;
		this.matrixView = matrixView;
		this.resetButton = resetButton;
		this.pauseButton = pauseButton;

		model.addObserver(matrixView);
		model.addObserver(previewView);
		model.addObserver(holdView);
		model.addObserver(scoreView);

		model.notifyObservers();

		matrixView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		matrixView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		matrixView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "rotatecw");
		matrixView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "rotateccw");
		matrixView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "softdropstart");
		matrixView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "softdropstop");
		matrixView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "harddrop");
		matrixView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK, false), "swap");
		matrixView.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "reset");

		matrixView.getActionMap().put("left", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (!model.isRunning()) return;

				model.shift(true);
				model.notifyObservers();
			}
		});

		matrixView.getActionMap().put("right", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (!model.isRunning()) return;

				model.shift(false);
				model.notifyObservers();
			}
		});

		matrixView.getActionMap().put("rotatecw", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (!model.isRunning()) return;

				if (model.rotate(true)) {
					Constants.sounds.get("rotate").play();
				} else {
					Constants.sounds.get("denied").play();
				}

				if (model.hasChanged()) {
					model.notifyObservers();
				}
			}
		});

		matrixView.getActionMap().put("rotateccw", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (!model.isRunning()) return;

				if (model.rotate(false)) {
					Constants.sounds.get("rotate").play();
				} else {
					Constants.sounds.get("denied").play();
				}

				model.notifyObservers();
			}
		});

		matrixView.getActionMap().put("softdropstart", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (!model.isRunning()) return;

				model.softDrop(true);
				model.notifyObservers();
			}
		});

		matrixView.getActionMap().put("softdropstop", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (!model.isRunning()) return;

				model.softDrop(false);
				model.notifyObservers();
			}
		});

		matrixView.getActionMap().put("harddrop", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (!model.isRunning()) return;

				model.hardDrop();

				Constants.sounds.get("drop").play();

				model.notifyObservers();
			}
		});

		matrixView.getActionMap().put("swap", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (!model.isRunning()) return;

				model.swap();
				model.notifyObservers();
			}
		});

		matrixView.getActionMap().put("reset", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				model.reset();

				pauseButton.setText("PAUSE");

				model.notifyObservers();
			}
		});
	}

	public void playList() {
		Constants.sounds.get("rickroll").loop();
	}

	public void actionPerformed(ActionEvent e) {
		if ("restart".equals(e.getActionCommand())) {
			model.reset();

			pauseButton.setText("PAUSE");
		} else if ("pause".equals(e.getActionCommand())) {
			if (model.isGameOver()) return;

			if (model.isPaused()) {
				model.resume();

				pauseButton.setText("PAUSE");
			} else {
				model.pause();

				pauseButton.setText("RESUME");
			}
		}

		model.notifyObservers();
	}
}
