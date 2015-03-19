package gti310.tp4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The SZLReaderWriter class writes and reads SZL files. The SZL format is a
 * private format of file: it is only used in the context of the GTI310 course
 * at the ?TS.
 * 
 * @author Fran?ois Caron
 */
public class SZLReaderWriter {	
	/*
	 * magic id for our type of file
	 */
	private static final byte MAGIC_ID = (byte)0xEA;
	
	/**
	 * Write an SZL file. The height, width and qualityFactor parameters will
	 * be stored in the file's header. The filenam parameter is the name that
	 * will be given to the file: it can be a complete path of just the
	 * filename. The actual data is retreived from the writing buffer in the
	 * Entropy class.
	 * 
	 * @param filename
	 * @param height
	 * @param width
	 * @param qualityFactor
	 * @param buffer
	 * @param size
	 */
	public static void writeSZLFile(String filename, int height, int width, int qualityFactor) {
		try {
			/* open a new file, overwrite if it exists */
			DataOutputStream out = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(filename)));
			
			/* write magig id */
			out.writeByte(MAGIC_ID);
			
			/* write height & width */
			out.writeByte((height >> 8) & 0xff);
			out.writeByte(height & 0xff); /* 16 bits to write height */
			out.writeByte((width >> 8) & 0xff);
			out.writeByte(width & 0xff); /* 16 bits to write width */
			
			/* write number of color components */
			out.writeByte((byte)(Main.COLOR_SPACE_SIZE & 0xff));
			
			/* write quality factor */
			out.writeByte(qualityFactor & 0xff);
			
			/* write values */
			out.write(Entropy.getBitstream(), 0, Entropy.getNumberOfBytesUsed() + 1);
			
			/* close file handler */
			out.close();
		} catch(IOException e) {
			/*
			 * something went wrong while writting the file
			 */
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * The readSQZFile method reads a Squeeze-light type file. It opens the
	 * file and checks the magic code to make sure it is the right type of
	 * file. If it is, the header of the file is read and stored an returned.
	 * 
	 * The buffer will 4 available spaces: the first space is for the image's
	 * height, the second, for the image's width, the third, for the amount of
	 * dimensions in the color space (should be 3), and the last space stores
	 * the quality factor.
	 * 
	 * The values to reassemble the image are all read and stored in the
	 * Entropy class for futur reading.
	 *  
	 * @param filename
	 * @return
	 */
	public static int[] readSZLFile(String filename) {
		try {
			DataInputStream in = new DataInputStream(
					new BufferedInputStream(new FileInputStream(filename)));
			
			/* read file format */
			byte format = in.readByte();
			/* check to make sure we are reading a SZL file */
			if(format != MAGIC_ID) {
				in.close();  // close file handler
				return null; // nothing to return.
			}
			
			/* buffer to store the file's header information */
			int[] header = new int[4];
			
			/* read image height */
			header[0] = (in.readByte() << 8) + (in.readByte() & 0xff);
			
			/* read image width */
			header[1] = (in.readByte() << 8) + (in.readByte() & 0xff);
			
			/* read number of color components */
			header[2] = in.readByte() & 0xff;
			
			/* read quality factor */
			header[3] = in.readByte() & 0xff;
			
			/* load values */
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			Entropy.loadBitstream(buffer);
			
			/* close file handler */
			in.close();
			
			/* return the file's header */
			return header;
		} catch (Exception e) {
			/*
			 * something went wrong while reading the file
			 */
			System.err.println(e.getMessage());
		}
		
		return null;
	}
}
