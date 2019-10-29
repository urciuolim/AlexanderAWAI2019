package awai;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.sourceforge.tess4j.TesseractException;

public class GameState 
{
	
	ImageClassifier IC;
	Terrain[][] gameBoard;
	EmulatorFrame emulator;
	
	public GameState(BufferedImage[][] terrainGrid, EmulatorFrame e) throws IOException, InterruptedException, TesseractException
	{
		emulator = e;
		IC = new ImageClassifier(emulator);
		gameBoard = new Terrain[AWAI.tileHeight][AWAI.tileWidth];
		for (int i = 0; i < terrainGrid.length; i++)
		{
			for (int j = 0; j < terrainGrid[i].length; j++)
			{
				System.out.print("Looking at (" + i + "," + j + "), ");
				gameBoard[i][j] = IC.whatIsTerrain(terrainGrid[i][j], i, j);
			}
		}
	}
	
	public void printGameState()
	{
		for (Terrain[] column : gameBoard)
		{
			for(Terrain tile : column)
			{
				System.out.print(tile.ter.toString() + "\t");
			}
			System.out.println();
		}
	}
	
	public Terrain getTile(int y, int x)
	{
		return gameBoard[y][x];
	}
}
