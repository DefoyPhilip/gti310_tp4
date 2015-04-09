package acUtils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import acUtils.ACUtils;

public class ACUtilsTest {
	
	public int[][] zigzag = new int[][]{
			new int[]{	96,		6,		-1,		-1,		0,		-1,		0,		0},		
			new int[]{	0,		1,		0,		0,		1,		0,		0,		0},		
			new int[]{	0,		0,		0,		0,		0,		0,		0,		0},			
			new int[]{	0,		0,		0,		0,		0,		0,		0,		0},		
			new int[]{	0,		0,		0,		0,		0,		0,		0,		0},			
			new int[]{	0,		0,		0,		0,		0,		0,		0,		0},			
			new int[]{	0,		0,		0,		0,		0,		0,		0,		0},			
			new int[]{	0,		0,		0,		0,		0,		0,		0,		0}
		};
	
	public List<int[]> runlengthValueList = new ArrayList<int[]>();
	

	@Test
	public void prepareACWriteTest() {
		System.out.println("Testing prepareACWriteTest()");
		initRunLengthValueList();
		ACUtils acUtils = new ACUtils();
		LinkedList<int[]> outputList = acUtils.prepareACWrite(zigzag);
		
		int i = 0;
		for (int[] is : outputList) {
			assertArrayEquals(is, runlengthValueList.get(i));
			i++;
		}
		
	}
	
	private void initRunLengthValueList(){
		int[] runlengthValue = new int[]{0,6};
		runlengthValueList.add(runlengthValue);
		
		runlengthValue = new int[]{0,-1};
		runlengthValueList.add(runlengthValue);
		
		runlengthValue = new int[]{0,-1};
		runlengthValueList.add(runlengthValue);
		
		runlengthValue = new int[]{1,-1};
		runlengthValueList.add(runlengthValue);
		
		runlengthValue = new int[]{3,1};
		runlengthValueList.add(runlengthValue);
		
		runlengthValue = new int[]{2,1};
		runlengthValueList.add(runlengthValue);
		
		runlengthValue = new int[]{0,0};
		runlengthValueList.add(runlengthValue);
		
	}

}
