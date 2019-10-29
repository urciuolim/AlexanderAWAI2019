package awai;

public class Terrain 
{

	public static enum Type
	{
		SEA, PLAIN, WOOD, ROAD, BRDG, BASE, HQ, MT, CITY;
	}
	int def;
	Type ter;
	
	public Terrain(Terrain.Type t, int d)
	{
		ter = t;
		def = d;
	}
	
}
