package gti310.tp4;
import java.util.Arrays;
import java.util.LinkedList;

import acUtils.ACUtils;
import quantificationUtils.QuantificationUtils;
import dct.DCTUtils;
import dpcm.Dpcm;
import yCbCr.YCbCrImageModel;
import yCbCr.YCbCrReaderWriter;
import zigzag.ZigzagReaderWriter;

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
		System.out.println(args[0]);
		System.out.println(args[1]);
		if(args.length == 3){
			System.out.println(args[2]);
			encode(args);
		}
		else if (args.length == 2){
			decode(args);
		}
		
	}
	private static void encode(String[] args){
		int fq = Integer.parseInt(args[0]);

		/* YCBCR*/
		YCbCrReaderWriter yCbCrCodec = new YCbCrReaderWriter();
		int[][][] RGBImage = PPMReaderWriter.readPPMFile(args[1]); // testing
		YCbCrImageModel yCbCrImage = yCbCrCodec.writeYCbCr(RGBImage);
		yCbCrCodec.readYCbCr(yCbCrImage);
		
		SZLImageModel image = new SZLImageModel(yCbCrImage.get_height(),yCbCrImage.get_width(),yCbCrImage.get_image());
		System.out.println(image.getBlockQuantity());
			for (int i = 0; i < image.getBlockQuantity(); i++) {
				image.writeBlock(Y, i,
						ZigzagReaderWriter.write(
								QuantificationUtils.quantY(
										DCTUtils.encode(image.getBlock(Y, i)
								),fq)
						)
				);
				image.writeBlock(U, i,
						ZigzagReaderWriter.write(
								QuantificationUtils.quantCbCr(
										DCTUtils.encode(image.getBlock(U, i)
								), fq)
						)
				);
				image.writeBlock(V, i,
						ZigzagReaderWriter.write(
								QuantificationUtils.quantCbCr(
										DCTUtils.encode(image.getBlock(V, i)
								), fq)
						)
				);
			}
		Dpcm.dpcm(image.getColorSpace(Y), Y);
		Dpcm.dpcm(image.getColorSpace(U), U);
		Dpcm.dpcm(image.getColorSpace(V), V);
		
		for (int i = 0; i < image.getBlockQuantity(); i++) {
			for (int j = 0; j < 3; j++) {
				Entropy.writeDC(Dpcm.getDC(j,i));
				LinkedList<int[]>ac = ACUtils.prepareACWrite(image.getBlock(j, i));
				for (int[] is : ac) {
					Entropy.writeAC(is[0], is[1]);
				}
			}
		}
		SZLReaderWriter.writeSZLFile(args[2], yCbCrImage.get_height(), yCbCrImage.get_width(), fq);
	}
	private static void decode(String[] args){
		int[] header = SZLReaderWriter.readSZLFile(args[0]);
		int blocQuanity = (header[1]/8)*(header[0]/8) * header[2];
		for (int i = 0; i < blocQuanity; i++) {
			int dc = Entropy.readDC();
			int[] ac;
			boolean val;
			LinkedList<int[]> runlengthValueList = new LinkedList<int[]>();
			do {
				ac = Entropy.readAC();
				int[] runlengthValue = new int[2];
				runlengthValue[0] = ac[0];
				runlengthValue[1] = ac[1];
				runlengthValueList.add(runlengthValue);
				val = ac[0] == ac[1] && ac[0] == 0;
			} while (!val);
			System.out.println("bloc:"+(int)(Math.ceil(i/3)+1));
			if( i % 3 == 0)
				System.out.println("Y");
			else if(i % 3 == 1)
				System.out.println("Cb");
			else
				System.out.println("Cr");
			// image = new SZLIMageModel(header[0],header[1])
			//ac(runlengthValueList) => zigzag 8x8
			//image.writeBloc(zigzag)
			// le bon a storer
			// int[] storedDcY[] = dc
			// int[] storedDcCb[] = dc
			// int[] storedDcCr[] = dc
			// rewrite les dc avec dpcm inverse
			// image.writePixel(colorSpace,bloc,0,0)
			//roule algo inversse
		}	
	}
}
