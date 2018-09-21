import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
//Steven Dellamore
class RecursiveExpansion {
	BufferedImage oldimage;
	BufferedImage newimage;
	RecursiveExpansion(BufferedImage oldimage){
		this.oldimage = oldimage;
		newimage = oldimage;
		// newimage = CreateNewImage();
	}
	public void FindLetter(int row, int col, int threshhold){
		FindSize(row,col,threshhold);
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
	public void FindSize(int row, int col, int threshhold){
		PixelValue pixel = new PixelValue(newimage, row, col);
		if((pixel.getRed() == 255) || (pixel.getRed() >= threshhold)){
			return;
		}
		pixel.setRGB("Red");
		try{
			FindSize(row - 1, col, threshhold);
			FindSize(row + 1, col, threshhold);
			FindSize(row, col + 1, threshhold);
			FindSize(row, col - 1, threshhold);
			// FindSize(row + 1, col + 1, threshhold);
			// FindSize(row + 1, col - 1, threshhold);
			// FindSize(row - 1, col + 1, threshhold);
			// FindSize(row - 1, col - 1, threshhold);
		}catch(Exception e){

		}

	}
	private void AddToPicture(){

	}
	private void ResizePicture(){

	}
	public BufferedImage CreateNewImage(){
		// PixelValue pixel;
		// BufferedImage newimag = new BufferedImage(5000,5000,1); //Create new image
		// for (int i = 0; i < 5000; i++) {
		// 	for (int j = 0; j < 5000; j++) {
		// 		pixel = new PixelValue(newimag, i, j);
		// 		pixel.setRGB("White"); //sets new picture to all white
		// 	}
		// }
		try{
			File pic = new File("test/NewImage ");
	      	ImageIO.write(newimage, "jpg", pic);
	     }catch(Exception e){
	     	System.out.println("Could not create new file in RecursiveExpansion");
	     }
	     return newimage;
	}
}


				// System.out.println(pixel.getRed());
				// System.out.println(pixel.getGreen());
				// System.out.println(pixel.getBlue());