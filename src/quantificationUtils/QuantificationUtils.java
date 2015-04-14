package quantificationUtils;

public class QuantificationUtils {
	private static int[][] tableY = new int[][]{
		new int[]{16,40,40,40,40,40,51,61},
		new int[]{40,40,40,40,40,58,60,55},
		new int[]{40,40,40,40,40,57,69,56},
		new int[]{40,40,40,40,51,87,80,62},
		new int[]{40,40,40,56,68,109,103,77},
		new int[]{40,40,55,64,81,104,113,92},
		new int[]{49,64,78,87,103,121,120,101},
		new int[]{72,92,95,98,112,100,103,95}
	};
	private static int[][] tableCbCr = new int[][]{
		new int[]{17,40,40,95,95,95,95,95},
		new int[]{40,40,40,95,95,95,95,95},
		new int[]{40,40,40,95,95,95,95,95},
		new int[]{40,40,95,95,95,95,95,95},
		new int[]{95,95,95,95,95,95,95,95},
		new int[]{95,95,95,95,95,95,95,95},
		new int[]{95,95,95,95,95,95,95,95},
		new int[]{95,95,95,95,95,95,95,95}
	};
	private static int [][] _testArrayWithDct = new int[][]{
		new int[]{1539,65,-12,4,1,2,-8,5},
		new int[]{-16,3,2,0,0,-11,-2,3},
		new int[]{-12,6,11,-1,3,0,1,-2},
		new int[]{-8,3,-4,2,-2,-3,-5,-2},
		new int[]{0,-2,7,-5,4,0,-1,-4},
		new int[]{0,-3,-1,0,4,1,-1,0},
		new int[]{3,-2,-3,3,3,-1,-1,3},
		new int[]{-2,5,-2,4,-2,2,-3,0}
	};
	private static int [][] _testArrayQuant = new int[][]{
		new int[]{107,2,0,0,0,0,0,0},
		new int[]{0,0,0,0,0,0,0,0},
		new int[]{0,0,0,0,0,0,0,0},
		new int[]{0,0,0,0,0,0,0,0},
		new int[]{0,0,0,0,0,0,0,0},
		new int[]{0,0,0,0,0,0,0,0},
		new int[]{0,0,0,0,0,0,0,0},
		new int[]{0,0,0,0,0,0,0,0}
	};
	public static int[][] quantY(int[][] input, int fq){
		return quant(input,fq,tableY);
	}
	public static int[][] quantCbCr(int[][] input, int fq){
		return quant(input,fq,tableCbCr);
	}
	public static int[][] dequantY(int[][] input, int fq){
		return dequant(input,fq,tableY);
	}
	public static int[][] dequantCbCr(int[][] input, int fq){
		return dequant(input,fq,tableCbCr);
	}
	/**
	 * Apply quantification to an 8x8 bloc with an alpha coefficient
	 * and a quantification table
	 * 
	 * Notation O(n^2)
	 * @param input
	 * @param fq
	 * @param table
	 * @return
	 */
	private static int[][] quant(int[][] input, int fq, int[][] table){
		if(fq == 100){
			return input;
		}
		float alpha = getAlpha(fq);
		int[][] output = new int[input.length][input.length];
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input.length; j++) {
				output[i][j] = Math.round(input[i][j]/(alpha*table[i][j]));
			}
		}
		return output;
	}
	/**
	 * Undo quantification to an 8x8 bloc with an alpha coefficient
	 * and a quantification table
	 * 
	 * Notation O(n^2)
	 * @param input
	 * @param fq
	 * @param table
	 * @return
	 */
	private static int[][] dequant(int[][] input, int fq, int[][] table){
		if(fq == 100){
			return input;
		}
		float alpha = getAlpha(fq);
		int[][] output = new int[input.length][input.length];
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input.length; j++) {
				output[i][j] = Math.round(input[i][j]*(alpha*table[i][j]));
			}
		}
		return output;
	}
	/**
	 * Calculate the alpha coefficient based on the quality factor given by the user
	 * @param fq
	 * @return
	 */
	private static float getAlpha(int fq){
		float alpha = 0;
		if(fq >= 1 && fq < 50){
			alpha = 50/fq;
		}
		else if(fq >= 50 && fq <= 99){
			alpha = (float)(200-(2*fq))/100;
		}
		else{
			System.out.println("Facteur de qualit� non support�");
		}
		return alpha;
	}
	public static void testQuant(){
		int[][] testOutput = quantY(_testArrayWithDct,55);
		for (int i = 0; i < testOutput.length; i++) {
			String stringOutput = "";
			for (int j = 0; j < testOutput.length; j++) {
				stringOutput += testOutput[i][j] + ",";
			}
			System.out.println(stringOutput);
		}
	}
	public static void testDequant(){
		int[][] testOutput = dequantY(_testArrayQuant,55);
		for (int i = 0; i < testOutput.length; i++) {
			String stringOutput = "";
			for (int j = 0; j < testOutput.length; j++) {
				stringOutput += testOutput[i][j] + ",";
			}
			System.out.println(stringOutput);
		}
	}
}
