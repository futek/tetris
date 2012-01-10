package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Tetris extends JPanel {
	public static GameModel model;
	public final GameController controller;

	public final MatrixView matrixView;

	public final Timer timer;

	public Tetris(int width, int height, int scale) {
		model = new GameModel(width, height, scale);

		matrixView = new MatrixView(model);
		PreviewView previewView = new PreviewView(model);
		HoldView holdView = new HoldView(model);
		ScoreView scoreView = new ScoreView(model);

		JButton resetButton = new JButton("RESTART");
		JButton pauseButton = new JButton("PAUSE");

		controller = new GameController(model, matrixView, previewView, holdView, scoreView, resetButton, pauseButton);

		resetButton.setActionCommand("restart");
		resetButton.addActionListener(controller);
		resetButton.setFocusable(false);
		pauseButton.setActionCommand("pause");
		pauseButton.addActionListener(controller);
		pauseButton.setFocusable(false);

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(matrixView)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(previewView)
						.addComponent(scoreView)
						.addComponent(resetButton)
						.addComponent(pauseButton)
						.addComponent(holdView)
						)
				);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(matrixView)
						.addGroup(layout.createSequentialGroup()
								.addComponent(previewView)
								.addComponent(scoreView)
								.addComponent(resetButton)
								.addComponent(pauseButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(holdView)
								)
						)
				);

		this.setBackground(Color.DARK_GRAY);

		timer = new Timer(GameModel.TICK_INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.tick();

				if (model.hasChanged()) {
					model.notifyObservers();
				}
			}
		});

		timer.start();
	}


	public static final void main(String[] args) {
		Tetris tetris = new Tetris(10, 20, 32);

		JFrame controls = new JFrame("CONTROLS");
		ControlsView controlsView = new ControlsView();
		controlsView.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
		controls.setContentPane(controlsView);
		controlsView.setAlignmentY(TOP_ALIGNMENT);
		controls.setBackground(Color.DARK_GRAY);
		controls.pack();
		controls.setResizable(false);
		controls.setVisible(true);

		JFrame frame = new JFrame("Тетрис");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(tetris);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		controls.setLocationRelativeTo(frame);
		controls.setLocation(frame.getLocation().x+frame.getWidth()+5, frame.getLocation().y);
	}
}
