// import java.lang.Object;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class Picture {
	
	public static void main(String[] args) {
		File pic;
		BufferedImage imag;
		String name = "picture.jpg";
		try{
			pic = new File(name);
			imag = ImageIO.read(pic);
		}catch(Exception e){
			System.out.println("Picture not found");
			return;
		}
		PixelScanner pixelscanner = new PixelScanner(imag);
		try{
      		pic = new File("test/Modified " + name);
      		ImageIO.write(imag, "jpg", pic);
    	}catch(IOException e){
      		System.out.println(e);
    	}
	}
}