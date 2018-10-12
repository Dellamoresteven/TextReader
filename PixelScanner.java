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
		PixelValue currentPixel = new PixelValue(image,0,0);
		PixelValue lastPixel = new PixelValue(image,0,0);
		int objnum = 0;
		for (int i = 0; i < image.getWidth(); i++) {
			for(int j = 0; j < image.getHeight(); j++){
				currentPixel = new PixelValue(image,i,j);
				if((mask[i][j] == 0)){
					if((threshhold(currentPixel,lastPixel))){
						mask[i][j] = objnum++;
					}else{
						lastPixel = currentPixel;
					}
				}
			}
		}
		printMask();




		//--------------- VERY JANK
		// PixelValue pixel;
		// PixelValue lastPixel = new PixelValue(image,0,0);
		// // currentpixel = pixel;
		// boolean first = false;
		// int objCount = 1;
		// // System.out.printf("%d:%d\n", testpixle.getRed(), testpixle2.getRed());
		// // return;
		// int thresholdcount = 0;
		// for (int i = 0; i < image.getWidth(); i++) {
		// 	for (int j = 0; j < image.getHeight(); j++) {
		// 		pixel = new PixelValue(image,i,j);
		// 		if((mask[i][j] == 0)){
		// 			if(thresholdcount == 0){
		// 				if((threshhold(pixel, lastPixel))){
		// 					System.out.println(i + ":" + j);
		// 					RecursiveExpansion recursiveExpansion = new RecursiveExpansion(image, mask, objCount++);
		// 					mask = recursiveExpansion.FindLetter(i,j);
		// 					RecObjHolder.add(recursiveExpansion);
		// 					// return;
		// 					thresholdcount = 1;
		// 					// return;
		// 				}
		// 			}else{
		// 				thresholdcount = 0;
		// 			}
		// 			lastPixel = pixel;
		// 		}else{
		// 			thresholdcount = 1;
		// 		}
				
		// 	}

		// }
		// printMask();
		// // printMask();
	}
	private void printMask(){
		for (int i = 0;i <  image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

				System.out.printf("%d ", mask[i][j]);
			}
		}
	}
	public boolean threshhold(PixelValue pixel, PixelValue lastpixel){ //return true if there is a new color, false otherwise
		int r = Math.abs(pixel.getRed() - lastpixel.getRed());
		int b = Math.abs(pixel.getBlue() - lastpixel.getBlue());
		int g = Math.abs(pixel.getGreen() - lastpixel.getGreen());
		if((r >= 40) || (b >= 40) || (g >= 40)){
			return true;
		}
		return false;
	}
}


				// System.out.println(pixel.getRed());
				// System.out.println(pixel.getGreen());
				// System.out.println(pixel.getBlue());