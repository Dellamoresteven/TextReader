import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*; 
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;




class Formater{
	List<RecursiveExpansion> RecObjHolder;
	int[][] mask;
	BufferedImage image;
	Formater(List<RecursiveExpansion> RecObjHolder, int[][] mask, BufferedImage image){
		this.RecObjHolder = RecObjHolder;
		this.mask = mask;
		this.image = image;
		startFormat(mask);
	}
	void startFormat(int[][] format){
		List<Integer> objRanInto = new ArrayList<Integer>();   
		// List<Integer> objRanInto = new ArrayList<Integer>();   
		//THIS IS ALMOST WORKING CODE, ACCOUNTS FOR i's and lines connecting
		/*
		
		int[] intersections = new int[mask[0].length];
			// objRanInto.add(1);
		// System.out.println(objRanInto.indexOf(1)); // -1
		// int[]
		int n = 0;
		int numOfIntersections;
		for (int i = 0; i < mask[0].length; i++) {
			for(int j = 0; j < mask.length; j++){
				if(format[j][i] != 0){
					if(objRanInto.indexOf(format[j][i]) == -1){
						objRanInto.add(format[j][i]);
					}
				}
			}
			// if(numOfIntersections == 0)
			// 	printNewLine(i);
			// System.out.println(objRanInto.size());
			intersections[n++] = objRanInto.size();
			objRanInto.clear();
		}
		int down = 0;
		int lastAvg = 0;
		int lastNum = 0;
		boolean newline = true;
		for(int k = 5; k < intersections.length; k++){

			int newnum = intersections[k];

			// int avg = (intersections[k] + intersections[k-1] + intersections[k-2] + intersections[k-3] + intersections[k-4])/5;
			if(lastNum > newnum)
				down++;
			else if(lastNum	== newnum);

			else
				down = 0;
			System.out.println("New:" + newnum + " old:" + lastNum + " down:" + down);
			if(down >= 2){
				System.out.println("BOOK K:" + k);
				if(newnum == 0){
					if(newline == true){
						System.out.println("BOOOOOOKS K:" + k);
						printNewLine(k);
						// newline = false;
					}
				}
				down = 0;
				// printNewLine(k);
			}
			lastNum = newnum;
		}
		printToPic();
		*/


		// boolean topline = true;
		// for (int i = 0; i < mask[0].length; i++) {
		// 	for(int j = 0; j < mask.length; j++){
		// 		if(format[j][i] != 0){
		// 			if(objRanInto.indexOf(format[j][i]) == -1){
		// 				objRanInto.add(format[j][i]);
		// 			}
		// 		}
		// 	}
		// 	// if(numOfIntersections == 0)
		// 	// 	printNewLine(i);
		// 	// System.out.println(objRanInto.size());
		// 	// intersections[n++] = objRanInto.size();
		// 	if(objRanInto.size() == 0){
		// 		printNewLine(i);
		// 	}
		// 	objRanInto.clear();
		// }
		objRanInto.clear();
		for (int i = 0; i < format[0].length; i++) {
			for (int j = 0; j < format.length; j++) {
				if(format[j][i] != 0){
					objRanInto.add(format[j][i]);
					break;
				}
			}
		}
		printToPic();
		System.out.println(objRanInto);
		// for (int i = 0; i ; ) {
			
		// }
	}


	void printNewLine(int line){
		for(int j = 0; j < image.getWidth(); j++){
			image.setRGB(j,line,-16777216);
		}
	}
	void printToPic(){
		File pic;
		String name = "test";
		try{
			pic = new File("newPicccc");
  			ImageIO.write(image, "jpg", pic);
		}catch(Exception e){

		}
	}
}


