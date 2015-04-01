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
	
	private static final double R_Y_FACTOR = 1.164;
	private static final double R_CB_FACTOR = 0;
	private static final double R_CR_FACTOR = 1.596;
	
	private static final double G_Y_FACTOR = 1.164;
	private static final double G_CB_FACTOR = -0.392;
	private static final double G_CR_FACTOR = -0.813;
	
	private static final double B_Y_FACTOR = 1.164;
	private static final double B_CB_FACTOR = 2.017;
	private static final double B_CR_FACTOR = 0;
	
	
	private static final double Y_ADJ = 16;
	private static final double CR_ADJ = 128;
	private static final double CB_ADJ = 128;
	
	private double _bpc = 256; // bits per color
	
	PPMReaderWriter ppmRW = new PPMReaderWriter();
	
	public YCbCrReaderWriter() {
		
	}
	
	public YCbCrImageModel writeYCbCr(int[][][] RGBImage) {
		
		//[nbChannels][height][width]
		
		int height = RGBImage[0].length;
		int width = RGBImage[0][0].length;
		
		YCbCrImageModel yCbCrImage = new YCbCrImageModel(RGBImage[0].length, RGBImage[0][0].length);
		

		for (int i = 0; i < height; i++) {
			
			for (int j = 0; j < width; j++) {
				
				double[] normalizedRgbArray = new double[3];
				normalizedRgbArray[0] = (double) (RGBImage[0][i][j] / _bpc);
				normalizedRgbArray[1] = (double) (RGBImage[1][i][j] / _bpc);
				normalizedRgbArray[2] = (double) (RGBImage[2][i][j] / _bpc);
				
				yCbCrImage.addColorComponentFromRGB(0, i, j, (int) Math.ceil(this.RGBtoY(i, j, normalizedRgbArray)));
				yCbCrImage.addColorComponentFromRGB(1, i, j, (int) Math.ceil(this.RGBtoCb(i, j, normalizedRgbArray)));
				yCbCrImage.addColorComponentFromRGB(2, i, j, (int) Math.ceil(this.RGBtoCr(i, j, normalizedRgbArray)));
				
			}
		}
		
		return yCbCrImage;
	}
	
	
	public int[][][] readYCbCr(YCbCrImageModel yCbCrImage) {
		
		// [colorChannel][height][width]
		int[][][] rgbImage = new int[3][yCbCrImage.get_height()][yCbCrImage.get_width()];
		
		for (int i = 0; i < yCbCrImage.get_height(); i++) {
			
			for (int j = 0; j < yCbCrImage.get_width(); j++) {
				
				int[] yCbCrArray = new int[3];
				yCbCrArray[0] = yCbCrImage.get_image()[0][i][j];
				yCbCrArray[1] = yCbCrImage.get_image()[1][i][j];
				yCbCrArray[2] = yCbCrImage.get_image()[2][i][j];
				
				rgbImage[0][i][j] = YCbCrToR(i, j, yCbCrArray);
				rgbImage[1][i][j] = YCbCrToG(i, j, yCbCrArray);
				rgbImage[2][i][j] = YCbCrToB(i, j, yCbCrArray);
			}
		}
		
		
		return rgbImage;
	}
	
	private int RGBtoY(int row, int col, double[] normalizedRgbArray) {
		return (int) ((Y_R_FACTOR * normalizedRgbArray[0] + Y_G_FACTOR *  normalizedRgbArray[1] + Y_B_FACTOR *  normalizedRgbArray[2] + Y_ADJ));
	}

	private int RGBtoCb(int row, int col, double[] normalizedRgbArray) {
		return (int) (((CB_R_FACTOR * normalizedRgbArray[0]) + (CB_G_FACTOR *  normalizedRgbArray[1]) + (CB_B_FACTOR *  normalizedRgbArray[2]) + CB_ADJ));
	}
	
	private int RGBtoCr(int row, int col, double[] normalizedRgbArray) {
		return (int) ((CR_R_FACTOR * normalizedRgbArray[0] + CR_G_FACTOR *  normalizedRgbArray[1] + CR_B_FACTOR *  normalizedRgbArray[2] + CR_ADJ));
	}
	
	/* http://www.equasys.de/colorconversion.html */
	private int YCbCrToR(int row, int col, int[] yCbCrArray){
		return (int) (Math.floor(R_Y_FACTOR * (yCbCrArray[0] - Y_ADJ)) + Math.ceil(R_CR_FACTOR * (yCbCrArray[2] - CR_ADJ)));
	}
	
	private int YCbCrToG(int row, int col, int[] yCbCrArray){
		return (int) (Math.floor(G_Y_FACTOR * (yCbCrArray[0] - Y_ADJ)) + Math.ceil(G_CB_FACTOR * (yCbCrArray[1] - CB_ADJ)) + Math.ceil(G_CR_FACTOR * (yCbCrArray[2] - CR_ADJ)));
	}
	
	private int YCbCrToB(int row, int col, int[] yCbCrArray){
		return (int) (Math.floor(B_Y_FACTOR * (yCbCrArray[0] - Y_ADJ)) + Math.ceil(B_CB_FACTOR * (yCbCrArray[1] - CB_ADJ)));
	}


}
