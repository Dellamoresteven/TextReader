import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
//Steven Dellamore
class RecursiveExpansion {
	BufferedImage oldimage;
	BufferedImage newimage;
	int[][] mask;
	int objCount;
	RecursiveExpansion(BufferedImage oldimage, int [][] mask, int objCount){
		this.oldimage = oldimage;
		newimage = oldimage;
		this.mask = mask;
		this.objCount = objCount;
		// newimage = CreateNewImage();
	}
	public int[][] FindLetter(int row, int col){
		FindSize(row,col);
		return mask;
		// PixelValue pixel = new PixelValue(newimage, row, col);
		// if(pixel.getRed() == 255){
		// 	return;
		// }
		// pixel.setRGB("Red");
	}

	int top = 0;
	int bot = 0;
	int left = 0;
	int right = 0;
	public void FindSize(int row, int col){
		PixelValue pixel;
		try{
			pixel = new PixelValue(newimage, row, col);
		}catch(Exception e){
			System.out.println("FEFAWFEAW");
		}
		if(mask[row][col] != 0){
			return;
		}
		mask[row][col] = objCount;
		FindSize(row - 1, col);
		FindSize(row + 1, col);
		FindSize(row, col + 1);
		FindSize(row, col - 1);
			// FindSize(row + 1, col + 1, threshhold);
			// FindSize(row + 1, col - 1, threshhold);
			// FindSize(row - 1, col + 1, threshhold);
			// FindSize(row - 1, col - 1, threshhold);
	}

}


				// System.out.println(pixel.getRed());
				// System.out.println(pixel.getGreen());
				// System.out.println(pixel.getBlue());