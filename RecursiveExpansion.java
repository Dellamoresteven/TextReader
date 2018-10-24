import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
//Steven Dellamore
class RecursiveExpansion {
	BufferedImage oldimage;
	BufferedImage newimage;
	PixelValue lastpixel; 
	static int[][] mask;
	int objCount;
	public int top;
	public int bot;
	public int left; 
	public int right;
	RecursiveExpansion(BufferedImage oldimage, int [][] maskk, int objCount){
		this.oldimage = oldimage;
		newimage = oldimage;
		mask = maskk;
		this.objCount = objCount;
		// newimage = CreateNewImage();
	}
	public int[][] FindLetter(int row, int col){
		top = col;
		bot = col;
		left = row; 
		right = row;
		PixelValue pix = new PixelValue(newimage,row,col);
		lastpixel = pix; 
		// System.out.println("LAST PIXL: RED:" + lastpixel.getRed() + " GREEN:" + lastpixel.getGreen() + " BLUE:" + lastpixel.getBlue());

		// System.out.println(pix.getRed());
		FindSize(row,col);
		// printMask();
		return mask;
	}

	private void printMask(){
		for (int i = 0;i <  oldimage.getWidth(); i++) {
			for (int j = 0; j < oldimage.getHeight(); j++) {

				System.out.printf("%d ", mask[i][j]);
			}
		}
	}
	public void check(int row, int col){
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
	public void FindSize(int row, int col){
		
		// System.out.println(oldimage.getWidth() + ":" + oldimage.getHeight());
		if((row < 0) || (col < 0) || (row >= oldimage.getWidth()) || (col >= oldimage.getHeight()) || (mask[row][col] != 0)){
			// System.out.println(row + ":" + col);
			return;
		}
		// System.out.println(row + ":" + col);
		PixelValue pixel = new PixelValue(newimage, row, col);
		// System.out.println(pixel.getRed());
		// System.out.println(row + ":" + col);
		if(threshholds(pixel) || mask[row][col] != 0){
			// System.out.println(pixel.getRed());
			return;
		}
		check(row, col);
		// System.out.println(row + ":" + col);
		// System.out.println("RED:" + pixel.getRed() + " GREEN:" + pixel.getGreen() + " BLUE:" + pixel.getBlue());
		mask[row][col] = objCount;
		// System.out.println(row + ":" + col);
		// System.out.println(objCount);
		// System.out.println("F");
		FindSize(row - 1, col);
		// System.out.println("Fff");
		FindSize(row + 1, col);
		// System.out.println(row + ":" + col);
		FindSize(row, col + 1);
		// System.out.println("AAAAAAA");
		FindSize(row, col - 1);
			// FindSize(row + 1, col + 1, threshhold);
			// FindSize(row + 1, col - 1, threshhold);
			// FindSize(row - 1, col + 1, threshhold);
			// FindSize(row - 1, col - 1, threshhold);
	}
	public boolean threshholds(PixelValue pixel){ //return true if there is a new color, false otherwise
		int r = Math.abs(pixel.getRed() - lastpixel.getRed());
		int b = Math.abs(pixel.getBlue() - lastpixel.getBlue());
		int g = Math.abs(pixel.getGreen() - lastpixel.getGreen());

		if((r >= 80) || (b >= 80) || (g >= 80)){
			// System.out.println("TRUE" + r + ":" + b + ":" + g);
			// System.out.println(r + ":" + b + ":" + g);
			return true;
		}
		// System.out.println("FALSE" + r + ":" + b + ":" + g);
		return false;
	}

}


				// System.out.println(pixel.getRed());
				// System.out.println(pixel.getGreen());
				// System.out.println(pixel.getBlue());