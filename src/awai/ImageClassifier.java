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

import net.sourceforge.tess4j.*;

import awai.GameState.Terrain;

public class ImageClassifier 
{
	final int TOL = 2000;
	ArrayList<HistTerrainPair> terrainHists;
	Tesseract tess;
	EmulatorFrame emulator;
	
	public ImageClassifier(EmulatorFrame e)
	{
		terrainHists = new ArrayList<HistTerrainPair>();
		tess = new Tesseract();
		emulator = e;
		loadDatabase();
	}
	
	public Terrain whatIsTerrain(BufferedImage img, int y, int x) throws IOException, InterruptedException, TesseractException
	{
		Histogram unk = new Histogram(img);
		String in;
		String terrain;
		BufferedImage[][] inspects;
		for (HistTerrainPair hTP : terrainHists)
		{
			if (unk.isSimilar(hTP.h, TOL))
			{
				System.out.println("Found " + hTP.t.toString());
				return hTP.t;
			}
		}
		//System.out.println("Unknown tile found");
		inspects = emulator.inspect(y, x);
		ImageIO.write(inspects[0][0], "jpg", new File(AWAI.baseFP + "\\Screenshots\\terraininspect.jpg"));
		terrain = tess.doOCR(inspects[0][0]).toUpperCase().trim();
		if (terrain.equals("BA $¢")) terrain = "BASE";
		else if (terrain.equals("HO") || terrain.equals("HA")) terrain = "HQ";
		System.out.println("Discovered |" + terrain + "|");
		HistTerrainPair temp = new HistTerrainPair(unk, Terrain.valueOf(terrain));
		terrainHists.add(temp);
		//updateDatabase(temp);
		return temp.t;
	}
	
	public void updateDatabase(HistTerrainPair pair) throws IOException
	{
		String dbPath = AWAI.baseFP + "\\Database\\terrain.db";
		BufferedWriter writer = new BufferedWriter(new FileWriter(dbPath, true));
		writer.append(pair.h.toString() + ":" + pair.t.name() + "\n");
		writer.close();
	}
	
	private void loadDatabase()
	{
		BufferedReader reader;
		String dbPath = AWAI.baseFP + "\\Database\\terrain.db";
		String line;
		int i = 0;
		try {
			reader = new BufferedReader(new FileReader(dbPath));
			line = reader.readLine();
			while (line != null) {
				i = line.indexOf(':');
				terrainHists.add(new HistTerrainPair(
						new Histogram(line.substring(0,  i)),
						Terrain.valueOf(line.substring(i+1))));
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Img DB did not load properly");
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
