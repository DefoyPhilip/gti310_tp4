package yCbCr;

public class YCbCrImageModel {
	int[][][] _image;
	double[][][] _imageDouble;
	int _width;
	int _height;

	/**
	 * Image model used to convert RGB images to YCbCr
	 * 
	 * @param height
	 * @param width
	 */
	
	public YCbCrImageModel(int height, int width){
		_width = width;
		_height = height;
		_image = new int[3][height][width];
		_imageDouble = new double[3][height][width];
	}
	
	public void addColorComponentFromRGB(int channelIndex, int row, int col, int value) {
		_image[channelIndex][row][col] = value;
	}
	
	public void addColorComponentFromRGBDouble(int channelIndex, int row, int col, double value) {
		_imageDouble[channelIndex][row][col] = value;
	}


	public int[][][] get_image() {
		return _image;
	}
	public void set_image(int[][][] image){
		_image = image;
	}
	
	public double[][][] get_imageDouble() {
		return _imageDouble;
	}


	public int get_width() {
		return _width;
	}

	public int get_height() {
		return _height;
	}


	
	
	
}
