package dpcm;

public class Dpcm {
	private static int[][] storedDC = new int[3][];
	
	public static void dpcmInverse(int[] dcs, int colorSpaceIndex){
		storedDC[colorSpaceIndex] = new int[dcs.length];
		storedDC[colorSpaceIndex][0] = dcs[0];
		for (int i = 1; i < dcs.length; i++) {
			storedDC[colorSpaceIndex][i] = dcs[i-1] + dcs[i];
		}
	}
	public static void dpcm(int[][][] colorSpace, int colorSpaceIndex){
		storedDC[colorSpaceIndex] = new int[colorSpace.length];
		for (int i = colorSpace.length-1; i > 0  ; i--) {
			storedDC[colorSpaceIndex][i] = colorSpace[i][0][0] - colorSpace[i-1][0][0];
		}
		storedDC[colorSpaceIndex][0] = colorSpace[0][0][0];
	}
	public static int getDC(int colorSpaceIndex,int index){
		return storedDC[colorSpaceIndex][index];
	}
}
