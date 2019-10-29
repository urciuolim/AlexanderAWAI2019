package awai;

import java.io.File;
import java.io.IOException;
import net.sourceforge.tess4j.*;

public class TessTest {

	public static void main(String[] args) throws IOException 
	{
		ITesseract tess = new Tesseract();
		String baseFP = new File( "." ).getCanonicalPath();
		File img = new File(baseFP + "\\test.jpg");
		if (img.exists())
		{
			System.out.println("Got Here");
	        try {
	            String result= tess.doOCR(img);
	            System.out.println(result);
	        } catch (TesseractException e) {
	            System.err.println(e.getMessage());
	        }
		}
	}

}
