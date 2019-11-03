package awai;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AWAI {

	final static int tileWidth = 15;
	final static int tileHeight = 10;
	static String baseFP = "";
	
	public static void main(String[] args) throws IOException, AWTException, InterruptedException 
	{
		baseFP = new File( "." ).getCanonicalPath();
		String emFP = baseFP + "\\VisualBoyAdvance\\VBA.exe";
		EmulatorFrame emulator = new EmulatorFrame (emFP);
		BufferedImage ps = emulator.printscreen(false);
		ImageIO.write(ps, "jpg", new File(baseFP + "\\Screenshots\\screen.jpg"));
		BufferedImage[][] grid = emulator.printGrid();
		GameState GS = new GameState(grid, emulator);
		GS.printGameState();
	}
}
