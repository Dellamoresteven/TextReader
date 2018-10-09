import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*; 
//Steven Dellamore
class PixelScanner {
	private BufferedImage image;
	private List<RecursiveExpansion> RecObjHolder = new ArrayList<RecursiveExpansion>();   
	public int[][] mask;
	PixelScanner(BufferedImage image){
		this.image = image;
		mask = new int[image.getWidth()][image.getHeight()];
		startscan();
	}
	private void startscan(){
		PixelValue pixel;
		// currentpixel = pixel;
		boolean first = false;
		int objCount = 1;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				if((mask[i][j] == 0)){
					// System.out.println(i + j);
					RecursiveExpansion recursiveExpansion = new RecursiveExpansion(image, mask, objCount++);
					mask = recursiveExpansion.FindLetter(i,j);
					RecObjHolder.add(recursiveExpansion);
				}
			}
		}
	}
}


				// System.out.println(pixel.getRed());
				// System.out.println(pixel.getGreen());
				// System.out.println(pixel.getBlue());