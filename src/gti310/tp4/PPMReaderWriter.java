package gti310.tp4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The PPMReaderWriter class enables reading and writting binary PPM files. The
 * values read are returned in a three dimension array: each dimension for one
 * of the colors in the RGB color space. The dimensions are all the same size.
 * The same must be true for writting pixel values to a file.
 *  
 * @author Fran?ois Caron
 */
public class PPMReaderWriter {
	/* 
	 * constants used to read & write
	 */
	private static final byte LINE_FEED = 10;
	private static final byte SPACE = 32;
	
	/*
	 * tag for PPM images
	 */
	private static final String MAGIC_ID = "P6";
	
	/*
	 * comment string to add to files written with this application
	 */
	private static final String COMMENTS = "# ?TS GTI310 codec squeeze light";
	
	/*
	 * maximum value in the file
	 * 1 is implicit: max value realy is 256
	 * use 255 since it is 0xff (11111111)
	 */
	private static final String MAX_GREY = "255";
	
	/**
	 * The readPPMFile method reads the magic id of the file and checks if it
	 * corresponds to the binary PPM magic id. If not, the file is closed and
	 * the method returns nothing.
	 * 
	 * The process then anayles the file's header and skips the comments. The
	 * height and width of the file are used to build the matrix that will hold
	 * the RGB values of the image. The matrix is a 3D array: each dimension
	 * holds one dimension of the RGB color space.
	 * 
	 * @param filename
	 * @return
	 */
	public static int[][][] readPPMFile(String filename) {
		try {
			DataInputStream in = new DataInputStream(
					new BufferedInputStream(new FileInputStream(filename)));
			byte b;
			
			/* read file format */
			String magicId = "";
			while((b = in.readByte()) != LINE_FEED && b  != SPACE) {
				magicId += (char)b;
			}
			
			/* check to make sure we are reading a PPM file */
			if(!magicId.equals(MAGIC_ID)) {
				in.close();  // close file handler
				return null; // nothing to return.
			}
			
			/* skip comments */
			while((b = in.readByte()) == 35)
				while((b = in.readByte()) != LINE_FEED);
			
			/*
			 * at this point, the last read character should be part of the
			 * image's height
			 */
			
			/* read height */
			String height = "" + (char)b;
			while((b = in.readByte()) != SPACE) {
				height += (char)b;
			}
			
			/* read width */
			String width = "";
			while((b = in.readByte()) != LINE_FEED) {
				width += (char)b;
			}
			
			/* skip the number of colors in the image*/
			while((b = in.readByte()) != LINE_FEED);
			
			/* read the image content */
			int totalBytes = Integer.parseInt(height) * Integer.parseInt(width) * 3;
			byte[] bytes = new byte[totalBytes];
			in.read(bytes);
			
			/* close the file handler */
			in.close();
			
			/* 
			 * creates a new array of RGB values with what was read in the file
			 */
			int h = Integer.parseInt(height);
			int w = Integer.parseInt(width);
			int[][][] image = new int[Main.COLOR_SPACE_SIZE][h][w];
			int offset = 0;
			for(int i = 0; i < h; i++) {
				for(int j = 0; j < w ; j++) {
					image[Main.R][i][j] = (int)(bytes[offset] & 0xff);
					image[Main.G][i][j] = (int)(bytes[offset + 1] & 0xff);
					image[Main.B][i][j] = (int)(bytes[offset + 2] & 0xff);
					offset += 3;
				}
			}
			
			/* return a 3D array of size height x width */
			return image;
			
		} catch (FileNotFoundException e) {
			/*
			 * The specified file was not found.
			 */
			System.err.println(e.getMessage());
		} catch (IOException e) {
			/*
			 * Someting went wrong while reading the file...
			 */
			System.err.println(e.getMessage());
		}
		
		/*
		 * An exception was raised: nothing to return...
		 */
		return null;
	}
	
	/**
	 * 
	 * @param filename
	 * @param image
	 */
	public static void writePPMFile(String filename, int[][][] image) {
		try {
			/* open a new file: overwrite it if it exists */
			DataOutputStream out = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(filename)));
			
			/* write magig id */
			out.writeBytes(MAGIC_ID);
			out.writeByte((int)(LINE_FEED & 0xff));
			
			/* write comments */
			out.writeBytes(COMMENTS);
			out.writeByte((int)(LINE_FEED & 0xff));
			
			/* write height & width */
			out.writeBytes("" + image[0].length);
			out.writeByte((int)(SPACE & 0xff));
			out.writeBytes("" + image[0][0].length);
			out.writeByte((int)(LINE_FEED & 0xff));
			
			/* write max grey level */
			out.writeBytes(MAX_GREY);
			out.writeByte((int)(LINE_FEED & 0xff));
			
			/* 
			 * store pixel values inside byte vector
			 */
			byte[] bytes = new byte[image.length * image[0].length * image[0][0].length];
			int offset = 0;
			for(int i = 0; i < image[0].length; i++) {
				for(int j = 0; j < image[0][0].length; j++) {
					bytes[offset]     = (byte)(image[Main.R][i][j]);
					bytes[offset + 1] = (byte)(image[Main.G][i][j]);
					bytes[offset + 2] = (byte)(image[Main.B][i][j]);
					offset += 3;
				}
			}
			
			/* write byte vector to file */
			out.write(bytes);
			
			/* close the file handler */
			out.close();
		} catch (IOException e) {
			/*
			 * something went wrong while writting the file...
			 */
			System.err.println(e.getMessage());
		}
	}
}
