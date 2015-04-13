package yCbCr;

public class YCbCrImageModel {
	int[][][] _image;
	double[][][] _imageDouble;
	int _width;
	int _height;
	int[] _channelCurrentBlockCol = new int[3];
	int[] _channelCurrentBlockRow = new int[3];
	

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
	
	
	public void stitchBlock(int channelIndex, int[][] block) {
		for (int i = 0; i < block.length; i++) {
			for (int j = 0; j < block[i].length; j++) {		
				this._image[channelIndex][_channelCurrentBlockRow[channelIndex]*8 + i][_channelCurrentBlockCol[channelIndex]*8 + j] = block[i][j];
				
			}
		}
		
		_channelCurrentBlockCol[channelIndex]++;

		if (_channelCurrentBlockCol[channelIndex] == (_width / 8)) {
			_channelCurrentBlockCol[channelIndex] = 0;
			_channelCurrentBlockRow[channelIndex]++;
		}
		
		
		
	}


	public int[][][] get_image() {
		return _image;
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
