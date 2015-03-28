package zigzag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ZigzagReaderWriter {
	

	public int[][] _testQuantified = new int[][]{
			new int[]{	1,		2,		6,		7,		15,		16,		28,		29},		
			new int[]{	3,		5,		8,		14,		17,		27,		30,		43},		
			new int[]{	4,		9,		13,		18,		26,		31,		42,		44},		
			new int[]{	10,		12,		19,		25,		32,		41,		45,		54},		
			new int[]{	11,		20,		24,		33,		40,		46,		53,		55},		
			new int[]{	21,		23,		34,		39,		47,		52,		56,		61},		
			new int[]{	22,		35,		38,		48,		51,		57,		60,		62},		
			new int[]{	36,		37,		49,		50,		58,		59,		63,		64}	
		};
	
	public List<Integer> _testZigzagOutput = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 
			28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 
			46, 47, 48, 49, 50, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 59, 60, 61, 
			62, 63, 64);
	
	
	public ZigzagReaderWriter() {
		
	}
	
	
	

	/*
	 * 
	 * For square matrix input only, minimum 3x3
	 * Example trajectory for 8x8 matrix : 
	 * 
		0|01   23       45           67              
		1|  0 1  2     3  4         5  6            7
		2|   0    1   2    3       4    5          6 7
		3|         0 1      2     3      4        5   6        7
		4|          0        1   2        3      4     5      6 7
		5|                    0 1          2    3       4    5   6    7
		6|		               0            1  2         3  4     5  6 7
		7|									 01           23       45   67
	*/
	public List<Integer> write(int[][] quantifiedInput) {
		List<Integer> zigzagVector = new ArrayList<Integer>();
		int highIndex = 0;
		int currentHighRow = 0;
		int currentLowRow = 0;
		
		ArrayList<Integer>[] visitedRows = new ArrayList[quantifiedInput.length];
		
		int[] nbVisitedElements = new int[8];
		
		
		/*
		 * While the last row hasn't been fully visited
		 */
		while (nbVisitedElements[quantifiedInput.length - 1] < quantifiedInput.length) {
			
			// from top left to middle
			if (nbVisitedElements[0] < quantifiedInput.length){
				
				zigzagVector.add(quantifiedInput[0][highIndex]);
				zigzagVector.add(quantifiedInput[0][highIndex+1]);
				
				nbVisitedElements[0] += 2;
				highIndex += 2;
				currentLowRow += 2;
				
				for (int i = 0 + 1; i <= currentLowRow; i++) {
					if (i < quantifiedInput.length) {
						zigzagVector.add(quantifiedInput[i][nbVisitedElements[i]]);
						nbVisitedElements[i] ++;
					}
				}
				
				for (int i = currentLowRow - 1; i > 0; i--) {
					if (i < quantifiedInput.length) {
						zigzagVector.add(quantifiedInput[i][nbVisitedElements[i]]);
						nbVisitedElements[i] ++;
					}
				}
			}
			
			// from middle to bottom right
			else {
				currentHighRow++;
				
				for (int i = currentHighRow + 1; i <= quantifiedInput.length; i++) {
					if (i < quantifiedInput.length) {
						zigzagVector.add(quantifiedInput[i][nbVisitedElements[i]]);
						nbVisitedElements[i] ++;
						
					}
				}
				
				zigzagVector.add(quantifiedInput[quantifiedInput.length - 1][nbVisitedElements[quantifiedInput.length - 1]]);
				nbVisitedElements[quantifiedInput.length - 1] ++;
				currentHighRow++;
				
				for (int i = quantifiedInput.length - 2; i >= currentHighRow; i--) {
					if (i < quantifiedInput.length && nbVisitedElements[i] < quantifiedInput.length) {
						zigzagVector.add(quantifiedInput[i][nbVisitedElements[i]]);
						nbVisitedElements[i] ++;
					}
				}
			}
		}
		return zigzagVector;
	
	}
	
	public void read(List<Integer> zigzagVector) {
		
	}
	
	
	public void print(List<Integer> zigzagVector){

		int c = 0;
		for (Iterator iterator = zigzagVector.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();

			if (c > 10) {
				c = 0;
				System.out.println("");
			}
			
			System.out.print(integer +", ");

			c++;
		}
	}
}
