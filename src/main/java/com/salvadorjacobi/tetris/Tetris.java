package com.salvadorjacobi.tetris;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

public class Tetris extends JPanel implements Runnable {
	private boolean running;

	public final GameModel model;
	public final GameController controller;

	public final WellView wellView;

	public Tetris(int width, int height, int scale) {
		model = new GameModel(width, height, scale);

		wellView = new WellView(model);
		PreviewView previewView = new PreviewView(model);
		HoldView holdView = new HoldView(model);
		ScoreView scoreView = new ScoreView(model);

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
						.addComponent(holdView)
						)
				);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(wellView)
						.addGroup(layout.createSequentialGroup()
								.addComponent(previewView)
								.addComponent(scoreView)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(holdView)
								)
						)
				);
	}

	public void start() {
		running = true;
		(new Thread(this)).start();
	}

	public void stop() {
		running = false;
	}

	// Not in use.. yet (or not)
	public void run() {
		long t = 0;
		long dt = 10000000;
		long maxFrameTime = 250000000;

		long currentTime = System.nanoTime();
		long accumulator = dt;

		while (running) {
			long newTime = System.nanoTime();
			long frameTime = newTime - currentTime;

			if (frameTime > maxFrameTime) {
				frameTime = maxFrameTime;
			}

			currentTime = newTime;
			accumulator += frameTime;

			while (accumulator >= dt) {
				update(t, dt);

				t += dt;
				accumulator -= dt;
			}

			double alpha = accumulator / dt;

			render(alpha);
		}
	}

	public void update(long t, long dt) {
		//model.update();
	}

	public void render(double alpha) {
		wellView.repaint();
	}

	public static final void main(String[] args) {
		Tetris stupid = new Tetris(10, 20, 32);

		JFrame frame = new JFrame("Tetris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(stupid);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		//frame.run();
	}
}
