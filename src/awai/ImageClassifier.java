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
		t = correctTerrainOCR(tess.doOCR(inspects[0]));
		System.out.println("Discovered |" + t + "|");
		HistTerrainPair temp = new HistTerrainPair(unk, new Terrain(Terrain.Type.valueOf(t)));
		terrainHists.add(temp);
		updateDatabase(temp);
		return temp.t;
	}
	
	private String correctTerrainOCR(String text)
	{
		String ret = text.toUpperCase().trim();
		if (ret.equals("BA $¢")) ret = "BASE";
		else if (ret.equals("HO") || ret.equals("HA")) ret = "HQ";
		return ret;
	}
	
	public void updateDatabase(HistTerrainPair pair) throws IOException
	{
		String dbPath = AWAI.baseFP + "\\Database\\terrain.db";
		BufferedWriter writer = new BufferedWriter(new FileWriter(dbPath, true));
		writer.append(pair.h.toString() + ":" + pair.t.ter.toString() + "\n");
		writer.close();
	}
	
	private void loadDatabase()
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
				line = line.substring(i+1);
				i = line.indexOf(':');
				ter = line.substring(0, i);
				terrainHists.add(new HistTerrainPair(
						new Histogram(his),
						new Terrain(Terrain.Type.valueOf(ter))));
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
