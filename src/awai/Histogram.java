package awai;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Histogram 
{
	int r[];
	int g[];
	int b[];
	
	public Histogram(BufferedImage img)
	{
		r = new int[26];
		g = new int[26];
		b = new int[26];
		Color temp;
		
		for (int i = 0; i < img.getWidth(); i++)
		{
			for (int j = 0;j < img.getHeight(); j++)
			{
				temp = new Color(img.getRGB(i, j));
				r[(int)(temp.getRed()/10)]++;
				g[(int)(temp.getGreen()/10)]++;
				b[(int)(temp.getBlue()/10)]++;
			}
		}
	}
	
	public Histogram(String saved)
	{
		r = new int[26];
		g = new int[26];
		b = new int[26];
		int index = 0;
		for (int i = 0; i < r.length; i++)
		{
			//System.out.println(i);
			index = saved.indexOf(' ');
			//System.out.println(Integer.parseInt(saved.substring(0, index)));
			r[i] = Integer.parseInt(saved.substring(0, index));
			saved = saved.substring(index+1);
			//System.out.println(saved);
			index = saved.indexOf(' ');
			//System.out.println(Integer.parseInt(saved.substring(0, index)));
			g[i] = Integer.parseInt(saved.substring(0, index));
			saved = saved.substring(index+1);
			//System.out.println(saved);
			index = saved.indexOf(' ');
			//System.out.println(Integer.parseInt(saved.substring(0, index)));
			b[i] = Integer.parseInt(saved.substring(0, index));
			saved = saved.substring(index+1);
		}
	}
	
	public String toString()
	{
		String out = "";
		for (int i = 0; i < r.length; i++)
		{
			out = out + r[i] + " " + g[i] + " " + b[i] + " ";
		}
		return out;
	}
	
	public boolean isSimilar(Histogram otherHistogram, int tolerance)
	{
		int diff = 0;
		for (int i = 0; i < r.length; i++)
		{
			diff += Math.abs(otherHistogram.r[i] - r[i]) + 
					Math.abs(otherHistogram.g[i] - g[i]) + 
					Math.abs(otherHistogram.b[i] - b[i]);
			/*
			if (Math.abs(otherHistogram.r[i] - r[i]) > tolerance ||
					Math.abs(otherHistogram.g[i] - g[i]) > tolerance ||
					Math.abs(otherHistogram.b[i] - b[i]) > tolerance)
			{
				return false;
			}
			*/
		}
		if (diff > tolerance) return false;
		return true;
	}
}
