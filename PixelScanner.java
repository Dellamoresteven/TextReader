import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class PixelScanner {
	private BufferedImage image;
	PixelScanner(BufferedImage image){
		this.image = image;
		startscan();
	}
	private void startscan(){
		PixelValue pixel;
		boolean first = false;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				pixel = new PixelValue(image, i, j);
				if((pixel.getRed() <= findthreshhold()) && (first == false)){
					pixel = new PixelValue(image, i, j-1);
					pixel.setRGB();
					first = true;
				}else if(pixel.getRed() > findthreshhold()){
					try{
						if (first == true) {
							pixel = new PixelValue(image, i, j);
							pixel.setRGB();
						}
					}catch(Exception e){}
					first = false;
				}

			}
		}
		first = false;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				pixel = new PixelValue(image, j, i);
				if((pixel.getRed() <= findthreshhold()) && (first == false)){
					pixel = new PixelValue(image, j-1, i);
					pixel.setRGB();
					first = true;
				}else if(pixel.getRed() > findthreshhold()){
					try{
						if(first == true){
							pixel = new PixelValue(image, j, i);
							pixel.setRGB();
						}
					}catch(Exception e){}
					first = false;
				}

			}
		}
	}
	private int findthreshhold(){
		return 150;
	}
}


				// System.out.println(pixel.getRed());
				// System.out.println(pixel.getGreen());
				// System.out.println(pixel.getBlue());