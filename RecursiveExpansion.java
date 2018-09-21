import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
//Steven Dellamore
class RecursiveExpansion {
	BufferedImage newimage;
	RecursiveExpansion(){
		System.out.println("Rev");
		PixelValue pixel;
		newimage = new BufferedImage(100,100,1); //Create new image
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				pixel = new PixelValue(newimage, i, j);
				pixel.setRGB("White");
			}
		}
		try{
			File pic = new File("test/NewImage ");
	      	ImageIO.write(newimage, "jpg", pic);
	     }catch(Exception e){
	     	System.out.println("Could not create new file in RecursiveExpansion");
	     }
	}
	public void FindLetter(int row, int col){
		System.out.println("FindLEtter");
	}
	private void AddToPicture(){

	}
	private void ResizePicture(){

	}
}


				// System.out.println(pixel.getRed());
				// System.out.println(pixel.getGreen());
				// System.out.println(pixel.getBlue());