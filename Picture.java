// import java.lang.Object;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;

class Picture {
	
	public static void main(String[] args) {
		File pic;
		BufferedImage imag;
		String name = "image0.jpg";
		try{
			pic = new File(name);
			imag = ImageIO.read(pic);
		}catch(Exception e){
			System.out.println("Picture not found");
			return;
		}
		PixelScanner pixelscanner = new PixelScanner(imag);
		// try{
		// 	pic = new File("YTESFA");
		// 	BufferedImage newImage = new BufferedImage(imag.getWidth(), imag.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

		// 	Graphics g = newImage.createGraphics();
		// 	g.drawImage(imag, 0, 0, imag.getWidth(), imag.getHeight(), null);
		// 	g.dispose();
		// 	ImageIO.write(newImage, "jpg", pic);
		// }catch(Exception e){
		// 	System.out.println("WHTF");
		// }

	}
}