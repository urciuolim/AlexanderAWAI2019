package awai;

public class Terrain 
{

	public static enum Type
	{
		SEA, PLAIN, WOOD, ROAD, BRDG, BASE, HQ, MT, CITY, UNKNOWN;
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
		case SEA:
		case ROAD:
		case BRDG:
		default:
			return 0;
		}
	}
	
	public int movementCost(Unit.Type u)
	{
		switch (ter)
		{
		case PLAIN:
		{
			switch (u)
			{
			case INFANTRY:
			case MECH:
			case TANK:
			case MDTANK:
			case APC:
			case ARTLRY:
			case AAIR:
				return 1;
			case RECON:
			case ROCKETS:
			case MISSILES:
				return 2;
			default:
				return 100;
			}
		}
		case WOOD:
			switch (u)
			{
			case INFANTRY:
			case MECH:
				return 1;
			case TANK:
			case MDTANK:
			case APC:
			case ARTLRY:
			case AAIR:
				return 2;
			case RECON:
			case ROCKETS:
			case MISSILES:
				return 3;
			default:
				return 100;
			}
		case ROAD:
		case BRDG:
		case BASE:
		case CITY:
		case HQ:
			switch (u)
			{
			case INFANTRY:
			case MECH:
			case TANK:
			case MDTANK:
			case APC:
			case ARTLRY:
			case AAIR:
			case RECON:
			case ROCKETS:
			case MISSILES:
				return 1;
			default:
				return 100;
			}
		case MT:
			switch (u)
			{
			case INFANTRY:
				return 2;
			case MECH:
				return 1;
			case TANK:
			case MDTANK:
			case APC:
			case ARTLRY:
			case AAIR:
			case RECON:
			case ROCKETS:
			case MISSILES:
			default:
				return 100;
			}
		case SEA:
			/* Add this later with sea and air units
			switch (u)
			{
			default:
				return 100;
			}
			*/
		default:
			return 100;
		}
	}
}
