package yCbCr;

public class YCbCrImageModel {
	double[][][] _image;
	int _width;
	int _height;

	public YCbCrImageModel(int height, int width){
		_width = width;
		_height = height;
		_image = new double[3][height][width];
	}
	
	public void addColorComponentFromRGB(int channelIndex, int row, int col, double value) {
		_image[channelIndex][row][col] = value;
	}

	public double[][][] get_image() {
		return _image;
	}

	public int get_width() {
		return _width;
	}

	public int get_height() {
		return _height;
	}


	
	
	
}
