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
		iniImageModel();
		int[][][] writtenRGB = ycbcr.readYCbCr(imageModel);
		//assertArrayEquals(rgbImage, writtenRGB);
		
		for (int i = 0; i < writtenRGB.length; i++) {
			for (int j = 0; j < writtenRGB[i].length; j++) {
				for (int k = 0; k < writtenRGB[i][j].length; k++) {
					assertFalse("Difference between RGB components is bigger than 2",Math.abs(Math.abs(writtenRGB[i][j][k]) - Math.abs(rgbImage[i][j][k])) > 2);
				}
			}
		}
	}
	

	private void iniImageModel() {
		
		imageModel.addColorComponentFromRGB(0, 0, 0, 144);
		imageModel.addColorComponentFromRGB(1, 0, 0, 54);
		imageModel.addColorComponentFromRGB(2, 0, 0, 34);
		
		imageModel.addColorComponentFromRGB(0, 0, 1, 113);
		imageModel.addColorComponentFromRGB(1, 0, 1, 72);
		imageModel.addColorComponentFromRGB(2, 0, 1, 137);
		
		imageModel.addColorComponentFromRGB(0, 1, 0, 113);
		imageModel.addColorComponentFromRGB(1, 1, 0, 72);
		imageModel.addColorComponentFromRGB(2, 1, 0, 137);
		
		imageModel.addColorComponentFromRGB(0, 1, 1, 81);
		imageModel.addColorComponentFromRGB(1, 1, 1, 90);
		imageModel.addColorComponentFromRGB(2, 1, 1, 239);
		
	}

}
