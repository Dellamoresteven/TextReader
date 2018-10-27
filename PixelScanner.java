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
import java.awt.Graphics;

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
		Formater format = new Formater(RecObjHolder,mask,this.image);

	}

	private void startscan(){
		PixelValue pixel;
		threshholdimage(image);
		
		boolean first = false;
		int objCount = 1;
		// System.out.printf("%d:%d\n", testpixle.getRed(), testpixle2.getRed());
		// return;
		int thresholdcount = 0;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

				pixel = new PixelValue(image,i,j);
				// System.out.println(pixel.getRGB());
				if((mask[i][j] == 0)){
					if(thresholdcount == 0){
						if(threshhold(pixel)){
							// System.out.println(i+":"+j);
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
				}else{
					thresholdcount = 1;
				}
				
			}

		}
		// printMask();
		// printMask();
		// System.out.println(RecObjHolder.size());

		PicturePrinter();
	}
	private void printMask(){
		for (int i = 0;i <  image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				System.out.printf("%d ", mask[i][j]);
			}
		}
	}
	public boolean threshhold(PixelValue pixel){ //return true if there is a new color, false otherwise
		if(pixel.getRGB() == -16777216)
			return true;
		return false;
	}
	public void threshholdimage(BufferedImage oldImage){
		PixelValue pixel;
		BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(),BufferedImage.TYPE_BYTE_BINARY); 
		for (int i = 0; i < oldImage.getWidth(); i++) {
			for (int j = 0; j < oldImage.getHeight(); j++) {
				pixel = new PixelValue(oldImage,i,j);
				int r = Math.abs(255 - pixel.getRed());
				int b = Math.abs(255 - pixel.getBlue());
				int g = Math.abs(255 - pixel.getGreen());
				if((r >= 190) || (b >= 190) || (g >= 190)){//black
					newImage.setRGB(i, j, Color.BLACK.getRGB());
				}else{//white
					newImage.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
		}
		this.image = newImage;
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
	/*
		Sends all the letters to the MI Python. 
	*/
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
		for(int i = 0; i < RecObjHolder.size(); i++){
			bufferedImage = new BufferedImage(maxWidth+1, maxHeight+1, BufferedImage.TYPE_BYTE_BINARY);
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
						bufferedImage.setRGB(j - RecObjHolder.get(i).left+width, k - RecObjHolder.get(i).top + height, RGBnum);
					}
				}
			}
			pic = new File("test/" + i);

			BufferedImage dimg = new BufferedImage(28, 28, bufferedImage.getType()); //problem with this line
			BufferedImage newImage = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_BINARY);

			Graphics g = newImage.createGraphics();
			g.drawImage(bufferedImage, 0, 0, 28, 28, null);
			g.dispose();
			int[][] arraysToSendToRichard = new int[28][28];
			for (int k = 0; k < 28; k++) {
				for (int j = 0; j < 28; j++) {
					if(bufferedImage.getRGB(k,j) == -16777216){
						arraysToSendToRichard[k][j] = 0;
					}else{
						arraysToSendToRichard[k][j] = 1;
					}
				}
			}
			// try{
			// 	ImageIO.write(bufferedImage, "jpg", pic);
			// }catch(Exception e){

			// }
			//SEND TO RICHARD HERE
		}
		// startFormat(mask);
		System.out.println("AMOUNT OF STUFF:" + RecObjHolder.size());
	}
}


				// System.out.println(pixel.getRed());
				// System.out.println(pixel.getGreen());
				// System.out.println(pixel.getBlue());