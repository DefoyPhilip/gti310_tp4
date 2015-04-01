package zigzag;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;

public class ZigzagReaderWriterTest {

	public int[][] quantified = new int[][]{
			new int[]{	1,		2,		6,		7,		15,		16,		28,		29},		
			new int[]{	3,		5,		8,		14,		17,		27,		30,		43},		
			new int[]{	4,		9,		13,		18,		26,		31,		42,		44},		
			new int[]{	10,		12,		19,		25,		32,		41,		45,		54},		
			new int[]{	11,		20,		24,		33,		40,		46,		53,		55},		
			new int[]{	21,		23,		34,		39,		47,		52,		56,		61},		
			new int[]{	22,		35,		38,		48,		51,		57,		60,		62},		
			new int[]{	36,		37,		49,		50,		58,		59,		63,		64}	
		};
	
	public LinkedList<Integer> zigzagVector = new LinkedList <Integer> (Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 
			28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 
			46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 
			62, 63, 64));
	
	private ZigzagReaderWriter zigzag = new ZigzagReaderWriter();
	
	@Test
	public void testWrite() {
		LinkedList<Integer> outputVector = zigzag.write(quantified);
		assertThat(outputVector, is(zigzagVector));
		
	}
	
	@Test
	public void testRead() {
		int[][] quantifiedOutput = zigzag.read(zigzagVector);
		assertArrayEquals(quantified, quantifiedOutput);
	}

}
