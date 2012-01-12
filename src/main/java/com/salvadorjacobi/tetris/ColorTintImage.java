package com.salvadorjacobi.tetris;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

public class ColorTintImage implements BufferedImageOp {
	public final Color tintColor;
	public final float mixRatio;

	public ColorTintImage(Color tintColor, float mixRatio) {
		this.tintColor = tintColor;
		this.mixRatio = mixRatio;
	}

	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
		return null;
	}

	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

		for (int x = 0; x < src.getWidth(); x++) {
			for (int y = 0; y < src.getHeight(); y++) {
				Color color = new Color(src.getRGB(x, y));

				int r1 = color.getRed();
				int r2 = tintColor.getRed();
				int r3 = (int) (r1 + (r2 - r1) * mixRatio);

				int g1 = color.getGreen();
				int g2 = tintColor.getGreen();
				int g3 = (int) (g1 + (g2 - g1) * mixRatio);

				int b1 = color.getBlue();
				int b2 = tintColor.getBlue();
				int b3 = (int) (b1 + (b2 - b1) * mixRatio);

				Color newColor = new Color(r3, g3, b3);

				dest.setRGB(x, y, newColor.getRGB());
			}
		}

		return dest;
	}

	public Rectangle2D getBounds2D(BufferedImage src) {
		return null;
	}

	public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
		return null;
	}

	public RenderingHints getRenderingHints() {
		return null;
	}
}
