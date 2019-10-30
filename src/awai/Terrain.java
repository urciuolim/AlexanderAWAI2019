package awai;

public class Terrain 
{

	public static enum Type
	{
		SEA, PLAIN, WOOD, ROAD, BRDG, BASE, HQ, MT, CITY;
	}
	int def;
	Type ter;
	
	public Terrain(Terrain.Type t)
	{
		ter = t;
		def = terrainToDef(t);
	}
	
	public String toString()
	{
		return ter.toString() + ": Def: " + def;
	}
	
	public static int terrainToDef(Terrain.Type t)
	{
		switch (t)
		{
		case PLAIN:
			return 1;
		case WOOD:
			return 2;
		case BASE:
		case CITY:
			return 3;
		case HQ:
		case MT:
			return 4;
		default:
			return 0;
		}
	}
}
