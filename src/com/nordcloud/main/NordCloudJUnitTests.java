package com.nordcloud.main;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.nordcloud.resources.Point;

class NordCloudJUnitTests {

	@Test
	@DisplayName("New Link Station with device test [SUCCESS]")
	void testGetStationLinkMapOfPointIntegerPoint() {
		String result = Engine.getStationLink(new HashMap<Point,Integer>()
		{
			{
				put(new Point(3,2),23);
			}
		}, new Point(1,2));
		//System.out.println(result);
		assertTrue(result.contains("3,2 with power 441"));
	}

	@Test
	@DisplayName("Just device test")
	void testGetStationLinkPoint() {
		Map<Point,String> assertedResults = new HashMap<Point,String>()
		{
			{
				put(new Point(0,0),"0,0 with power 100");
				put(new Point(100,100),"No link station within reach");
				put(new Point(15,10),"No link station within reach");
				put(new Point(18,18),"20,20 with power 4");
			}
		};
		
		List<Point> devicesPoints = new ArrayList<Point>(assertedResults.keySet());
		//System.out.println(Engine.getStationLink(devicesPoints));
		for(Map.Entry<Point, String> assertedResult : assertedResults.entrySet())
		{
			assertTrue(Engine.getStationLink(assertedResult.getKey()).contains(assertedResult.getValue()));
		}
	}

}
