package gti310.tp4.dct;

public class DCTEncoder {
	int[][] _testArray = new int[][]{
		new int[]{200,202,189,188,189,175,175,175},
		new int[]{200,203,198,188,189,182,178,175},
		new int[]{203,200,200,195,200,187,185,175},
		new int[]{200,200,200,200,197,187,187,187},
		new int[]{200,205,200,200,195,188,187,175},
		new int[]{200,200,200,200,200,190,187,175},
		new int[]{205,200,199,200,191,187,187,175},
		new int[]{210,200,200,200,188,185,187,186},
	};
	int [][] _testOutputArray = new int[][]{
		new int[]{1539,65,-12,4,1,2,-8,5},
		new int[]{-16,3,2,0,0,-11,-2,3},
		new int[]{-12,6,11,-1,3,0,1,-2},
		new int[]{-8,3,-4,2,-2,-3,-5,-2},
		new int[]{0,-2,7,-5,4,0,-1,-4},
		new int[]{0,-3,-1,0,4,1,-1,0},
		new int[]{3,-2,-3,3,3,-1,-1,3},
		new int[]{-2,5,-2,4,-2,2,-3,0},
	};
	int [][] _encodedBloc = new int[8][8];
	public DCTEncoder(){
		for (int u = 0; u < 8; u++) {
			for (int v = 0; v < 8; v++) {
				double firstSomResult = 0;
				for (int i = 0; i < _testArray.length; i++) {
					double secondSomResult = 0;
					for (int j = 0; j < _testArray[i].length; j++) {
						double cosP = Math.cos((((2*i)+1)*u*Math.PI)/16);
						double cosD = Math.cos((((2*j)+1)*v*Math.PI)/16);
						secondSomResult = secondSomResult+((cosP*cosD)*_testArray[i][j]);
					}
					firstSomResult = firstSomResult + secondSomResult;
				}
				double cu;
				double cv;
				if(u == 0)
					cu = 1/Math.sqrt(2);
				else
					cu = 1;
				if(v == 0)
					cv = 1/Math.sqrt(2);
				else
					cv = 1;
				_encodedBloc[u][v] = (int) (((cu*cv)/4)*firstSomResult);
			}
			System.out.println(
				_encodedBloc[u][0] + "," +
				_encodedBloc[u][1] + "," +
				_encodedBloc[u][2] + "," +
				_encodedBloc[u][3] + "," +
				_encodedBloc[u][4] + "," +
				_encodedBloc[u][5] + "," +
				_encodedBloc[u][6] + "," +
				_encodedBloc[u][7]
			);
		}
	}
}
