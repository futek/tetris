package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

@SuppressWarnings("serial")
public class Tetris extends JPanel implements ActionListener{
	public static GameModel model;
	public final GameController controller;

	public final MatrixView matrixView;

	public static JButton resetButton = new JButton("new game");
	public static JButton pauseButton = new JButton("pause");

	public Tetris(int width, int height, int scale) {
		model = new GameModel(width, height, scale);

		matrixView = new MatrixView(model);
		PreviewView previewView = new PreviewView(model);
		HoldView holdView = new HoldView(model);
		ScoreView scoreView = new ScoreView(model);

		resetButton.setActionCommand("new game");
		resetButton.addActionListener(this);
		pauseButton.setActionCommand("pause");
		pauseButton.addActionListener(this);

		controller = new GameController(model, matrixView, previewView, holdView, scoreView);

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
	}


	public static final void main(String[] args) {
		Tetris tetris = new Tetris(10, 20, 32);

		JFrame frame = new JFrame("Тетрис");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(tetris);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public static void reload(JButton button) {
		button.setEnabled(false);
		button.setEnabled(true);
	}

	public void actionPerformed(ActionEvent e) {
		if("new game".equals(e.getActionCommand())) {
			model.reset();
			pauseButton.setEnabled(false);
			reload(resetButton);
		}
		if("pause".equals(e.getActionCommand())) {
			if(matrixView.isEnabled()) {
				resetButton.setEnabled(false);
				matrixView.setEnabled(false);
				pauseButton.setText("resume");
			}else{
				resetButton.setEnabled(true);
				matrixView.setEnabled(true);
				pauseButton.setText("pause");

			}
		}
		reload(pauseButton);

	}
}
