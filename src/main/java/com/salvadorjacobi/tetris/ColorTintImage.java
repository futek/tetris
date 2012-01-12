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

		for (int x = 0; x < dest.getWidth(); x++) {
			for (int y = 0; y < dest.getHeight(); y++) {
				int rgb = dest.getRGB(x, y);
				dest.setRGB(x, y, rgb | tintColor.getRGB());
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
