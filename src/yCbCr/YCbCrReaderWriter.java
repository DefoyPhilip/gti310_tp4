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
				normalizedRgbArray[0] = RGBImage[0][i][j] / _bpc;
				normalizedRgbArray[1] = RGBImage[1][i][j] / _bpc;
				normalizedRgbArray[2] = RGBImage[2][i][j] / _bpc;
				
				yCbCrImage.addColorComponentFromRGB(0, i, j, this.RGBtoY(i, j, normalizedRgbArray));
				yCbCrImage.addColorComponentFromRGB(1, i, j, this.RGBtoCb(i, j, normalizedRgbArray));
				yCbCrImage.addColorComponentFromRGB(2, i, j, this.RGBtoCr(i, j, normalizedRgbArray));
				
				// test
				if (j<100) {
					//System.out.println(RGBImage[0][i][j]);
				}
				
			}
		}
		
		return yCbCrImage;
	}
	
	
	public double[][][] readYCbCr(YCbCrImageModel yCbCrImage) {
		
		// [colorChannel][height][width]
		double[][][] rgbImage = new double[3][yCbCrImage.get_height()][yCbCrImage.get_width()];
		
		for (int i = 0; i < yCbCrImage.get_height(); i++) {
			
			for (int j = 0; j < yCbCrImage.get_width(); j++) {
				
				double[] yCbCrArray = new double[3];
				yCbCrArray[0] = yCbCrImage.get_image()[0][i][j];
				yCbCrArray[1] = yCbCrImage.get_image()[1][i][j];
				yCbCrArray[2] = yCbCrImage.get_image()[2][i][j];
				
				rgbImage[0][i][j] = YCbCrToR(i, j, yCbCrArray);
				rgbImage[1][i][j] = YCbCrToG(i, j, yCbCrArray);
				rgbImage[2][i][j] = YCbCrToB(i, j, yCbCrArray);
				
				// test
				if (j<100) {
					
					// perte d'information comparativement au RGB d'origine
					// normal ? à cause des arrondissements
					// pour le moment, on garde les informations en type Double (au lieu de int) et on fait
					// un math.ceil pour garder le plus d'information possible
					System.out.println(rgbImage[0][i][j]);
				}
				
			}
		}
		
		
		return rgbImage;
	}
	
	private double RGBtoY(int row, int col, double[] normalizedRgbArray) {
		return (double) (Y_R_FACTOR * normalizedRgbArray[0] + Y_G_FACTOR *  normalizedRgbArray[1] + Y_B_FACTOR *  normalizedRgbArray[2] + Y_ADJ);
	}

	private double RGBtoCb(int row, int col, double[] normalizedRgbArray) {
		return (double) (CB_R_FACTOR * normalizedRgbArray[0] + CB_G_FACTOR *  normalizedRgbArray[1] + CB_B_FACTOR *  normalizedRgbArray[2] + CB_ADJ);
	}
	
	private double RGBtoCr(int row, int col, double[] normalizedRgbArray) {
		return (double) (CR_R_FACTOR * normalizedRgbArray[0] + CR_G_FACTOR *  normalizedRgbArray[1] + CR_B_FACTOR *  normalizedRgbArray[2] + CR_ADJ);
	}
	
	/* http://www.equasys.de/colorconversion.html */
	private double YCbCrToR(int row, int col, double[] yCbCrArray){
		return (double) (Math.ceil(R_Y_FACTOR * (yCbCrArray[0] - Y_ADJ)) + Math.ceil(R_CR_FACTOR * (yCbCrArray[2] - CR_ADJ)));
	}
	
	private double YCbCrToG(int row, int col, double[] yCbCrArray){
		return (double) (Math.ceil(G_Y_FACTOR * (yCbCrArray[0] - Y_ADJ)) + Math.ceil(G_CB_FACTOR * (yCbCrArray[1] - CB_ADJ)) + Math.ceil(G_CR_FACTOR * (yCbCrArray[2] - CR_ADJ)));
	}
	
	private double YCbCrToB(int row, int col, double[] yCbCrArray){
		return (double) (Math.ceil(B_Y_FACTOR * (yCbCrArray[0] - Y_ADJ)) + Math.ceil(B_CB_FACTOR * (yCbCrArray[1] - CB_ADJ)));
	}


}
