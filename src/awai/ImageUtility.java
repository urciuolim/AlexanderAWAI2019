package awai;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ImageUtility 
{
	
	public static BufferedImage wash(BufferedImage dirty, int tolerance)
	{
		BufferedImage clean =  new BufferedImage(dirty.getWidth(), dirty.getHeight(), dirty.getType());
		Color temp;
		for (int i = 0; i < dirty.getWidth(); i++)
		{
			for (int j = 0; j < dirty.getHeight(); j++)
			{
				temp = new Color(dirty.getRGB(i, j));
				if (temp.getRed() > tolerance & temp.getGreen() > tolerance & temp.getBlue() > tolerance)
					clean.setRGB(i, j, Color.black.getRGB());
				else
					clean.setRGB(i, j, Color.white.getRGB());
			}
		}
		return clean;
	}
	
	public static boolean compareImages(BufferedImage img1, BufferedImage img2, int tolerance)
	{
		int w = img1.getWidth(); int h = img2.getHeight();
		if (w != img2.getWidth() || h != img2.getHeight())
			return false;
		int counter = 0;
		for (int i = 0; i < w; i++)
		{
			for (int j = 0; j < h; j++)
			{
				if (img1.getRGB(i, j) != img2.getRGB(i, j))
				{
					counter++;
				}
			}
		}
		if (counter > tolerance) return false;
		return true;
	}
	
	public static void storeImage(BufferedImage img, String path) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
		writer.append(img.getWidth() + "\n");
		writer.append(img.getHeight() + "\n");
		writer.append(img.getType() + "\n");
		for (int i = 0; i < img.getWidth(); i++)
		{
			for (int j = 0; j < img.getHeight(); j++)
			{
				writer.append(img.getRGB(i, j) + "\n");
			}
		}
		writer.close();
	}
	
	public static BufferedImage readImage(File file) throws NumberFormatException, IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int w = Integer.parseInt(reader.readLine());
		int h = Integer.parseInt(reader.readLine());
		BufferedImage img = new BufferedImage(w, h, Integer.parseInt(reader.readLine()));
		for (int i = 0; i < w; i++)
		{
			for (int j = 0; j < h; j++)
			{
				img.setRGB(i, j, Integer.parseInt(reader.readLine()));
			}
		}
		reader.close();
		return img;
	}
}
