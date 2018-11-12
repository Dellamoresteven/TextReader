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
import java.awt.RenderingHints;
//Steven Dellamore
class PixelScanner {
	private BufferedImage image;
	private List<RecursiveExpansion> RecObjHolder = new ArrayList<RecursiveExpansion>();   
	public int[][] mask;
	static int objnum = 0;
	static int oldObjNum = 0;
	int h = 800;
	int w = 1200;
	PixelScanner(BufferedImage image){
		this.image = image;
		// this.image = resize(this.image,w,h);
		mask = new int[this.image.getHeight()][this.image.getWidth()];
		startscan();
		// Formater format = new Formater(RecObjHolder,mask,this.image);
	}
	// public static BufferedImage resize(BufferedImage img, int newW, int newH) {  
	//     int w = img.getWidth();  
	//     int h = img.getHeight();  
	//     BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
	//     Graphics2D g = dimg.createGraphics();  
	//     g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
	//     g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
	//     g.dispose();  
	//     return dimg;  
	// }  
	private int check(int row, int col, int checkAgaist){
		int r = -1;
		if(mask[row+1][col] != checkAgaist && mask[row+1][col] != 0){
			r = mask[row+1][col];
		}if(mask[row-1][col] != checkAgaist && mask[row-1][col] != 0){
			r = mask[row-1][col];
		}if(mask[row][col+1] != checkAgaist && mask[row][col+1] != 0){
			r = mask[row][col+1];
		}if(mask[row][col-1] != checkAgaist && mask[row][col-1] != 0){
			r = mask[row][col-1];
		}if(mask[row+1][col+1] != checkAgaist && mask[row+1][col+1] != 0){
			r = mask[row+1][col+1];
		}if(mask[row+1][col-1] != checkAgaist && mask[row+1][col-1] != 0){
			r = mask[row+1][col-1];
		}if(mask[row-1][col+1] != checkAgaist && mask[row-1][col+1] != 0){
			r = mask[row-1][col+1];
		}if(mask[row-1][col-1] != checkAgaist && mask[row-1][col-1] != 0){
			r = mask[row-1][col-1];
		}
		return r;
	}

	private void startscan(){
		threshholdimage(image);
		System.out.println(image.getHeight() + ":" + image.getWidth());
		for (int i = 1; i < image.getHeight()-1; i++) {
			for (int j = 1; j < image.getWidth()-1; j++) {
				if (image.getRGB(j,i) == -16777216) {
					int num = check(i, j, 0);
					if(num != 0){
						if(num == -1){
							objnum++;
							num = objnum;
						}
						mask[i][j] = num;
					}
				}
			}
		}
		System.out.println(objnum);
		printMask("MASK1");
		merging();
		printMask("MASK2");
		System.out.println(objnum);
		PicturePrinter();
	}

	private void merging(){
		for (int i = 0; i < mask.length; i++) {
			for(int j = 0; j < mask[0].length; j++){
				int num = mask[i][j];
				if(num != 0){
					int num2 = check(i,j,num);
					if(num2 != -1){
						if(num2 > num){
							replacing(num2,num);
						}else{
							replacing(num,num2);
						}
					}
				}
			}
		}
	}

	private void replacing(int replace, int with){
		// objnum--;
		for (int i = 0; i < mask.length; i++) {
			for(int j = 0; j < mask[0].length; j++){
				int num = mask[i][j];
				if(num == replace){
					mask[i][j] = with;
				}
			}
		}
	}
	private void printMask(String name){
		int a = 255;
	    int r = 120;
	    int g = 255;
	    int b = 0;
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(),image.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int i = 0;i <  image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				// System.out.printf("%d ", mask[i][j]);
				if(mask[i][j] == 0){
					bufferedImage.setRGB(j,i,Color.WHITE.getRGB());
				}else{
					int rg = r + mask[i][j];
					int gr = g + mask[i][j];
					int gb = b + mask[i][j];
					int RGBnum = (a<<24) | (rg<<16) | (gr<<8) | gb;
					bufferedImage.setRGB(j,i, RGBnum);
					// RGBnum += 10;
				}
			}
		}
		try{
	  		File pic = new File(name);
	  		ImageIO.write(bufferedImage, "jpg", pic);
		}catch(IOException e){
	  		System.out.println(e);
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

	int top;
	int bot;
	int right;
	int left;

	public void checkbounds(int row, int col){
		// System.out.println(top + ":" + bot + ":" + right + ":" + left);
		if(top > col){
			top = col;
		}if(bot < col){
			bot = col;
		}if(right < row){
			right = row;
		}if(left > row){
			left = row;
		}
	}
	/*
		Sends all the letters to the MI Python. 
	*/

	public void PicturePrinter(){
		BufferedImage newImage;
		for (int i = 1; i <= objnum; i++) {
			top = mask.length;
			bot = 0;
			right = 0;
			left = mask[0].length;
			newImage = new BufferedImage(mask[0].length, mask.length,BufferedImage.TYPE_BYTE_BINARY); 
			for (int j = 0; j < mask.length; j++) {
				for(int k = 0; k < mask[0].length; k++){
					if(mask[j][k] == i){
						newImage.setRGB(k,j,Color.WHITE.getRGB());
						checkbounds(j,k);
					}
				}
			}
			if(!(top == 485 && bot == 0)){
				// System.out.println(i + ":" + top + ":" + bot + ":" + right + ":" + left);
				try{
					File pic = new File("test/" + i);
					ImageIO.write(newImage, "jpg", pic);
				}catch(Exception e){
					System.out.println("Could not make iamge");

				}
			}
		}
	}
}


				// System.out.println(pixel.getRed());
				// System.out.println(pixel.getGreen());
				// System.out.println(pixel.getBlue());