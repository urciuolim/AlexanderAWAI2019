package awai;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class EmulatorFrame 
{
	// process that contains emulator
	Process emulator;
	// allows simulated keyboard and mouse input
	Robot robot;
	
	// Below rectangles correspond to VBA emulator window at position
	// windowX=437, windowY=66, which are settings in vba.ini
	// game window is the rectangle used by robot.mouseMove relative to emulator window
	final Rectangle gameWindow = new Rectangle(445, 67, 481, 371);
	// osk window is the rectangle used by robot.mouseMove relative to on screen keyboard
	final Rectangle oskWindow = new Rectangle(148, 465, 1080, 340);
	// screenshot window is rec used by robot screenshot to get entire emulator window
	final Rectangle screenshotWindow = new Rectangle(890, 135, 960, 740);
	// screenshot screen is rec used by robot screenshot to get only emulator game screen
	final Rectangle screenshotScreen = new Rectangle(890, 235, 960, 640);
	
	Point cursor;
	boolean isTileInfoLeft;
	
	int d = 0; // debugging counter

	public EmulatorFrame(String filepath) throws IOException, AWTException, InterruptedException
	{
		// starts emulator
		emulator = Runtime.getRuntime ().exec (filepath);
		robot = new Robot();
		// causes robot to pause
		robot.delay(1000);
		openAdvanceWars();
		// Open windows on screen keyboard
		Runtime.getRuntime().exec("cmd /c osk");
		robot.delay(5000);
		cursor = new Point(0,0);
		resetCursor();
	}
	
	public BufferedImage[] inspect(int y, int x) throws InterruptedException, IOException
	{
		moveCursor(y, x);
		return printTileInfo();
	}
	
	public void moveCursor(int y, int x) throws InterruptedException
	{
		int xincrement = 0;
		if (x != cursor.x) xincrement = (x - cursor.x)/Math.abs((x-cursor.x));
		int yincrement = 0;
		if (y != cursor.y) yincrement = (y - cursor.y)/Math.abs((y-cursor.y));
		for (int i = cursor.y; i != y; i += yincrement)
			if (yincrement > 0) down(50); else up(50);
		for (int i = cursor.x; i != x; i += xincrement)
			if (xincrement > 0) right(50); else left(50);
		cursor.setLocation(x, y);
		if (cursor.y > 3)
		{
			if (cursor.x >= 8) isTileInfoLeft = true;
			else if (cursor.x <= 7) isTileInfoLeft = false;
		}
		System.out.println("(" + cursor.y + "," + cursor.x + "): " + isTileInfoLeft);
	}
	
	public void resetCursor() throws InterruptedException
	{
		for (int i = 0; i < AWAI.tileWidth; i++)
			right(50);
		for (int i = 0; i < AWAI.tileHeight; i++)
			down(50);
		for (int i = 0; i < AWAI.tileWidth; i++)
			left(50);
		for (int i = 0; i < AWAI.tileHeight; i++)
			up(50);
		cursor.setLocation(0, 0);
	}
	
	/**
	 * Essentially a script that opens up the emulator and loads the start point save file
	 * @throws InterruptedException
	 */
	private void openAdvanceWars() throws InterruptedException
	{
	    robot.mouseMove(gameWindow.x + 20, gameWindow.y + 40);    
	    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	    robot.delay(500);
	    robot.mouseMove(gameWindow.x+20, gameWindow.y+60);    
	    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	    robot.delay(2000);
	    robot.mouseMove(gameWindow.x+100, gameWindow.y+70);    
	    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    robot.delay(500);
	    robot.mouseMove(gameWindow.x + 20, gameWindow.y + 40);    
	    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	    robot.delay(500);
	    robot.mouseMove(gameWindow.x+20, gameWindow.y+110);   
	    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	    robot.delay(2000);
	    robot.mouseMove(gameWindow.x+100, gameWindow.y+70);    
	    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    robot.delay(500);
	}
	
	private void oskInput(String key, int delay)
	{
		double x = 0; double y = 0;
		switch (key)
		{
		case "up":
			x = .7;
			y = .75;
			break;
		case "down":
			x = .7;
			y = .9;
			break;
		case "left":
			x = .66;
			y = .9;
			break;
		case "right":
			x = .77;
			y = .9;
			break;
		case "a":
			x = .13;
			y = .59;
			break;
		case "b":
			x = .38;
			y = .75;
			break;
		case "l":
			x = .57;
			y = .59;
			break;
		case "r":
			x = .27;
			y = .45;
			break;
		case "backspace":
			x = .8;
			y = .29;
			break;
		case "enter":
			x = .75;
			y = .65;
			break;
		}
		robot.mouseMove(oskWindow.x + (int)(x*oskWindow.width), 
				oskWindow.y + (int)(y*oskWindow.height));
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(delay);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(delay);
	}
	
	private void up(int d) throws InterruptedException
	{
		oskInput("up", d);
	}
	private void down(int d) throws InterruptedException
	{
		oskInput("down", d);
	}
	private void left(int d) throws InterruptedException
	{
		oskInput("left", d);
	}
	private void right(int d) throws InterruptedException
	{
		oskInput("right", d);
	}
	private void a(int d) throws InterruptedException
	{
		oskInput("a", d);
	}
	private void b(int d) throws InterruptedException
	{
		oskInput("b", d);
	}
	private void l(int d) throws InterruptedException
	{
		oskInput("l", d);
	}
	private void r(int d) throws InterruptedException
	{
		oskInput("r", d);
	}
	private void select(int d) throws InterruptedException
	{
		oskInput("backspace", d);
	}
	private void start(int d) throws InterruptedException
	{
		oskInput("enter", d);
	}
	
	public BufferedImage[][] printGrid()
	{
		//15 width x 10 height
		BufferedImage[][] grid = new BufferedImage[10][15];
		BufferedImage screen = printscreen(true);
		//System.out.println(screen.getWidth());
		//System.out.println(screen.getHeight());
		for (int i = 0; i < AWAI.tileHeight; i++)
		{
			//System.out.println("i: " + i);
			for (int j = 0; j < AWAI.tileWidth; j++)
			{
				//System.out.println("j: " + j);
				int w = (int)screen.getWidth()/15;
				int h = (int)screen.getHeight()/10;
				int x = j*w;
				int y = i*h;
				grid[i][j] = screen.getSubimage(x, y, w, h);
			}
		}
		return grid;
	}
	
	public BufferedImage printwindow()
	{
		return robot.createScreenCapture(screenshotWindow);
	}
	
	public BufferedImage printscreen(boolean holdB)
	{
		BufferedImage img;
		if (holdB)
		{
			robot.mouseMove(oskWindow.x + (int)(.38*oskWindow.width), 
					oskWindow.y + (int)(.75*oskWindow.height));
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.delay(100);
		}
		img = robot.createScreenCapture(screenshotScreen);
		if (holdB)
		{
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		}
		return img;
	}
	
	public BufferedImage[] printTileInfo() throws IOException, InterruptedException
	{
		Thread.sleep(100); // Needed in case tile info is switching sides
		BufferedImage[] subimgs = new BufferedImage[2];
		BufferedImage temp;
		int x, y, w, h;
		y = screenshotScreen.y + (screenshotScreen.height * 6) / AWAI.tileHeight;
		w = (screenshotScreen.width * 2) / AWAI.tileWidth;
		h = (screenshotScreen.height * 4) / AWAI.tileHeight;
		x = screenshotScreen.x;
		if (!isTileInfoLeft) x += screenshotScreen.width* 13 / AWAI.tileWidth;
		Rectangle rec = new Rectangle(x, y, w, h);
		BufferedImage img = robot.createScreenCapture(rec);
		ImageIO.write(img, "jpg", new File(AWAI.baseFP + "\\Screenshots\\terraininspectwhole" + d + ".jpg"));
		temp = img.getSubimage(0, img.getHeight()/8, img.getWidth(), img.getHeight()*3/16);
		ImageIO.write(temp, "jpg", new File(AWAI.baseFP + "\\Screenshots\\terraininspectraw" + d + ".jpg"));
		subimgs[0] = ImageUtility.wash(temp, 150);
		ImageIO.write(subimgs[0], "jpg", new File(AWAI.baseFP + "\\Screenshots\\terraininspectwash" + d + ".jpg"));
		temp = img.getSubimage(0, img.getHeight()*23/32, img.getWidth(), img.getHeight()*2/16);
		ImageIO.write(temp, "jpg", new File(AWAI.baseFP + "\\Screenshots\\terraincapinspectraw" + d + ".jpg"));
		subimgs[1] = ImageUtility.wash(temp, 150);
		ImageIO.write(subimgs[1], "jpg", new File(AWAI.baseFP + "\\Screenshots\\terraincapinspectwash" + d + ".jpg"));
		d++;
		return subimgs;
	}
	
	/*
	public BufferedImage[] printIntel(boolean left)
	{
		BufferedImage[] subimgs = new BufferedImage[6];
		int x, y, w, h;
		y = screenshotScreen.y;
		w = (screenshotScreen.width * 7) / AWAI.tileWidth;
		h = screenshotScreen.height;
		x = screenshotScreen.x;
		if (!left) x += screenshotScreen.width* 8 / AWAI.tileWidth;
		Rectangle rec = new Rectangle(x, y, w, h);
		BufferedImage img = robot.createScreenCapture(rec);
		subimgs[0] = img.getSubimage(0, img.getHeight()/2, img.getWidth()/2, img.getHeight()/6);
		subimgs[1] = img.getSubimage(img.getWidth()/2, img.getHeight()/2, img.getWidth()/2, img.getHeight()/6);
		subimgs[2] = img.getSubimage(0, img.getHeight()*4/6, img.getWidth()/2, img.getHeight()/6);
		subimgs[3] = img.getSubimage(img.getWidth()/2, img.getHeight()*4/6, img.getWidth()/2, img.getHeight()/6);
		subimgs[4] = img.getSubimage(0, img.getHeight()*5/6, img.getWidth()/2, img.getHeight()/6);
		subimgs[5] = img.getSubimage(img.getWidth()/2, img.getHeight()*5/6, img.getWidth()/2, img.getHeight()/6);
		return subimgs;
	}*/
}
