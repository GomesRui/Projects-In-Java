package com.nordcloud.resources;

public class Point {
	private int positionX;
	private int positionY;
	public Point(int positionX, int positionY) {
		super();
		this.positionX = positionX;
		this.positionY = positionY;
	}
	public int getPositionX() {
		return positionX;
	}
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
    
    @Override
    public int hashCode() {
    	String hashCodeString = "("+positionX+","+positionY+")";
        return hashCodeString.hashCode();
    }
 
    //Compare x and y
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (positionX != other.getPositionX() && positionY != other.getPositionY())
            return false;
        
        return true;
    }
}
