package yCbCr;

public class YCbCrImageModel {
	int[][][] _image;

	public YCbCrImageModel(int height, int width){
		_image = new int[3][height][width];
	}
	
	public void addColorComponentFromRGB(int channelIndex, int row, int col, int value) {
		_image[channelIndex][row][col] = value;
	}
}
