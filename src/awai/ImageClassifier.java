package awai;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageClassifier 
{
	final int TOL = 2000;
	ArrayList<HistTerrainPair> terrainHists;
	EmulatorFrame emulator;
	
	public ImageClassifier(EmulatorFrame e)
	{
		terrainHists = new ArrayList<HistTerrainPair>();
		emulator = e;
		loadTerrainDatabase();
	}
	
	public String imageWordMatch(BufferedImage img, int y, int x) throws IOException
	{
		BufferedImage recordImg;
		File dbFolder = new File(AWAI.baseFP + "\\Database\\words");
		if (dbFolder.listFiles() != null)
		{
		    for ( File fileEntry : dbFolder.listFiles()) 
		    {
				recordImg = ImageUtility.readImage(fileEntry);
				if(ImageUtility.compareImages(recordImg, img, 100))
				{
					return fileEntry.getName();
				}
		    }
		}
		updateWordDatabase(img, "REPLACEME" + y + "_" + x);
		return "UNKNOWN";
	}
	
	public void updateWordDatabase(BufferedImage img, String word) throws IOException
	{
		String dbPath = AWAI.baseFP + "\\Database\\words\\";
		ImageUtility.storeImage(img, dbPath + word);
	}
	
	public Terrain whatIsTerrain(BufferedImage img, int y, int x) throws IOException, InterruptedException
	{
		Histogram unk = new Histogram(img);
		String t;
		BufferedImage[] inspects;
		for (HistTerrainPair hTP : terrainHists)
		{
			if (unk.isSimilar(hTP.h, TOL))
			{
				System.out.println("Found " + hTP.t.ter.toString());
				return hTP.t;
			}
		}
		//System.out.println("Unknown tile found");
		inspects = emulator.inspect(y, x);
		t = imageWordMatch(inspects[0], y, x);
		t = t.substring(0, t.indexOf("_"));
		System.out.println("Discovered |" + t + "|");
		HistTerrainPair temp;
		try {
			temp = new HistTerrainPair(unk, new Terrain(Terrain.Type.valueOf(t)));
		}
		catch (IllegalArgumentException e)
		{
			temp = new HistTerrainPair(unk, new Terrain(Terrain.Type.UNKNOWN));
		}
		terrainHists.add(temp);
		updateTerrainDatabase(temp);
		return temp.t;
	}
	
	public void updateTerrainDatabase(HistTerrainPair pair) throws IOException
	{
		String dbPath = AWAI.baseFP + "\\Database\\terrain.db";
		BufferedWriter writer = new BufferedWriter(new FileWriter(dbPath, true));
		writer.append(pair.h.toString() + ":" + pair.t.ter.toString() + "\n");
		writer.close();
	}
	
	private void loadTerrainDatabase()
	{
		BufferedReader reader;
		String dbPath = AWAI.baseFP + "\\Database\\terrain.db";
		String line, his, ter;
		int i = 0;
		try {
			reader = new BufferedReader(new FileReader(dbPath));
			line = reader.readLine();
			while (line != null) {
				i = line.indexOf(':');
				his = line.substring(0, i);
				ter = line.substring(i+1);
				terrainHists.add(new HistTerrainPair(
						new Histogram(his),
						new Terrain(Terrain.Type.valueOf(ter))));
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Terrain DB did not load properly");
		}
		System.out.println("Read " + terrainHists.size() + " records from DB");
	}
	
	private class HistTerrainPair
	{
		public Histogram h;
		public Terrain t;
		
		public HistTerrainPair(Histogram hin, Terrain tin)
		{
			h = hin; t = tin;
		}
	}
}
