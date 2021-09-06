package com.nordcloud.resources;

public abstract class Entity {
	protected Point position;
	public Entity(int positionX, int positionY) {
		super();
		this.position = new Point(positionX,positionY);
	}
	public Entity(Point p) {
		super();
		this.position = p;
	}
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point p) {
		this.position = p;
	}
}
