package dpcm;

public class Dpcm {
	private static int[] storedDC;
	private static int[][][] testDC = new int[][][]{
		new int[][]{new int[]{150}},
		new int[][]{new int[]{155}},
		new int[][]{new int[]{149}},
		new int[][]{new int[]{152}},
		new int[][]{new int[]{144}}
	};
	
	public static int[] writeDC(int[][][] colorSpace){
		storedDC = new int[colorSpace.length];
		for (int i = colorSpace.length-1; i > 0  ; i--) {
			storedDC[i] = colorSpace[i][0][0] - colorSpace[i-1][0][0];
		}
		storedDC[0] = colorSpace[0][0][0];
		return storedDC;
	}
	public static void testWriteDC(){
		int[] test = writeDC(testDC);
		System.out.println(test[0]);
		System.out.println(test[1]);
		System.out.println(test[2]);
		System.out.println(test[3]);
		System.out.println(test[4]);
	}
}
