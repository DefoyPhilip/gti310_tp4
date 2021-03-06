package acUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ACUtils {

	/**
	 * Return a linked list that contain every ac couple to send
	 * them to the entropy class before writing the file
	 * 
	 * Notation O(n^2)
	 * @param zigzag
	 * @return
	 */
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
	/**
	 * Read the info from the entropy file and create
	 * an 8x8 bloc
	 * 
	 * Notation O(n^2)
	 * @param runlengthValueList
	 * @return
	 */
	public static int[][] readAC(List<int[]> runlengthValueList) {
		
		ArrayList<Integer> zigzagList = new ArrayList<Integer>();
		int[][] zigzagArray = new int[8][8];
		int startingCol = 1;
		
		
		for (int[] couples : runlengthValueList) {
			if (couples[0] == 0 && couples[1] != 0) {
				zigzagList.add(couples[1]);
			} else if (couples[0] > 0) {
				for (int k = 0; k < couples[0]; k++) {
					zigzagList.add(0);
				}
				zigzagList.add(couples[1]);
			} 
		}
		
		// transform list into 2D array
		for (int i = 0; i < zigzagArray.length; i++) {
			for (int j = startingCol; j < zigzagArray[i].length; j++) {
				if (!zigzagList.isEmpty())
					zigzagArray[i][j] = zigzagList.remove(0);
				else
					break;
			}
			
			startingCol = 0;
		}
		
		return zigzagArray;
	}
}
