package zigzag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ZigzagReaderWriter {
	
	private static final int NO_EDGE = 0;
	private static final int TOP_EDGE = 1;
	private static final int RIGHT_EDGE = 2;
	private static final int BOTTOM_EDGE = 3;
	private static final int LEFT_EDGE = 4;
	private static final int UP = 5;
	private static final int DOWN = 6;
	
	
	
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
		
		int currentRow = 0;
		int currentCol = 0;
		
		int direction = DOWN;
		
		int width = quantifiedInput.length;
		int height = quantifiedInput.length;
		
		int[] nbVisitedElements = new int[width];
		
		/*
		 * While the last row hasn't been fully visited
		 */
		while (nbVisitedElements[width - 1] < width) {
			int edge = whichEdge(currentRow, currentCol, height, width);
			//System.out.println("[" + currentRow + "][" + currentCol + "]" + " => " + edge);
			switch (edge) {
				case TOP_EDGE :
					zigzagVector.add(quantifiedInput[currentRow][currentCol]);
					zigzagVector.add(quantifiedInput[currentRow][currentCol + 1]);
					nbVisitedElements[currentRow] += 2;
					currentRow++;
					direction = DOWN;
					break;
				
				case BOTTOM_EDGE : 
					zigzagVector.add(quantifiedInput[height-1][currentCol]);
					zigzagVector.add(quantifiedInput[height-1][currentCol + 1]);
					nbVisitedElements[currentRow] += 2;
					currentRow = height-2;
					currentCol += 2;
					direction = UP;
					break;
				
				case LEFT_EDGE :
					zigzagVector.add(quantifiedInput[currentRow][0]);
					zigzagVector.add(quantifiedInput[currentRow+1][0]);
					nbVisitedElements[currentRow] ++;
					nbVisitedElements[currentRow+1] ++;
					currentCol++;
					direction = UP;
					break;
					
				case RIGHT_EDGE : 
					zigzagVector.add(quantifiedInput[currentRow][width-1]);
					zigzagVector.add(quantifiedInput[currentRow+1][width-1]);
					nbVisitedElements[currentRow] ++;
					nbVisitedElements[currentRow+1] ++;
					currentCol--;
					currentRow += 2;
					direction = DOWN;
					break;
					
				case NO_EDGE : 
					zigzagVector.add(quantifiedInput[currentRow][currentCol]);
					nbVisitedElements[currentRow] ++;
					
					if (direction == UP){
						currentRow--;
						currentCol++;
					}
					else{
						currentRow++;
						currentCol--;
					}
					
					break;
					
				default :
					break;
			}
			
		}
		return zigzagVector;
	}


	public void read(List<Integer> zigzagVector) {
		
	}
	
	private int whichEdge(int row, int col, int height, int width){
		if (row == 0)
			return TOP_EDGE;
		if (row == height - 1)
			return BOTTOM_EDGE;
		if (col == 0)
			return LEFT_EDGE;
		if (col == width - 1)
			return RIGHT_EDGE;

		return NO_EDGE;
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
