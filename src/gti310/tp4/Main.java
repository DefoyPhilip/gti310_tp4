package gti310.tp4;
import gti310.tp4.dct.DCTUtils;

/**
 * The Main class is where the different functions are called to either encode
 * a PPM file to the Squeeze-Light format or to decode a Squeeze-Ligth image
 * into PPM format. It is the implementation of the simplified JPEG block 
 * diagrams.
 * 
 * @author Fran?ois Caron
 */
public class Main {

	/*
	 * The entire application assumes that the blocks are 8x8 squares.
	 */
	public static final int BLOCK_SIZE = 8;
	
	/*
	 * The number of dimensions in the color spaces.
	 */
	public static final int COLOR_SPACE_SIZE = 3;
	
	/*
	 * The RGB color space.
	 */
	public static final int R = 0;
	public static final int G = 1;
	public static final int B = 2;
	
	/*
	 * The YUV color space.
	 */
	public static final int Y = 0;
	public static final int U = 1;
	public static final int V = 2;
	
	/**
	 * The application's entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Squeeze Light Media Codec !");
		DCTUtils.testEncode(true);
		DCTUtils.testDecode(true);
	}
}
