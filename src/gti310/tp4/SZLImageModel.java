package gti310.tp4;

public class SZLImageModel {
	private int[][][][] imageModel; 
	public final int Y_MATRIX = 0;
	public final int Cb_MATRIX = 1;
	public final int Cr_MATRIX = 2;
	
	public SZLImageModel(int height, int width, int[][][] YCbCr){
		this.imageModel = new int[3][(width/8)*(height/8)][8][8];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < YCbCr[i].length; j++) {
				for (int x = 0; x < 8; x++) {
					for (int y = 0; y < 8; y++) {
						imageModel[i][j][x][y] = YCbCr[i][x][y];
					}
				}
			}
			
		}
	}
	public int getPixel(int colorSpace, int bloc, int x, int y){
		return imageModel[colorSpace][bloc][x][y];
	}
	public void writePixel(int colorSpace, int bloc, int x, int y, int pixel){
		imageModel[colorSpace][bloc][x][y] = pixel;
	}
	public int[][] getBlock(int colorSpace, int bloc){
		return imageModel[colorSpace][bloc];
	}
	public void writeBlock(int colorSpace, int blocIndex, int[][]bloc){
		imageModel[colorSpace][blocIndex] = bloc;
	}
	public int[][][] getColorSpace(int colorSpace){
		return imageModel[colorSpace];
	}
	public void writeColorSpace(int colorSpaceIndex, int[][][] colorSpace){
		imageModel[colorSpaceIndex] = colorSpace;
	}
	
}
