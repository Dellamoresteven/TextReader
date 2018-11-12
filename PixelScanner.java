/**
 * @class PixelScanner
 * 
 * @author Steven Dellamore dellamoresteven@gmail.com
 * @author Richard Hansen richie.hansen@gmail.com
 * 
 * @version 1.0 11/12/2018
 *
 */

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;


/**
 * PixelScanner class, this takens an image and pulls the letters out and
 * puts them into new smaller pictures to be sent to the AI for identifcation.
 */
class PixelScanner {
	private BufferedImage image;	/* Declares the image that the class will be working on */
	//private List<RecursiveExpansion> RecObjHolder = new ArrayList<RecursiveExpansion>();   
	private int[][] mask;	/* Declares mask as a private 2D int array */
	private int objnum;		/* Declares objnum as a private Interger. */

	/**
	 * A constructor that will init this.mask, this.image and this.objnum. Then it will  
	 * start the scan of the image by calling {@link #startscan()}. Finally it will
	 * call the Formater class constructor. 
	 *
	 * @param image The image that you are wanting to extract letters from
	 */
	PixelScanner(BufferedImage image){
		this.objnum = 0;
		this.image = image;
		this.mask = new int[this.image.getHeight()][this.image.getWidth()];
		startscan();
		// Formater format = new Formater(RecObjHolder,mask,this.image);
	}

	/**
	 * A method that is called to check around the given cords to find other numbers.
	 * This method can be called with a checkAgaist number to ignore a certain number. (always ignores 0)
	 *
	 * @param row    The row of the cords
	 * @param col    The Collumns of the cords
	 * @param checkAgaist    number to not include in your search
	 *
	 * @return the number around that is not == 0 && checkAgaist
	 */
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

	/**
	* A method that will start scanning through each pixel of the this.image
	* and copy the objectnum to the corresponding this.mask[][] value. 

	* @return void
	*/
	private void startscan(){
		this.image = threshholdimage(image);
		// System.out.println(image.getHeight() + ":" + image.getWidth());

		/**
		 * Creates nested for loops to run through all the pixels starting at 1 to
		 * the length of the height and width -1. This is because when I call {@link #check(int,int,int)}
		 * we do not want to get an index out of bounds error. This for loop reads from left right
		 */
		for (int i = 1; i < image.getHeight()-1; i++) {
			for (int j = 1; j < image.getWidth()-1; j++) {
				if (image.getRGB(j,i) == -16777216) {
					int num = check(i, j, 0);
					if(num != 0){
						if(num == -1){
							/** 
							 * Checks to see if num == -1, if so we know its a new obj
							 * and increse objnum and assign num to equal the new obj
							 */
							num = ++objnum;
						}
						/* Sets the mask to objnum that is around or a new objnum */
						this.mask[i][j] = num; 
					}
				}
			}
		}
		printMask("MASK1");
		merging();
		printMask("MASK2");
		PicturePrinter();
	}

	/**
	 * A method that will merge the numbers in the same letter and change them all
	 * to the lowest number. 
	 *
	 * @return void
	 */
	private void merging(){
		/**
		 * Creates nested for loops to scan through the mask from left to right.
		 * This will check for objnum's that are touching. If they are touching
		 * it will replace the higher objnum for the lower objnum.
		 */
		for (int i = 0; i < this.mask.length; i++) {
			for(int j = 0; j < this.mask[0].length; j++){
				int num = this.mask[i][j];
				if(num != 0){
					/* Checks to see what other objnums are around num */ 
					int num2 = check(i,j,num);
					if(num2 != -1){
						if(num2 > num){
							/* Replaces num2 with num since num is smaller */
							replacing(num2,num);
						}else{
							/* Replaces num with num2 since num2 is smaller */
							replacing(num,num2);
						}
					}
				}
			}
		}
	}

