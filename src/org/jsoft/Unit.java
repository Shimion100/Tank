package org.jsoft;

import java.awt.image.BufferedImage;

abstract public class Unit {
	//×ø±ê
	int x;
	int y;
	
	//ÏÖÊµÍ¼Æ¬
	BufferedImage showImage = null;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public BufferedImage getShowImage() {
		return showImage;
	}
	public void setShowImage(BufferedImage showImage) {
		this.showImage = showImage;
	}
	
	abstract void draw();
}
