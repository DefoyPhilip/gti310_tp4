package zigzag;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ZigzagReaderWriter {
	
	private static final int NO_EDGE = 0;
	private static final int TOP_EDGE = 1;
	private static final int RIGHT_EDGE = 2;
	private static final int BOTTOM_EDGE = 3;
	private static final int LEFT_EDGE = 4;
	private static final int UP = 5;
	private static final int DOWN = 6;
	
	
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
	public int[][] write(int[][] quantifiedInput) {
		LinkedList<Integer> zigzagVector = new LinkedList<Integer>();
		
		int currentRow = 0;
		int currentCol = 0;
		
		int direction = DOWN;
		
		int size = quantifiedInput.length;
		
		int[] nbVisitedElements = new int[size];
		
		int[][] zigzagOutput = new int[size][size];
		
		
		/*
		 * While the last row hasn't been fully visited
		 * starting from the first row
		 */
		while (nbVisitedElements[size - 1] < size) {
			int edge = whichEdge(currentRow, currentCol, size, size);
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
					zigzagVector.add(quantifiedInput[size-1][currentCol]);
					zigzagVector.add(quantifiedInput[size-1][currentCol + 1]);
					nbVisitedElements[currentRow] += 2;
					currentRow = size-2;
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
					zigzagVector.add(quantifiedInput[currentRow][size-1]);
					zigzagVector.add(quantifiedInput[currentRow+1][size-1]);
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
		
		
		// put in 2d array
		for (int i = 0; i < zigzagOutput.length; i++) {
			for (int j = 0; j < zigzagOutput[i].length; j++) {
				zigzagOutput[i][j] = zigzagVector.remove(0);
			}
		}
		
		return zigzagOutput;
	}

	/*
	 * Reads a vector and outputs a square matrix
	 * by writing values in zigzag
	 */
	public int[][] read(int[][] zigzagArray) {
		int size = zigzagArray.length;
		int[][] quantifiedOutput = new int[size][size];
		LinkedList<Integer> zigzagVector = new LinkedList();
		
		int[] nbInsertedElements = new int[size];
		
		int currentRow = size-1;
		int currentCol = size-1;
		
		int direction = UP;
		
		
		/*
		 * Convert input to 1D vector
		 */
		for (int i = 0; i < zigzagArray.length; i++) {
			for (int j = 0; j < zigzagArray[i].length; j++) {
				zigzagVector.add(zigzagArray[i][j]);
			}
		}
		
		/*
		 * While the first row hasn't been fully visited
		 * starting from the last row
		 */
		while (nbInsertedElements[0] < size) {
			int edge = whichEdge(currentRow, currentCol, size, size);
			//System.out.println("[" + currentRow + "][" + currentCol + "]" + " => " + edge);
			
			switch (edge) {
				case BOTTOM_EDGE :
					quantifiedOutput[size-1][currentCol] = zigzagVector.remove(zigzagVector.size()-1);
					quantifiedOutput[size-1][currentCol-1] = zigzagVector.remove(zigzagVector.size()-1);
					nbInsertedElements[currentRow] +=2;
					direction = UP;
					currentRow--;
					break;
				
				case TOP_EDGE : 
					quantifiedOutput[0][currentCol] = zigzagVector.remove(zigzagVector.size()-1);
					quantifiedOutput[0][currentCol-1] = zigzagVector.remove(zigzagVector.size()-1);
					nbInsertedElements[0] +=2;
					direction = DOWN;
					currentRow++;
					currentCol -=2;
					break;
				
				case LEFT_EDGE :
					quantifiedOutput[currentRow][0] = zigzagVector.remove(zigzagVector.size()-1);
					quantifiedOutput[currentRow-1][0] = zigzagVector.remove(zigzagVector.size()-1);
					nbInsertedElements[currentRow] ++;
					nbInsertedElements[currentRow-1] ++;
					direction = UP;
					currentRow-=2;
					currentCol++;
					break;
					
				case RIGHT_EDGE : 
					quantifiedOutput[currentRow][currentCol] = zigzagVector.remove(zigzagVector.size()-1);
					quantifiedOutput[currentRow-1][currentCol] = zigzagVector.remove(zigzagVector.size()-1);
					nbInsertedElements[currentRow] ++;
					nbInsertedElements[currentRow-1] ++;
					direction = DOWN;
					currentCol--;
					break;
					
				case NO_EDGE : 
					quantifiedOutput[currentRow][currentCol] = zigzagVector.remove(zigzagVector.size()-1);
					nbInsertedElements[currentRow] ++;
					
					if (direction == DOWN){
						currentRow++;
						currentCol--;
					}
					else{
						currentRow--;
						currentCol++;
					}
					
					break;
					
				default :
					break;
			}
			
		}

		return quantifiedOutput;
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
		
		System.out.println("/////// zigzag write //////");
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
		
		System.out.println("");
	}
	
	public void print(int[][] quantified){
		System.out.println("");
		System.out.println("/////// zigzag read //////");
		for (int i = 0; i < quantified.length; i++) {
			for (int j = 0; j < quantified[i].length; j++) {
				System.out.print(quantified[i][j] + ",");
			}
			System.out.println("");
		}
		
		System.out.println("");

	}
	

}
