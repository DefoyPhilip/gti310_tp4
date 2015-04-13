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

	private int[][] _block1, _block2, _block3, _block4;
	
	public int[][][] stitchedImage = new int[3][16][16];
	
	

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
	
	@Test
	public void stitchBlockTest(){
		initStitchedImage();
		initBlocks();
		imageModel = new YCbCrImageModel(16, 16);
		imageModel.stitchBlock(0, _block1);
		imageModel.stitchBlock(0, _block2);
		imageModel.stitchBlock(0, _block3);
		imageModel.stitchBlock(0, _block4);
		
		imageModel.stitchBlock(1, _block1);
		imageModel.stitchBlock(1, _block2);
		imageModel.stitchBlock(1, _block3);
		imageModel.stitchBlock(1, _block4);
		
		imageModel.stitchBlock(2, _block1);
		imageModel.stitchBlock(2, _block2);
		imageModel.stitchBlock(2, _block3);
		imageModel.stitchBlock(2, _block4);
		
		//for (int i = 0; i < imageModel.get_image().length; i++) {
			for (int j = 0; j < imageModel.get_image()[0].length; j++) {
				for (int k = 0; k < imageModel.get_image()[0][j].length; k++) {
					//System.out.println(imageModel.get_image()[0][j][k]);
				}
			}
		//}
		
		
		assertArrayEquals(stitchedImage, imageModel.get_image());
	}
	
	private void initStitchedImage() {
		int c = 0;
		int v = 0;
		for (int i = 0; i < stitchedImage.length; i++) {
			for (int j = 0; j < stitchedImage[i].length; j++) {
				for (int k = 0; k < stitchedImage[i][j].length; k++) {

					if (j<8 && k<8)
						stitchedImage[i][j][k] = 1;
					if (j<8 && k>=8)
						stitchedImage[i][j][k] = 2;
					if (j>=8 && k<8)
						stitchedImage[i][j][k] = 3;
					if (j>=8 && k>=8)
						stitchedImage[i][j][k] = 4;
				}
			}
		}
	}
	
	private void initBlocks() {
		_block1 = new int[8][8];
		_block2 = new int[8][8];
		_block3 = new int[8][8];
		_block4 = new int[8][8];
		
		for (int i = 0; i < _block1.length; i++) {
			for (int j = 0; j < _block1.length; j++) {
				_block1[i][j] = 1;
				_block2[i][j] = 2;
				_block3[i][j] = 3;
				_block4[i][j] = 4;
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
