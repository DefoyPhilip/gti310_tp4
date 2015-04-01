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
	public static int[][] quantY(int[][] input, int fq){
		return quant(input,fq,tableY);
	}
	public static int[][] quantCbCr(int[][] input, int fq){
		return quant(input,fq,tableCbCr);
	}
	private static int[][] quant(int[][] input, int fq, int[][] table){
		float alpha = getAlpha(fq);
		int[][] output = new int[input.length][input.length];
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input.length; j++) {
				output[i][j] = Math.round(input[i][j]/(alpha*table[i][j]));
			}
		}
		return output;
	}
	private static float getAlpha(int fq){
		float alpha = 0;
		if(fq >= 1 || fq <= 50){
			alpha = 50/fq;
		}
		else if(fq > 50 || fq <= 99){
			alpha = (200-(2*fq))/100;
		}
		else{
			System.out.println("Facteur de qualité non supporté");
		}
		return alpha;
	}
}
