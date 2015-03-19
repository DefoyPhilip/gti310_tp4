package yCbCr;

import gti310.tp4.PPMReaderWriter;

public class YCbCrEncoder {
	
	PPMReaderWriter ppmRW = new PPMReaderWriter();
	
	public YCbCrEncoder() {
		
	}
	
	public void getRGB() {
		int[][][] image = PPMReaderWriter.readPPMFile("medias/lena.ppm");
		
		for (int[][] is : image) {
			for (int[] is2 : is) {
				for (int i : is2) {
					System.out.println(i);
				}
			}
		}
	}

}
