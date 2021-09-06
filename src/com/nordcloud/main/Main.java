package com.nordcloud.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nordcloud.resources.Point;

public class Main {

	final static Pattern LINK_STATION_PATTERN = Pattern.compile("\\(\\d+,\\d+,\\d+\\)");
	final static Pattern DEVICE_PATTERN = Pattern.compile("\\(\\d+,\\d+\\)");
	
	public static void main(String[] args) {
		
		String result = "";
		
		try
		{
			if(args != null && args.length > 0)
			{
				List<Point> devicesPositions = new ArrayList<Point>();
				Map<Point,Integer> linkstations = new HashMap<Point,Integer>();
				for(int argsIndex = 0; argsIndex < args.length; argsIndex++)
				{
					String arg = args[argsIndex];
					if(LINK_STATION_PATTERN.matcher(arg).matches())
					{
						String[] values = arg.substring(1,arg.length()-1).split(","); //remove ()
						Point linkstationPosition = new Point(Integer.valueOf(values[0]), Integer.valueOf(values[1]));
						if(!linkstations.containsKey(linkstationPosition))
						{
							linkstations.put(linkstationPosition, Integer.valueOf(values[2]));
						}
						
					} else if(DEVICE_PATTERN.matcher(arg).matches())
					{
						String[] values = arg.substring(1,arg.length()-1).split(","); //remove ()
						devicesPositions.add(new Point(Integer.valueOf(values[0]), Integer.valueOf(values[1])));
					} else 
					{
						throw new Exception("Invalid format provided in the arguments: " + arg);
					}
					
				}
				
				if(devicesPositions != null && devicesPositions.size() > 0)
				{
					if(linkstations != null && linkstations.size() > 0)
					{
						result = Engine.getStationLink(linkstations, devicesPositions);
					} else
					{
						result = Engine.getStationLink(devicesPositions);
					}
				} else {
					throw new Exception("No device positions were provided!");
				}
				
			} else 
			{
				throw new Exception("No device positions were provided!");
			}
		} catch(Exception ex)
		{
			result = ex.getMessage();
		}
		
		System.out.println(result);
	
	}

}
