package com.salvadorjacobi.tetris;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Constants {
	public static final Map<Tetrimino.Shape, Color> blockColors;
	static {
		Map<Tetrimino.Shape, Color> map = new HashMap<Tetrimino.Shape, Color>();

		map.put(Tetrimino.Shape.I, Color.decode("#00FFFF"));
		map.put(Tetrimino.Shape.J, Color.decode("#0000FF"));
		map.put(Tetrimino.Shape.L, Color.decode("#FF8800"));
		map.put(Tetrimino.Shape.O, Color.decode("#FFFF00"));
		map.put(Tetrimino.Shape.S, Color.decode("#00FF00"));
		map.put(Tetrimino.Shape.T, Color.decode("#8800FF"));
		map.put(Tetrimino.Shape.Z, Color.decode("#FF0000"));

		blockColors = Collections.unmodifiableMap(map);
	}

	public static final Map<Tetrimino.Shape, int[][][]> trueRotation;
	static {
		Map<Tetrimino.Shape, int[][][]> map = new HashMap<Tetrimino.Shape, int[][][]>();

		map.put(Tetrimino.Shape.I, new int[][][] {
				{
					{0,0,0,0,0},
					{0,0,0,0,0},
					{0,1,1,1,1},
					{0,0,0,0,0},
					{0,0,0,0,0}
				}, {
					{0,0,0,0,0},
					{0,0,1,0,0},
					{0,0,1,0,0},
					{0,0,1,0,0},
					{0,0,1,0,0}
				}, {
					{0,0,0,0,0},
					{0,0,0,0,0},
					{1,1,1,1,0},
					{0,0,0,0,0},
					{0,0,0,0,0}
				}, {
					{0,0,1,0,0},
					{0,0,1,0,0},
					{0,0,1,0,0},
					{0,0,1,0,0},
					{0,0,0,0,0}
				}
		});
		map.put(Tetrimino.Shape.J, new int[][][] {
				{
					{1,0,0},
					{1,1,1},
					{0,0,0}
				}, {
					{0,1,1},
					{0,1,0},
					{0,1,0}
				}, {
					{0,0,0},
					{1,1,1},
					{0,0,1}
				}, {
					{0,1,0},
					{0,1,0},
					{1,1,0}
				}
		});
		map.put(Tetrimino.Shape.L, new int[][][] {
				{
					{0,0,1},
					{1,1,1},
					{0,0,0}
				}, {
					{0,1,0},
					{0,1,0},
					{0,1,1}
				}, {
					{0,0,0},
					{1,1,1},
					{1,0,0}
				}, {
					{1,1,0},
					{0,1,0},
					{0,1,0}
				}
		});
		map.put(Tetrimino.Shape.O, new int[][][] {
				{
					{0,1,1},
					{0,1,1},
					{0,0,0}
				}, {
					{0,0,0},
					{0,1,1},
					{0,1,1}
				}, {
					{0,0,0},
					{1,1,0},
					{1,1,0}
				}, {
					{1,1,0},
					{1,1,0},
					{0,0,0}
				}
		});
		map.put(Tetrimino.Shape.S, new int[][][] {
				{
					{0,1,1},
					{1,1,0},
					{0,0,0}
				}, {
					{0,1,0},
					{0,1,1},
					{0,0,1}
				}, {
					{0,0,0},
					{0,1,1},
					{1,1,0}
				}, {
					{1,0,0},
					{1,1,0},
					{0,1,0}
				}
		});
		map.put(Tetrimino.Shape.T, new int[][][] {
				{
					{0,1,0},
					{1,1,1},
					{0,0,0}
				}, {
					{0,1,0},
					{0,1,1},
					{0,1,0}
				}, {
					{0,0,0},
					{1,1,1},
					{0,1,0}
				}, {
					{0,1,0},
					{1,1,0},
					{0,1,0}
				}
		});
		map.put(Tetrimino.Shape.Z, new int[][][] {
				{
					{1,1,0},
					{0,1,1},
					{0,0,0}
				}, {
					{0,0,1},
					{0,1,1},
					{0,1,0}
				}, {
					{0,0,0},
					{1,1,0},
					{0,1,1}
				}, {
					{0,1,0},
					{1,1,0},
					{1,0,0}
				}
		});

		trueRotation = Collections.unmodifiableMap(map);
	}

	public static final Map<Tetrimino.Shape, int[][][]> offsetData;
	static {
		Map<Tetrimino.Shape, int[][][]> map = new HashMap<Tetrimino.Shape, int[][][]>();

		int[][][] JLSTZ = new int[][][] {
				{ { 0,  0}, { 0,  0}, { 0,  0}, { 0,  0}, { 0,  0} },
				{ { 0,  0}, {+1,  0}, {+1, +1}, { 0, -2}, {+1, -2} },
				{ { 0,  0}, { 0,  0}, { 0,  0}, { 0,  0}, { 0,  0} },
				{ { 0,  0}, {-1,  0}, {-1, +1}, { 0, -2}, {-1, -2} }
		};

		int[][][] I = new int[][][] {
				{ { 0,  0}, {-1,  0}, {+2,  0}, {-1,  0}, {+2,  0} },
				{ {-1,  0}, { 0,  0}, { 0,  0}, { 0, -1}, { 0, +2} },
				{ {-1, -1}, {+1, -1}, {-2, -1}, {+1,  0}, {-2,  0} },
				{ { 0, -1}, { 0, -1}, { 0, -1}, { 0, +1}, { 0, -2} }
		};

		int[][][] O = new int[][][] {
				{ { 0,  0} },
				{ { 0, +1} },
				{ {-1, +1} },
				{ {-1,  0} }
		};

		map.put(Tetrimino.Shape.I, I);
		map.put(Tetrimino.Shape.J, JLSTZ);
		map.put(Tetrimino.Shape.L, JLSTZ);
		map.put(Tetrimino.Shape.O, O);
		map.put(Tetrimino.Shape.S, JLSTZ);
		map.put(Tetrimino.Shape.T, JLSTZ);
		map.put(Tetrimino.Shape.Z, JLSTZ);

		offsetData = Collections.unmodifiableMap(map);
	}

	public static final BufferedImage blockBaseImage;
	public static final Map<Tetrimino.Shape, BufferedImage> blockImages;
	static {
		Map<Tetrimino.Shape, BufferedImage> map = new HashMap<Tetrimino.Shape, BufferedImage>();

		BufferedImage baseImage = null;

		try {
			baseImage = ImageIO.read(Tetris.class.getResource("/block.png"));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		blockBaseImage = baseImage;

		for (Map.Entry<Tetrimino.Shape, Color> entry : blockColors.entrySet()) {
			Tetrimino.Shape shape = entry.getKey();
			Color color = entry.getValue();

			BufferedImage tintedImage = null;
			BufferedImageOp op = new ColorTintFilter(color, 0.5f);
			tintedImage = op.filter(baseImage, null);

			map.put(shape, tintedImage);
		}

		blockImages = Collections.unmodifiableMap(map);
	}

	public static final Map<String, AudioClip> sounds;
	static {
		Map<String, AudioClip> map = new HashMap<String, AudioClip>();

		map.put("rotate", Applet.newAudioClip(Tetris.class.getResource("/rotate.wav")));
		map.put("drop", Applet.newAudioClip(Tetris.class.getResource("/drop.wav")));
		map.put("clear", Applet.newAudioClip(Tetris.class.getResource("/clear.wav")));
		map.put("denied", Applet.newAudioClip(Tetris.class.getResource("/denied.wav")));
		map.put("lock", Applet.newAudioClip(Tetris.class.getResource("/lock.wav")));

		sounds = Collections.unmodifiableMap(map);
	}

	public static final Font baseFont;
	public static final Font messageFont;
	static {
		Font font = null;
		InputStream stream = Tetris.class.getResourceAsStream("/acknowtt.ttf");

		try {
			font = Font.createFont(Font.TRUETYPE_FONT, stream);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		baseFont = font.deriveFont(20f);
		messageFont = font.deriveFont(60f);
	}
}
