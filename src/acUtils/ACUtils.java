package acUtils;

import java.util.LinkedList;

public class ACUtils {

	public static LinkedList<int[]> prepareACWrite(int[][] zigzag) {
		LinkedList<int[]> runlengthValueList = new LinkedList<int[]>();
		
		
		int zerosCounter = 0;
		
		// skipping first element (DC) during first iteration
		int startingCol = 1;
		
		for (int i = 0; i < zigzag.length; i++) {;
			
			for (int j = startingCol; j < zigzag[i].length; j++) {
	
				if (zigzag[i][j] != 0) {
					int[] runlengthValue = new int[2];
					runlengthValue[0] = zerosCounter;
					runlengthValue[1] = zigzag[i][j];
					runlengthValueList.add(runlengthValue);
					
					zerosCounter = 0;
				} else {
					zerosCounter++;
				}
				
				// EOB
				if (i == zigzag.length-1 && j == zigzag.length-1) {
					int[] EOBrunlengthValue = new int[2];
					runlengthValueList.add(EOBrunlengthValue);
				}
			}
			
			startingCol = 0;
		}
		
		return runlengthValueList;
		
	}
}
