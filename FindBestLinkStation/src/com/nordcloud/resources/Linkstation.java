package com.nordcloud.resources;

public class Linkstation extends Entity{

	private int reach;
	private int power;
	public Linkstation(int positionX, int positionY, int reach) {
		super(positionX,positionY);
		this.reach = reach;
		this.power = 0;
	}
	public Linkstation(Point p, int reach) {
		super(p);
		this.reach = reach;
		this.power = 0;
	}
	public int getReach() {
		return reach;
	}
	public void setReach(int reach) {
		this.reach = reach;
	}
	public int getPower() {
		return power;
	}
	
	@Override
    public int hashCode() {
        return super.position.hashCode();
    }
 
    //Compare x and y
    @Override
    public boolean equals(Object obj) {
                
        return super.position.equals(obj);
    }
	
	public int calculatePower(Device device) {
		double deviceDistance = Math.sqrt(Math.pow((double)super.position.getPositionX()-(double)device.getPosition().getPositionX(),2.0) + Math.pow((double)this.position.getPositionY()-(double)device.getPosition().getPositionY(),2.0));
		
		if(deviceDistance > this.reach)
		{
			this.power = 0;
		} else 
		{
			this.power = (int) Math.pow((double)reach - deviceDistance, 2.0);
		}
		
		return this.power;
	}	
}
