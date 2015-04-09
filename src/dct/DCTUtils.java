package dct;

public class DCTUtils {
	private static int[][] _testArrayNoDct = new int[][]{
		new int[]{200,202,189,188,189,175,175,175},
		new int[]{200,203,198,188,189,182,178,175},
		new int[]{203,200,200,195,200,187,185,175},
		new int[]{200,200,200,200,197,187,187,187},
		new int[]{200,205,200,200,195,188,187,175},
		new int[]{200,200,200,200,200,190,187,175},
		new int[]{205,200,199,200,191,187,187,175},
		new int[]{210,200,200,200,188,185,187,186}
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
	public static int[][] decode(int[][] input){
		int[][] output = new int[input.length][input[0].length];
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input.length; j++) {
				double firstSomResult = 0;
				for (int u = 0; u < input.length; u++) {
					double secondSomResult = 0;
					for (int v = 0; v < input[i].length; v++) {
						double cosP = cosCalulation(i,u,input.length);
						double cosD = cosCalulation(j,v,input.length);
						double cu = cw(u);
						double cv = cw(v);
						secondSomResult = secondSomResult+(((cosP*cosD)*input[u][v])*((cu*cv)/4));
					}
					firstSomResult = firstSomResult + secondSomResult;
				}
				output[i][j] = (int) firstSomResult;
			}	
		}
		return output;
	}
	public static int[][] encode(int[][] input){
		int[][] output = new int[input.length][input[0].length];
		for (int u = 0; u < input.length; u++) {
			for (int v = 0; v < input.length; v++) {
				double firstSomResult = 0;
				for (int i = 0; i < input.length; i++) {
					double secondSomResult = 0;
					for (int j = 0; j < input[i].length; j++) {
						double cosP = cosCalulation(i,u,input.length);
						double cosD = cosCalulation(j,v,input.length);
						secondSomResult = secondSomResult+((cosP*cosD)*input[i][j]);
					}
					firstSomResult = firstSomResult + secondSomResult;
				}
				double cu = cw(u);
				double cv = cw(v);
				output[u][v] = (int) Math.round(cucvCoefficient(cu,cv,input.length)*firstSomResult);
			}
		}
		return output;
	}
	public static void testEncode(boolean showMatrice){
		System.out.println("DCT Decode test started");
		boolean reslut = test(encode(_testArrayNoDct),_testArrayWithDct,showMatrice);
		System.out.println("The test return "+ reslut);
		System.out.println("///////");
	}
	public static void testDecode(boolean showMatrice){
		System.out.println("DCT Decode test started");
		boolean reslut = test(decode(_testArrayWithDct),_testArrayNoDct,showMatrice);
		System.out.println("The test return "+ reslut);
		System.out.println("///////");
	}
	private static boolean test(int[][] output,int[][] arrTestOutput, boolean showMatrice){
		boolean test = true;
		for (int i = 0; i < output.length; i++) {
			String stringOutput = "";
			for (int j = 0; j < output.length; j++) {
				if(arrTestOutput[i][j] != output[i][j] && arrTestOutput[i][j] != output[i][j]+1 && arrTestOutput[i][j] != output[i][j]-1){
					test = false;
				}
				stringOutput += output[i][j] + ",";
			}
			if(showMatrice){
				System.out.println(stringOutput);
			}
		}
		return test;
	}
	private static double cw(int u){
		if(u == 0)
			return 1/Math.sqrt(2);
		else
			return  1;
	}
	private static double cucvCoefficient(double cu, double cv, int m){
		return (2*(cu*cv)/(Math.sqrt(m*m)));
	}
	private static double cosCalulation(int i, int u, int m){
		return Math.cos((((2*i)+1)*u*Math.PI)/(m*2));
	}
}
