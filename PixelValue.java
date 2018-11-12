


import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class PixelValue {
	private int RGBnum;
	private BufferedImage image;
	private int row;
	private int col;
	PixelValue(BufferedImage image, int row, int col){
		RGBnum = image.getRGB(row,col);
		this.image = image;
		this.row = row;
		this.col = col;
	}
	public int getRGB(){
		return RGBnum;
	}
	public int getAlpha(){
		return ((RGBnum>>24) & 0xff);
	}
	public int getRed(){
		return ((RGBnum>>16) & 0xff);
	}
	public int getGreen(){
		return ((RGBnum>>8) & 0xff);
	}
	public int getBlue(){
		return (RGBnum & 0xff);
	}
	public void setRGB(String color){
		int a = 255;
	    int r = 120;
	    int g = 255;
	    int b = 0;
		switch(color){
			case "White":
				a = 255;
				r = 255;
				g = 255;
				b = 255;
				break;
			case "Green":
				a = 255;
	    		r = 120;
	   			g = 255;
	   			b = 0;
	   			break;
	   		case "Black":
	   			a = 255;
	   			r = 0;
	   			g = 0;
	   			b = 0;
	   			break;
	   		case "Red":
	   			a = 255;
	   			r = 255;
	   			g = 0;
	   			b = 0;
	   			break;
		}
    	RGBnum = (a<<24) | (r<<16) | (g<<8) | b;
		image.setRGB(this.row,this.col, RGBnum); 
	}
	// int p = imag.getRGB(0,0);
	// System.out.println("RGB value: " + imag.getRGB(0,0));
	// System.out.println("Alpha: " + ((p>>24) & 0xff));
	// System.out.println("Read: " + ((p>>16) & 0xff));
	// System.out.println("Green: " + ((p>>8) & 0xff));
	// System.out.println("Blue: " + (p & 0xff));


}