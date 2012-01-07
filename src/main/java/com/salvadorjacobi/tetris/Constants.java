package com.salvadorjacobi.tetris;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jdesktop.swingx.image.ColorTintFilter;

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

	public static final Map<Tetrimino.Shape, int[][][]> tetriminoShapes;
	static {
		Map<Tetrimino.Shape, int[][][]> map = new HashMap<Tetrimino.Shape, int[][][]>();

		map.put(Tetrimino.Shape.I, new int[][][] {
				{
					{0, 0, 0, 0},
					{1, 1, 1, 1},
					{0, 0, 0, 0},
					{0, 0, 0, 0}
				}, {
					{0, 1, 0, 0},
					{0, 1, 0, 0},
					{0, 1, 0, 0},
					{0, 1, 0, 0}
				}, {
					{0, 0, 0, 0},
					{0, 0, 0, 0},
					{1, 1, 1, 1},
					{0, 0, 0, 0}
				}, {
					{0, 0, 1, 0},
					{0, 0, 1, 0},
					{0, 0, 1, 0},
					{0, 0, 1, 0}
				}
		});
		map.put(Tetrimino.Shape.J, new int[][][] {
				{
					{0, 0, 0, 0},
					{1, 1, 1, 0},
					{0, 0, 1, 0},
					{0, 0, 0, 0}
				}, {
					{0, 1, 1, 0},
					{0, 1, 0, 0},
					{0, 1, 0, 0},
					{0, 0, 0, 0}
				}, {
					{1, 0, 0, 0},
					{1, 1, 1, 0},
					{0, 0, 0, 0},
					{0, 0, 0, 0}
				}, {
					{0, 1, 0, 0},
					{0, 1, 0, 0},
					{1, 1, 0, 0},
					{0, 0, 0, 0}
				}
		});
		map.put(Tetrimino.Shape.L, new int[][][] {
				{
					{0, 0, 0, 0},
					{1, 1, 1, 0},
					{1, 0, 0, 0},
					{0, 0, 0, 0}
				}, {
					{0, 1, 0, 0},
					{0, 1, 0, 0},
					{0, 1, 1, 0},
					{0, 0, 0, 0}
				}, {
					{0, 0, 1, 0},
					{1, 1, 1, 0},
					{0, 0, 0, 0},
					{0, 0, 0, 0}
				}, {
					{1, 1, 0, 0},
					{0, 1, 0, 0},
					{0, 1, 0, 0},
					{0, 0, 0, 0}
				}
		});
		map.put(Tetrimino.Shape.O, new int[][][] {
				{
					{0, 0, 0, 0},
					{0, 1, 1, 0},
					{0, 1, 1, 0},
					{0, 0, 0, 0}
				}
		});
		map.put(Tetrimino.Shape.S, new int[][][] {
				{
					{0, 0, 0, 0},
					{0, 1, 1, 0},
					{1, 1, 0, 0},
					{0, 0, 0, 0}
				}, {
					{0, 1, 0, 0},
					{0, 1, 1, 0},
					{0, 0, 1, 0},
					{0, 0, 0, 0}
				}, {
					{1, 1, 0, 0},
					{0, 1, 1, 0},
					{0, 0, 0, 0},
					{0, 0, 0, 0}
				}, {
					{0, 1, 0, 0},
					{1, 1, 0, 0},
					{1, 0, 0, 0},
					{0, 0, 0, 0}
				}
		});
		map.put(Tetrimino.Shape.T, new int[][][] {
				{
					{0, 0, 0, 0},
					{1, 1, 1, 0},
					{0, 1, 0, 0},
					{0, 0, 0, 0}
				}, {
					{0, 1, 0, 0},
					{0, 1, 1, 0},
					{0, 1, 0, 0},
					{0, 0, 0, 0}
				}, {
					{0, 1, 0, 0},
					{1, 1, 1, 0},
					{0, 0, 0, 0},
					{0, 0, 0, 0}
				}, {
					{0, 1, 0, 0},
					{1, 1, 0, 0},
					{0, 1, 0, 0},
					{0, 0, 0, 0}
				}
		});
		map.put(Tetrimino.Shape.Z, new int[][][] {
				{
					{0, 0, 0, 0},
					{1, 1, 0, 0},
					{0, 1, 1, 0},
					{0, 0, 0, 0}
				}, {
					{0, 0, 1, 0},
					{0, 1, 1, 0},
					{0, 1, 0, 0},
					{0, 0, 0, 0}
				}, {
					{1, 1, 0, 0},
					{0, 1, 1, 0},
					{0, 0, 0, 0},
					{0, 0, 0, 0}
				}, {
					{0, 1, 0, 0},
					{1, 1, 0, 0},
					{1, 0, 0, 0},
					{0, 0, 0, 0}
				}
		});

		tetriminoShapes = Collections.unmodifiableMap(map);
	}

	public static final BufferedImage blockBaseImage;
	public static final Map<Tetrimino.Shape, BufferedImage> blockImages;
	static {
		Map<Tetrimino.Shape, BufferedImage> map = new HashMap<Tetrimino.Shape, BufferedImage>();

		BufferedImage baseImage = null;

		try {
			baseImage = ImageIO.read(Tetris.class.getResource("/block.png"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
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
<<<<<<< HEAD
		map.put("nyan cat", Applet.newAudioClip(Tetris.class.getResource("/nyan cat.mid")));
		map.put("rickroll", Applet.newAudioClip(Tetris.class.getResource("/rickroll.mid")));
=======
		map.put("clear", Applet.newAudioClip(Tetris.class.getResource("/clear.wav")));
>>>>>>> 92abc1b2c252aad30ccd02cb409925f8cdb0701b

		sounds = Collections.unmodifiableMap(map);
	}
}
