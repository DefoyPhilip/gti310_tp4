package dpcm;

public class Dpcm {
	private static int[][] storedDC = new int[3][];
	/**
	 * Recalculate the first pixel of a 8x8 bloc after
	 * getting it from the entropy
	 * 
	 * Notation O(N)
	 * @param dcs
	 * @param colorSpaceIndex
	 */
	public static void dpcmInverse(int[] dcs, int colorSpaceIndex){
		storedDC[colorSpaceIndex] = new int[dcs.length];
		storedDC[colorSpaceIndex][0] = dcs[0];
		for (int i = 1; i < dcs.length; i++) {
			storedDC[colorSpaceIndex][i] = storedDC[colorSpaceIndex][i-1] + dcs[i];
		}
	}
	/**
	 * Write DC an stored them for future use
	 * 
	 * Notation O(N)
	 * @param colorSpace
	 * @param colorSpaceIndex
	 */
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
	public static int[] getDCs(int colorSpaceIndex){
		return storedDC[colorSpaceIndex];
	}
	public static void logDcs(){
		String line = "";
		for (int i = 0; i < storedDC[0].length; i++) {
			line += storedDC[0][i]+",";
		}
		System.out.println(line);
	}
}
