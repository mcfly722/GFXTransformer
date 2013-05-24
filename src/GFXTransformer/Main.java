package GFXTransformer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.imageio.ImageIO;

public class Main {

	private static int MainKey=182149108;
	private static final int blockSize = 8;	
	private static final int imageSize = 1024;
	private static final int inLine=imageSize/blockSize;
	private static final int blocks=inLine*inLine;
	
	private static HashMap<Integer,Integer> getSequence(int mainKey){
		// create table
		HashMap<Integer,Integer> exchange = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> sequence = new HashMap<Integer,Integer>();
		Random randomGenerator = new Random(mainKey);
		
		for(int i=0;i<blocks;i++)
			exchange.put(randomGenerator.nextInt(), i);
		
		SortedSet<Integer> sortedKeys = new TreeSet<Integer>(exchange.keySet());
		
		int i=0;
		for(Integer key:sortedKeys){
			sequence.put(i, exchange.get(key));
			i++;
		}
		return sequence; 
	}
	
	private static void exchange(BufferedImage src,BufferedImage dst,int s,int d){
		int xS=(s%inLine)*blockSize;
		int yS=(int) Math.floor(Math.round(s/inLine))*blockSize;
		int xD=(d%inLine)*blockSize;
		int yD=(int) Math.floor(Math.round(d/inLine))*blockSize;

		
		for(int y=0;y<blockSize;y++){
			int[] buffer = new int[blockSize];
			src.getRGB(xS, yS+y, blockSize,1, buffer, 0, blockSize);
			dst.setRGB(xD, yD+y, blockSize,1, buffer, 0, blockSize);
		}
	}
	
	public static void main(String[] args) {
		if(args.length!=2)
			System.out.println("usage: GFXTransformer <src.png> <dst.png>");
		else{
			try {
				BufferedImage imgSrc = ImageIO.read(new File(args[0]));
				BufferedImage imgDst = new BufferedImage(imageSize, imageSize,BufferedImage.TYPE_INT_ARGB);

				HashMap<Integer,Integer> sequence = getSequence(MainKey);


				Iterator<Integer> iter = sequence.keySet().iterator();
				while (iter.hasNext()) {
					int key = (Integer) iter.next();
					int val = (Integer) sequence.get(key);
					exchange(imgSrc, imgDst, val, key);
				}

				File outputfile = new File(args[1]);
				ImageIO.write(imgDst, "png", outputfile);
				
				System.out.println("done");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
