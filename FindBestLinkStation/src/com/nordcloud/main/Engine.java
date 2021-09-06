package com.nordcloud.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nordcloud.resources.Device;
import com.nordcloud.resources.Linkstation;
import com.nordcloud.resources.Point;

public abstract class Engine {
	
	private static List<Linkstation> linkStations = new ArrayList<Linkstation>()
	{
		{
			add(new Linkstation(new Point(0,0), 10));
			add(new Linkstation(new Point(20,20), 5));
			add(new Linkstation(new Point(10,0), 12));
		}
	};
	
	
	public static String getStationLink(Map<Point, Integer> newLinkStations, Point devicePosition)
	{
		StringBuilder result = new StringBuilder("");
		
		//Validate input data
		if((devicePosition != null) && (newLinkStations != null && newLinkStations.size() > 0))
		{
			//Create Device objects
			Device device = new Device(devicePosition);
			
			//Add Link station objects
			for(Map.Entry<Point, Integer> newLinkStation : newLinkStations.entrySet())
			{
				if(newLinkStation.getValue() != null)
				{
					if(!linkStations.stream().anyMatch(linkstation -> linkstation.equals(newLinkStation.getKey())))
					{
						linkStations.add(new Linkstation(newLinkStation.getKey(),newLinkStation.getValue()));
					} else
					{
						result.append("\r\nLink station ("+newLinkStation.getKey().getPositionX()+","+newLinkStation.getKey().getPositionY()+") already exists!");
					}
				}
			}
			
			Linkstation mostPowerfullStation = getMostPowerfullStation(linkStations,device);
			if(mostPowerfullStation != null)
			{
				result.append("\r\nBest link station for point " + device.getPosition().getPositionX() + "," + device.getPosition().getPositionY() + " is "
			+ mostPowerfullStation.getPosition().getPositionX() + "," + mostPowerfullStation.getPosition().getPositionY() + " with power " + mostPowerfullStation.getPower());
			} else {
				result.append("\r\nNo link station within reach for point " +device.getPosition().getPositionX()+ "," +device.getPosition().getPositionY());
			}
		} else {
			result.append("No link station within reach for point x,y");
		}
			
			
		
		return result.toString();
	}
	public static String getStationLink(Map<Point, Integer> newLinkStations, List<Point> devicesPosition)
	{
		StringBuilder result = new StringBuilder("");
		
		if(devicesPosition != null && devicesPosition.size() > 0 && newLinkStations != null && newLinkStations.size() > 0)
		{
			//Add Link station objects
			for(Map.Entry<Point, Integer> newLinkStation : newLinkStations.entrySet())
			{
				if(newLinkStation.getValue() != null)
				{
					if(!linkStations.stream().anyMatch(linkstation -> linkstation.equals(newLinkStation.getKey())))
					{
						linkStations.add(new Linkstation(newLinkStation.getKey(),newLinkStation.getValue()));
					} else
					{
						result.append("\r\nLink station ("+newLinkStation.getKey().getPositionX()+","+newLinkStation.getKey().getPositionY()+") already exists!");
					}
				}
			}
			
			for(Point devicePosition : devicesPosition)
			{
				//Create Device objects
				Device device = new Device(devicePosition);
				
				Linkstation mostPowerfullStation = getMostPowerfullStation(linkStations,device);
				if(mostPowerfullStation != null)
				{
					result.append("\r\nBest link station for point " + device.getPosition().getPositionX() + "," + device.getPosition().getPositionY() + " is "
				+ mostPowerfullStation.getPosition().getPositionX() + "," + mostPowerfullStation.getPosition().getPositionY() + " with power " + mostPowerfullStation.getPower());
				} else {
					result.append("\r\nNo link station within reach for point " +device.getPosition().getPositionX()+ "," +device.getPosition().getPositionY());
				}
			}
		} else {
			result.append("No link station within reach for point x,y");
		}
		
		return result.toString();
	}
	public static String getStationLink(Point devicePosition)
	{
		StringBuilder result = new StringBuilder("");
		
		if(devicePosition != null)
		{
			Device device = new Device(devicePosition);
			Linkstation mostPowerfullStation = getMostPowerfullStation(linkStations,device);
			if(mostPowerfullStation != null)
			{
				result.append("\r\nBest link station for point " + device.getPosition().getPositionX() + "," + device.getPosition().getPositionY() + " is "
			+ mostPowerfullStation.getPosition().getPositionX() + "," + mostPowerfullStation.getPosition().getPositionY() + " with power " + mostPowerfullStation.getPower());
			} else {
				result.append("\r\nNo link station within reach for point " +device.getPosition().getPositionX()+ "," +device.getPosition().getPositionY());
			}
		} else {
			result.append("No link station within reach for point x,y");
		}
		
		return result.toString();
	}
	
	public static String getStationLink(List<Point> devicesPosition)
	{
		StringBuilder result = new StringBuilder("");
		
		if(devicesPosition != null && devicesPosition.size() > 0)
		{
			for(Point devicePosition : devicesPosition)
			{
				Device device = new Device(devicePosition);
				Linkstation mostPowerfullStation = getMostPowerfullStation(linkStations,device);
				if(mostPowerfullStation != null)
				{
					result.append("\r\nBest link station for point " + device.getPosition().getPositionX() + "," + device.getPosition().getPositionY() + " is "
				+ mostPowerfullStation.getPosition().getPositionX() + "," + mostPowerfullStation.getPosition().getPositionY() + " with power " + mostPowerfullStation.getPower());
				} else {
					result.append("\r\nNo link station within reach for point " +device.getPosition().getPositionX()+ "," +device.getPosition().getPositionY());
				}
			}
		} else {
			result.append("No link station within reach for point x,y");
		}
		
		return result.toString();
	}
	
	
	
	private static Linkstation getMostPowerfullStation(List<Linkstation> currentLinkStations, Device device)
	{
		Linkstation mostPowerfullStation = null;
		
		if(currentLinkStations != null && currentLinkStations.size() > 0 && device != null)
		{
			int currentPower = 0;
			for(Linkstation currentLinkStation : currentLinkStations)
			{
				int linkStationPower = 0;
				linkStationPower = currentLinkStation.calculatePower(device);
				
				if(currentPower < linkStationPower)
				{
					mostPowerfullStation = currentLinkStation;
					currentPower = linkStationPower;
					
				}
			}
			
			if(currentPower == 0)
				mostPowerfullStation = null;
		}
		
		return mostPowerfullStation;
	}
}
