import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
//Steven Dellamore
class RecursiveExpansion {
	BufferedImage oldimage;
	BufferedImage newimage;
	static int[][] mask;
	int objCount;
	RecursiveExpansion(BufferedImage oldimage, int [][] maskk, int objCount){
		this.oldimage = oldimage;
		newimage = oldimage;
		mask = maskk;
		this.objCount = objCount;
		// newimage = CreateNewImage();
	}
	public int[][] FindLetter(int row, int col){
		
		PixelValue pix = new PixelValue(newimage,row,col);
		// System.out.println(pix.getRed());
		FindSize(row,col, pix);
		// printMask();
		return mask;
		// PixelValue pixel = new PixelValue(newimage, row, col);
		// if(pixel.getRed() == 255){
		// 	return;
		// }
		// pixel.setRGB("Red");
	}

	private void printMask(){
		for (int i = 0;i <  oldimage.getWidth(); i++) {
			for (int j = 0; j < oldimage.getHeight(); j++) {

				System.out.printf("%d ", mask[i][j]);
			}
		}
	}
	public void FindSize(int row, int col, PixelValue lastpixel){
		// System.out.println(row + ":" + col);
		// System.out.println(oldimage.getWidth() + ":" + oldimage.getHeight());
		if((row < 0) || (col < 0) || (row >= oldimage.getWidth()) || (col >= oldimage.getHeight()) || (mask[row][col] != 0)){
			// System.out.println(row + ":" + col);
			return;
		}
		// System.out.println(row + ":" + col);
		PixelValue pixel = new PixelValue(newimage, row, col);
		// System.out.println(pixel.getRed());
		if(threshhold(pixel,lastpixel)){
			// System.out.println(pixel.getRed());
			return;
		}
		mask[row][col] = objCount;
		// System.out.println(row + ":" + col);
		// System.out.println(objCount);
		// System.out.println("F");
		FindSize(row - 1, col, pixel);
		// System.out.println("Fff");
		FindSize(row + 1, col, pixel);
		// System.out.println(row + ":" + col);
		FindSize(row, col + 1, pixel);
		// System.out.println("AAAAAAA");
		FindSize(row, col - 1, pixel);
			// FindSize(row + 1, col + 1, threshhold);
			// FindSize(row + 1, col - 1, threshhold);
			// FindSize(row - 1, col + 1, threshhold);
			// FindSize(row - 1, col - 1, threshhold);
	}
	public boolean threshhold(PixelValue pixel, PixelValue lastpixel){ //return true if there is a new color, false otherwise
		int r = Math.abs(pixel.getRed() - lastpixel.getRed());
		int b = Math.abs(pixel.getBlue() - lastpixel.getBlue());
		int g = Math.abs(pixel.getGreen() - lastpixel.getGreen());
		// System.out.println(r + ":" + b + ":" + g);
		if((r >= 40) || (b >= 40) || (g >= 40)){
			// System.out.println(r + ":" + b + ":" + g);
			return true;
		}
		return false;
	}

}


				// System.out.println(pixel.getRed());
				// System.out.println(pixel.getGreen());
				// System.out.println(pixel.getBlue());