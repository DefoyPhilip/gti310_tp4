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
		if(args.length == 3){
			System.out.println(args[2]);
			encode(args);
		}
		else if (args.length == 2){
			decode(args);
		}
		
	}
	/**
	 * Notation O(n^4)
	 * @param args
	 */
	
	private static void encode(String[] args){
		int fq = Integer.parseInt(args[0]);

		//Create a YCbCr images with the rgb ppm image
		YCbCrReaderWriter yCbCrCodec = new YCbCrReaderWriter();
		int[][][] RGBImage = PPMReaderWriter.readPPMFile(args[1]); // testing
		YCbCrImageModel yCbCrImage = yCbCrCodec.writeYCbCr(RGBImage);
		yCbCrCodec.readYCbCr(yCbCrImage);
		
		//Instanciacte our data model (check class to see the structure)
		SZLImageModel image = new SZLImageModel(yCbCrImage.get_height(),yCbCrImage.get_width(),yCbCrImage.get_image());
		//rewriting each 8x8 bloc after applying Zigzag, quantification and DCT algorithm
		for (int i = 0; i < image.getBlockQuantity(); i++) {
			image.writeBlock(Y, i,
					ZigzagReaderWriter.write(//O(n^2)
							QuantificationUtils.quantY(//O(n^2)
									DCTUtils.encode(image.getBlock(Y, i)) //O(n^4)
							,fq)
					)
			);
			image.writeBlock(U, i,
					ZigzagReaderWriter.write(//O(n^2)
							QuantificationUtils.quantCbCr(//O(n^2)
									DCTUtils.encode(image.getBlock(U, i))//O(n^4)
							, fq)
					)
			);
			image.writeBlock(V, i,
					ZigzagReaderWriter.write(//O(n^2)
							QuantificationUtils.quantCbCr(//O(n^2)
									DCTUtils.encode(image.getBlock(V, i))//O(n^4)
							, fq)
					)
			);
		}
		//Storing DC's of each color space
		Dpcm.dpcm(image.getColorSpace(Y), Y);
		Dpcm.dpcm(image.getColorSpace(U), U);
		Dpcm.dpcm(image.getColorSpace(V), V);
		
		//Writing binary's in the Entropy buffer
		for (int i = 0; i < image.getBlockQuantity(); i++) {
			for (int j = 0; j < 3; j++) {
				Entropy.writeDC(Dpcm.getDC(j,i));
				LinkedList<int[]>ac = ACUtils.prepareACWrite(image.getBlock(j, i));
				for (int[] is : ac) {
					Entropy.writeAC(is[0], is[1]);
				}
			}
		}
		//Write the file
		SZLReaderWriter.writeSZLFile(args[2], yCbCrImage.get_height(), yCbCrImage.get_width(), fq);
	}
	/**
	 * Notation O(n^4)
	 * @param args
	 */
	private static void decode(String[] args){
		int[] header = SZLReaderWriter.readSZLFile(args[0]);
		int blocQuanity = (header[1]/8)*(header[0]/8) * header[2];
		//Creating an empty model
		SZLImageModel image = new SZLImageModel(header[0],header[1]);
		int[][] storedDc = new int[3][blocQuanity/3];
		for (int i = 0; i < blocQuanity; i++) {
			int[] ac;
			boolean val;
			int blocIndex = (int)(Math.ceil(i/3));
			LinkedList<int[]> runlengthValueList = new LinkedList<int[]>();
			//Get DC and store it
			storedDc[i % 3][blocIndex] = Entropy.readDC();
			//Get AC and write them in the model
			//Read ac until the entropy class read a 0 : 0 couple
			do {
				ac = Entropy.readAC();
				int[] runlengthValue = new int[2];
				runlengthValue[0] = ac[0];
				runlengthValue[1] = ac[1];
				runlengthValueList.add(runlengthValue);
				val = ac[0] == ac[1] && ac[0] == 0;
			} while (!val);
			image.writeBlock(i % 3, blocIndex, ACUtils.readAC(runlengthValueList));
		}
		// Apply every inverse algorithm to rebuild the image
		for (int i = 0; i < 3; i++) {
			Dpcm.dpcmInverse(storedDc[i], i);
			for (int j = 0; j < blocQuanity/3; j++) {
				image.writePixel(i,j,0,0,Dpcm.getDC(i, j));
				if(i == 0){
					image.writeBlock(i, j, 
						DCTUtils.decode(
							QuantificationUtils.dequantY(
								ZigzagReaderWriter.read(
									image.getBlock(i, j)
								), header[3]
							)
						)
					);
				}
				else{
					image.writeBlock(i, j, 
						DCTUtils.decode(
							QuantificationUtils.dequantCbCr(
								ZigzagReaderWriter.read(
									image.getBlock(i, j)
								), header[3]
							)
						)
					);
				}	
			}
		}
		//Redo a normal 2 Dimensonal array to convert it un RGB
		int[][][] imageRecouper = image.recoupage();
		YCbCrReaderWriter yCbCrCodec = new YCbCrReaderWriter();
		YCbCrImageModel yCbCrImage = new YCbCrImageModel(header[0],header[1]);
		yCbCrImage.set_image(imageRecouper);
		yCbCrCodec.readYCbCr(yCbCrImage);
		//Rewrite the uncompressed image
		PPMReaderWriter.writePPMFile(args[1], yCbCrCodec.readYCbCr(yCbCrImage));
	}
}
