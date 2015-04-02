package yCbCr;

import static org.junit.Assert.*;
import gti310.tp4.PPMReaderWriter;

import org.junit.Test;

import yCbCr.YCbCrImageModel;
import yCbCr.YCbCrReaderWriter;

public class YCbCrReaderWriterTest {
	
	private String ppmFilePath = "tests/yCbCr/2x2-test-ppm.ppm";
	
	private YCbCrImageModel imageModel = new YCbCrImageModel(2, 2);
	
	private int[][][] rgbImage = PPMReaderWriter.readPPMFile(ppmFilePath);
	
	YCbCrReaderWriter ycbcr = new YCbCrReaderWriter();
	
	
	

	@Test
	public void testWriteYCbCr() {
		System.out.println("Testing writeYCbCr()");
		iniImageModel();
		YCbCrImageModel writtenYCbCr = ycbcr.writeYCbCr(rgbImage);
		assertArrayEquals(imageModel.get_image(), writtenYCbCr.get_image());
	}
	
	@Test
	public void testReadYCbCr(){
		System.out.println("Testing readYCbCr()");
		YCbCrImageModel writtenYCbCr = ycbcr.writeYCbCr(rgbImage);
		int[][][] writtenRGB = ycbcr.readYCbCr(writtenYCbCr);
		assertArrayEquals(rgbImage, writtenRGB);
	}
	

	private void iniImageModel() {
		
		imageModel.addColorComponentFromRGB(0, 0, 0, 145);
		imageModel.addColorComponentFromRGB(1, 0, 0, 55);
		imageModel.addColorComponentFromRGB(2, 0, 0, 35);
		
		imageModel.addColorComponentFromRGB(0, 0, 1, 114);
		imageModel.addColorComponentFromRGB(1, 0, 1, 72);
		imageModel.addColorComponentFromRGB(2, 0, 1, 138);
		
		imageModel.addColorComponentFromRGB(0, 1, 0, 114);
		imageModel.addColorComponentFromRGB(1, 1, 0, 72);
		imageModel.addColorComponentFromRGB(2, 1, 0, 138);
		
		imageModel.addColorComponentFromRGB(0, 1, 1, 82);
		imageModel.addColorComponentFromRGB(1, 1, 1, 91);
		imageModel.addColorComponentFromRGB(2, 1, 1, 240);
		
	}

}
