import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*; 
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;

//Steven Dellamore
class PixelScanner {
	private BufferedImage image;
	private List<RecursiveExpansion> RecObjHolder = new ArrayList<RecursiveExpansion>();   
	public int[][] mask;
	static int objnum = 0;
	static int oldObjNum = 0;
	PixelScanner(BufferedImage image){
		this.image = image;
		mask = new int[image.getWidth()][image.getHeight()];
		startscan();
	}
	// private int check(int i, int j){
	// 	// System.out.println(i + ":" + j);
	// 	if(mask[i+1][j] != 0){
	// 		return mask[i+1][j];
	// 	}
	// 	else if(mask[i-1][j] != 0){
	// 		return mask[i-1][j];
	// 	}
	// 	else if(mask[i][j+1] != 0){
	// 		return mask[i][j+1];
	// 	}
	// 	else if(mask[i][j-1] != 0){
	// 		return mask[i][j-1];
	// 	}
	// 	else if(mask[i+1][j+1] != 0){
	// 		return mask[i+1][j+1];
	// 	}
	// 	else if(mask[i-1][j-1] != 0){
	// 		return mask[i-1][j-1];
	// 	}
	// 	else if(mask[i+1][j-1] != 0){
	// 		return mask[i+1][j-1];
	// 	}
	// 	else if(mask[i-1][j+1] != 0){
	// 		return mask[i-1][j+1];
	// 	}
	// 	System.out.println(i + ":" + j);
	// 	return objnum++; 
	// }
	private void startscan(){
		// PixelValue currentPixel = new PixelValue(image,0,0);
		// PixelValue lastPixel = new PixelValue(image,0,0);
		// boolean threshholdcounter = false;
		// // int objnum = 0;
		// for (int i = 0; i < image.getWidth(); i++) {
		// 	for(int j = 0; j < image.getHeight(); j++){
		// 		currentPixel = new PixelValue(image,i,j);
		// 		if(threshhold(currentPixel, lastPixel)){
		// 			if(!threshholdcounter){
		// 				mask[i][j] = check(i,j);
		// 				threshholdcounter = true;
		// 				oldObjNum = objnum;
		// 			}else{
		// 				oldObjNum = 0;
		// 				threshholdcounter = false;
		// 			}
		// 		}
		// 		mask[i][j] = oldObjNum;
		// 		lastPixel = currentPixel;
		// 	}
		// 	lastPixel = new PixelValue(image,i,0);
		// }
		// System.out.println(objnum);
		// printMask();




		PixelValue pixel;
		PixelValue lastPixel = new PixelValue(image,0,0);
		// System.out.println("FIRSt - RED:" + lastPixel.getRed() + " GREEN:" + lastPixel.getGreen() + " BLUE:" + lastPixel.getBlue());

		// currentpixel = pixel;
		boolean first = false;
		int objCount = 1;
		// System.out.printf("%d:%d\n", testpixle.getRed(), testpixle2.getRed());
		// return;
		int thresholdcount = 0;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				pixel = new PixelValue(image,i,j);
				if((mask[i][j] == 0)){
					if(thresholdcount == 0){
						if((mask[i][j] == 0) && (threshhold(pixel, lastPixel))){
							RecursiveExpansion recursiveExpansion = new RecursiveExpansion(image, mask, objCount++);
							mask = recursiveExpansion.FindLetter(i,j);
							RecObjHolder.add(recursiveExpansion);

							// return;
							thresholdcount = 1;
							// return;
						}
					}else{
						thresholdcount = 0;
					}
					lastPixel = pixel;
				}else{
					thresholdcount = 1;
				}
				
			}

		}
		// printMask();
		// printMask();
		// System.out.println(RecObjHolder.size());

		PicturePrinter();
		// printOverImage();

	}
	private void printMask(){
		for (int i = 0;i <  image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				System.out.printf("%d ", mask[i][j]);
			}
		}
	}
	public boolean threshhold(PixelValue pixel, PixelValue lastpixel){ //return true if there is a new color, false otherwise
		int r = Math.abs(pixel.getRed() - 255);
		int b = Math.abs(pixel.getBlue() - 255);
		int g = Math.abs(pixel.getGreen() - 255);
		if((r >= 200) || (b >= 200) || (g >= 200)){
			// System.out.println("FF");
			return true;
		}
		return false;
	}
	public void printOverImage(){
		// BufferedImage imag = ImageIO.read(pic);
		// imag = ImageIO.read(pic);
		File pic;
		String name = "test";
		try{
			for(int i = 0; i < image.getWidth(); i++){
				for(int j = 0; j < image.getHeight(); j++){
					if(mask[i][j] % 10 == 0){
						int RGBnum = (255<<24) | (255<<16) | (0<<8) | 0;
						image.setRGB(i, j, RGBnum);
					}
					else if(mask[i][j] != 0){
						int RGBnum = (255<<24) | (120<<16) | (255<<8) | 0;
						image.setRGB(i, j, RGBnum);
					}else{
						int RGBnum = (255<<24) | (120<<16) | (255<<8) | 0;
						image.setRGB(i, j, RGBnum);
					}
				}
			}
      		pic = new File("test/Modified " + name);
      		ImageIO.write(image, "jpg", pic);
    	}catch(IOException e){
      		System.out.println(e);
    	}
	}
	public void PicturePrinter(){
		// System.out.println(RecObjHolder.get(0).top);
		// System.out.println(RecObjHolder.get(0).bot);
		// System.out.println(RecObjHolder.get(0).left);
		// System.out.println(RecObjHolder.get(0).right);
		int maxHeight = 0;
		int maxWidth = 0;
		for (int i = 0; i < RecObjHolder.size(); i++) {
			int width = RecObjHolder.get(i).right - RecObjHolder.get(i).left;

			if(maxWidth < RecObjHolder.get(i).right - RecObjHolder.get(i).left)
				maxWidth = RecObjHolder.get(i).right - RecObjHolder.get(i).left;

			if(maxHeight < RecObjHolder.get(i).bot - RecObjHolder.get(i).top)
				maxHeight = RecObjHolder.get(i).bot - RecObjHolder.get(i).top;
		}

		if(maxHeight > maxWidth){
			maxWidth = maxHeight;
		}else{
			maxHeight = maxWidth;
		}
		// System.out.println(RecObjHolder.get(0).left + ":" + RecObjHolder.get(0).top);
		File pic;
		BufferedImage bufferedImage;
		String name = "test";
		try{
			for(int i = 0; i < RecObjHolder.size(); i++){
				bufferedImage = new BufferedImage(maxWidth+1, maxHeight+1, BufferedImage.TYPE_INT_RGB);
				for(int v = 0; v < maxWidth+1; v++){
					for(int u = 0; u < maxWidth+1; u++){
						int RGBnumx = (255<<24) | (255<<16) | (255<<8) | 255;
						bufferedImage.setRGB(v,u,RGBnumx);
					}
				}
				for (int j = 0;  j < image.getWidth(); j++) {
					for(int k = 0; k < image.getHeight(); k++){
						if(mask[j][k] == i+1){
							int RGBnum = (255<<24) | (0<<16) | (0<<8) | 0;
							int width = ((maxWidth+1) - (RecObjHolder.get(i).right - RecObjHolder.get(i).left))/2;
							int height = ((maxWidth+1) - (RecObjHolder.get(i).bot - RecObjHolder.get(i).top))/2;
							// System.out.println("i:" + i + " " + width + ":" + height);
							bufferedImage.setRGB(j-RecObjHolder.get(i).left+width, k - RecObjHolder.get(i).top + height, RGBnum);
						}
					}
				}
				pic = new File("test/" + i);
      			ImageIO.write(bufferedImage, "jpg", pic);
			}
    	}catch(IOException e){
      		System.out.println(e);
    	}
	}
}


				// System.out.println(pixel.getRed());
				// System.out.println(pixel.getGreen());
				// System.out.println(pixel.getBlue());