	/**
	 * A method that will replace all of one number in this.mask[][] with a differnt
	 * number.
	 *
	 * @param replace    The number that we are replacing
	 * @param with    The number that we are replacing with
	 * 
	 * @return void
	 */
	private void replacing(int replace, int with){
		/**
		 * Creates nested for loops to scan through the mask from left to right.
		 * This will replace all the 'replace' values in the mask and change them to
		 * the 'with' value. 
		 */
		for (int i = 0; i < this.mask.length; i++) {
			for(int j = 0; j < this.mask[0].length; j++){
				int num = mask[i][j];
				if(num == replace){
					/* takes the index where the mask[i][j] equals replace and replaces it with with */
					mask[i][j] = with;
				}
			}
		}
	}

	/**
	 * A method that will print out the mask objects into a new picture. This is good for
	 * visual testing to make it easy to see how the program is working
	 *
	 * @param name    Name of file created
	 *
	 * @return void
	 */
	private void printMask(String name){
		/* set initial RGB values */
		int a = 255;
	    int r = 120;
	    int g = 255;
	    int b = 0;
	    /* Creates a new BuffredImage object for each letter with the dimensions being the size of this.image */
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(),image.getHeight(), BufferedImage.TYPE_INT_RGB);
		/**
		 * Creates nested for loops to scan through the mask from left to right.
		 * This will run through the image and set the new 'bufferedImage' with the 
		 * corresponding mask[i][j] values. 
		 */
		for (int i = 0;i <  image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				if(mask[i][j] == 0){
					/* If its the background */
					bufferedImage.setRGB(j,i,Color.WHITE.getRGB());
				}else{
					/* Sets the R G B values based on this.mask values */
					int rg = r + mask[i][j];
					int gr = g + mask[i][j];
					int gb = b + mask[i][j];
					int RGBnum = (a<<24) | (rg<<16) | (gr<<8) | gb;
					/* Sets Pixle value */
					bufferedImage.setRGB(j,i, RGBnum);
				}
			}
		}
		/* Saves the image according to 'name' */ 
		try{
	  		File pic = new File(name);
	  		ImageIO.write(bufferedImage, "jpg", pic);
		}catch(IOException e){
	  		System.out.println(e);
		}
	}

	/**
	 * A method that will check a pixels RGB value.
	 *
	 * @deprecated As of <TextReader> <743167be4a30295419a7e0ec00cd9fe0f32d99de>, 
	 * because this.image is now threshholded at the start, use 
	 * 			{@link #threshholdimage(BufferedImage)} instead.
 	 * 
	 * @param pixel    The current pixel that you are trying to check
	 *
	 * @return true if pixel is black, false otherwise
	 */
	@Deprecated
	public boolean threshhold(PixelValue pixel){
		if(pixel.getRGB() == -16777216)
			/* If pixel is black */
			return true; 
		/* If pixel is not black */
		return false;
	}

	/**
	 * A method that changes the given image to a binary image (white and black). 
	 * This method changes the this.image to the new threshholded image
	 *
	 * @param oldImage    The image that you are trying to threshold
	 *
	 * @return The new black and white image
	 */
	public BufferedImage threshholdimage(BufferedImage oldImage){
		PixelValue pixel;
		/* Creates a new BuffredImage object for the new threshholded image to be put onto */
		BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(),BufferedImage.TYPE_BYTE_BINARY); 
		/**
		 * Creates nested for loops to scan through the image from left to right.
		 * This will check if the difference in color of R, G, B is > then the threshold
		 * If it is we can assume that this is a letter and put a black pixel on the 
		 * new image, if not we assume its background and put a white pixel
		 */
		for (int i = 0; i < oldImage.getWidth(); i++) {
			for (int j = 0; j < oldImage.getHeight(); j++) {
				pixel = new PixelValue(oldImage,i,j);
				int r = Math.abs(255 - pixel.getRed());
				int b = Math.abs(255 - pixel.getBlue());
				int g = Math.abs(255 - pixel.getGreen());
				if((r >= 190) || (b >= 190) || (g >= 190)){
					newImage.setRGB(i, j, Color.BLACK.getRGB());
				}else{
					newImage.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
		}
		return newImage;
	}

	/**
	 * A method that will print the mask over this.image. This is good
	 * for visual testing to see what the program is doing.
	 *
	 * @deprecated As of <TextReader> <743167be4a30295419a7e0ec00cd9fe0f32d99de>, 
	 * because we do not want to write over orginal image, also does not change 
	 * color based on this.objnum, use 
	 * 			{@link #printMask(String)} instead.
	 *
	 * @return void
	 */
	@Deprecated
	public void printOverImage(){
		// BufferedImage imag = ImageIO.read(pic);
		// imag = ImageIO.read(pic);
		File pic;
		String name = "test";
		try{
			/**
			 * Creates nested for loops to scan through the mask from left to right.
			 * This writes over this.image with new pixels based off valueas in mask 
			 * that correspond with each color. 
			 */
			for(int i = 0; i < image.getWidth(); i++){
				for(int j = 0; j < image.getHeight(); j++){
					if(mask[i][j] % 10 == 0){
						/* Every 10th letter makes it a different letter */ 
						int RGBnum = (255<<24) | (255<<16) | (0<<8) | 0;
						image.setRGB(i, j, RGBnum);
					}
					else if(mask[i][j] != 0){
						/* If its a letter go here */
						int RGBnum = (255<<24) | (120<<16) | (255<<8) | 0;
						image.setRGB(i, j, RGBnum);
					}else{
						/* If its not a letter then its the background, so go here */
						int RGBnum = (255<<24) | (120<<16) | (255<<8) | 0;
						image.setRGB(i, j, RGBnum);
					}
				}
			}
			/* Creates a new image in the test folder with name as follows: 'Modified test' */
      		pic = new File("test/Modified " + name);
      		ImageIO.write(image, "jpg", pic);
    	}catch(IOException e){
      		System.out.println(e);
    	}
	}
	/* creates globel top/bot/right/left for each letter */
	int top;
	int bot;
	int right;
	int left;

	/**
	 * A method that will check check the bounds of each letter to 
	 * see if there is a new max/min. If there is then it will replace this.top,
	 * this.bot, this.right or this.left with the new max/min.
	 *
	 * @param row    Gives the row to check
	 * @param col    Gives the col to check

	 * @return void
	 */
	public void checkbounds(int row, int col){
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

	/**
	 * A method that will find the top/bot/right/left of each letter and print
	 * them to there own photo. Then will move each letter to the top left corner 
	 * and print the letter in red. This is good for visual testing to make sure
	 * this.mask has the correct letters in the correct places and can be moved.
	 *
	 * @return void 
	 */
	public void PicturePrinter(){
		BufferedImage newImage;
		/**
		 * Single loop that will go through each objnum and make pictures based on
		 * this.mask positions of the objnum
		 */
		for (int i = 1; i <= objnum; i++) {
			/* reset globel top/bot/right/left varibles for next letter */
			top = mask[0].length;
			bot = 0;
			right = 0;
			left = mask.length;
			/* counter to see how many pixels the letter is */
			int c = 0; 
			/* Creates a new BuffredImage object for each letter to be put into */ 
			newImage = new BufferedImage(mask[0].length, mask.length,BufferedImage.TYPE_INT_RGB); 
			/**
			 * Creates nested for loops to scan through the mask from left to right.
			 * This will write to the new image for each letter. Checks to see if 
			 * the current this.mask values is equal to i and places a pixel there. 
			 */
			for (int j = 0; j < mask.length; j++) {
				for(int k = 0; k < mask[0].length; k++){
					if(mask[j][k] == i){
						c++;
						newImage.setRGB(k,j,Color.WHITE.getRGB());
						checkbounds(j,k);
					}
				}
			}
			/* Checks to see if the letter exsits after the merge, and is > then 40 pixels */
			if((top < bot) && (c > 40)){
				/**
				 * Creates nested for loops to scan through the mask from left to right.
				 * This will make the object be pushed up to the top left corner of the screen
				 * and turn the letter to red.
				 */
				for (int j = 0; j < mask.length; j++) {
					for(int k = 0; k < mask[0].length; k++){
						if(mask[j][k] == i){
							c++;
							newImage.setRGB(k-top,j-left,Color.RED.getRGB());
						}
					}
				}
				try{
					/* Writes each letter to a new file in test/ with name as following: 'i' */
					File pic = new File("test/" + i);
					ImageIO.write(newImage, "jpg", pic);
				}catch(Exception e){
					System.out.println("Could not make iamge");

				}
			}
		}
	}
}

/* END OF FILE */ 