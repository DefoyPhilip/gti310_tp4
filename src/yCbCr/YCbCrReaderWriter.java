package yCbCr;

import gti310.tp4.PPMReaderWriter;

public class YCbCrReaderWriter {
	
	private static final double Y_R_FACTOR = 65.481;
	private static final double Y_G_FACTOR = 128.533;
	private static final double Y_B_FACTOR = 24.966;
	private static final double CB_R_FACTOR = -37.797;
	private static final double CB_G_FACTOR = -74.203;
	private static final double CB_B_FACTOR = 112;
	private static final double CR_R_FACTOR = 112;
	private static final double CR_G_FACTOR = -93.786;
	private static final double CR_B_FACTOR = -18.214;
	private static final short Y_ADJ = 16;
	private static final short CR_ADJ = 128;
	private static final double CB_ADJ = 128;
	
	private double _bpc = 256; // bits per color
	
	PPMReaderWriter ppmRW = new PPMReaderWriter();
	
	public YCbCrReaderWriter() {
		
	}
	
	public void getRGB() {
		
		//[nbChannels][height][width]
		int[][][] RGBImage = PPMReaderWriter.readPPMFile("medias/lena.ppm");
		
		int height = RGBImage[0].length;
		int width = RGBImage[0][0].length;
		
		YCbCrImageModel yCbCrImage = new YCbCrImageModel(RGBImage[0].length, RGBImage[0][0].length);
		

		for (int i = 0; i < height; i++) {
			
			for (int j = 0; j < width; j++) {
				
				double[] normalizedRgbArray = new double[3];
				normalizedRgbArray[0] = RGBImage[0][i][j] / _bpc;
				normalizedRgbArray[1] = RGBImage[1][i][j] / _bpc;
				normalizedRgbArray[2] = RGBImage[2][i][j] / _bpc;
				
				yCbCrImage.addColorComponentFromRGB(0, i, j, this.toY(i, j, normalizedRgbArray));
				yCbCrImage.addColorComponentFromRGB(1, i, j, this.toCb(i, j, normalizedRgbArray));
				yCbCrImage.addColorComponentFromRGB(2, i, j, this.toCr(i, j, normalizedRgbArray));
				
			}
		}
	}
	
	private int toY(int row, int col, double[] normalizedRgbArray) {
		return (int) (Y_R_FACTOR * normalizedRgbArray[0] + Y_G_FACTOR *  normalizedRgbArray[1] + Y_B_FACTOR *  normalizedRgbArray[2] + Y_ADJ);
	}

	private int toCb(int row, int col, double[] normalizedRgbArray) {
		return (int) (CB_R_FACTOR * normalizedRgbArray[0] + CB_G_FACTOR *  normalizedRgbArray[1] + CB_B_FACTOR *  normalizedRgbArray[2] + CB_ADJ);
	}
	
	private int toCr(int row, int col, double[] normalizedRgbArray) {
		return (int) (CR_R_FACTOR * normalizedRgbArray[0] + CR_G_FACTOR *  normalizedRgbArray[1] + CR_B_FACTOR *  normalizedRgbArray[2] + CR_ADJ);
	}


}
