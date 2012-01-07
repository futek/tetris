package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

public class Tetris extends JPanel {
	public static GameModel model;
	public final GameController controller;

	public final WellView wellView;
	
	public static JButton resetButton = new JButton("reset");

	public Tetris(int width, int height, int scale) {
		model = new GameModel(width, height, scale);

		wellView = new WellView(model);
		PreviewView previewView = new PreviewView(model);
		HoldView holdView = new HoldView(model);
		ScoreView scoreView = new ScoreView(model);
		
		resetButton.setActionCommand("disable");

		controller = new GameController(model, wellView, previewView, holdView, scoreView);

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(wellView)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(previewView)
						.addComponent(scoreView)
						.addComponent(resetButton)
						.addComponent(holdView)
						)
				);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(wellView)
						.addGroup(layout.createSequentialGroup()
								.addComponent(previewView)
								.addComponent(scoreView)
								.addComponent(resetButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(holdView)
								)
						)
				);

		this.setBackground(Color.DARK_GRAY);
	}
	
	
	public static JButton getButton() {
		return resetButton;
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
}
