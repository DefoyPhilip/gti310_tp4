package gti310.tp4;

/**
 * The Entropy class is used to write the DC and AC coefficients after DPCM or
 * RLE compression. It does not write the information to a file, but to a
 * buffer, that can be written to a file using the SZLReaderWriter class.
 * 
 * @author Fran?ois Caron
 * 
 * @history
 * 2006-11-29 Fixed values in HUFFMAN_AC table
 */
public class Entropy {
	/*
	 * constants to access the HUFFMAN tables
	 */
	private static final int SIZE = 0;
	private static final int AMPLITUDE = 1;
	
	/*
	 * The HUFFMAN_DC table is used to encode the DC coefficient after they
	 * went throught the DPCM. The first column represents the number of bits
	 * to use to write the size value and the second column represents
	 * the decimal value of Huffman size binary value. 
	 * 
	 * CATEGORY  BASECODE       LENGTH BINARY VALUE
	 *        0  010                 3            2
	 *        1  011                 3            3
	 *        2  100                 3            4
	 *        3  00                  2            0
	 *        4  101                 3            5
	 *        5  110                 3            6
	 *        6  1110                4           14
	 *        7  11110               5           30
	 *        8  111110              6           62
	 *        9  1111110             7          126
	 *        A  11111110            8          254
	 *        B  111111110           9          510
	 *        C  1111111110         10         1022
	 *        D  11111111110        11         2046
	 *        
	 */
	private static final int[][] HUFFMAN_DC = {
		{ 3, 3, 3, 2, 3, 3,  4,  5,  6,  7,    8,   9,   10,   11}, /* size */
		{ 2, 3, 4, 0, 5, 6, 14, 30, 62, 126, 254, 510, 1022, 2046}  /* amplitude */
	};
	
	/*
	 * special values for the HUFFMAN_AC encoding
	 */
	private static final int ZRL = 2041; /* 11111111001 */
	private static final int ZRL_SIZE = 11;
	private static final int EOB = 10;   /* 1010 */
	private static final int EOB_SIZE = 4;
	
	/*
	 * The HUFFMAN_AC table holds the decimal values to the binary string
	 * used to encode the (RUNLENGTH,SIZE) pair with Huffman coding. The
	 * special values were omitted from this table.
	 * 
	 * RUN /      BASECODE
	 * SIZE
	 * 0/1        00
	 * 0/2        01
	 * 0/3        100
	 * 0/4        1011
	 * 0/5        11010
	 * 0/6        111000
	 * 0/7        1111000
	 * 0/8        1111110110
	 * 0/9        1111111110000010
	 * 0/A        1111111110000011
	 * ...        ...
	 * F/8        1111111111111100        
	 * F/9        1111111111111101
	 * F/A        1111111111111110
	 * 
	 */
	private static final int[][][] HUFFMAN_AC = {
		{ /* size */
			{  2,  2,  3,  4,  5,  7,  8, 10, 16, 16},
			{  4,  5,  7,  9, 11, 16, 16, 16, 16, 16},
			{  5,  8, 10, 12, 16, 16, 16, 16, 16, 16},
			{  6,  9, 12, 16, 16, 16, 16, 16, 16, 16},
			{  6, 10, 16, 16, 16, 16, 16, 16, 16, 16},
			{  7, 11, 16, 16, 16, 16, 16, 16, 16, 16},
			{  7, 12, 16, 16, 16, 16, 16, 16, 16, 16},
			{  8, 12, 16, 16, 16, 16, 16, 16, 16, 16},
			{  9, 15, 16, 16, 16, 16, 16, 16, 16, 16},
			{  9, 16, 16, 16, 16, 16, 16, 16, 16, 16},
			{  9, 16, 16, 16, 16, 16, 16, 16, 16, 16},
			{ 10, 16, 16, 16, 16, 16, 16, 16, 16, 16},
			{ 10, 16, 16, 16, 16, 16, 16, 16, 16, 16},
			{ 11, 16, 16, 16, 16, 16, 16, 16, 16, 16},
			{ 16, 16, 16, 16, 16, 16, 16, 16, 16, 16},
			{ 16, 16, 16, 16, 16, 16, 16, 16, 16, 16}
		},
		{ /* amplitude */
			{    0,     1,     4,    11,    26,   120,   248,  1014, 65410, 65411},
			{   12,    27,   121,   502,  2038, 65412, 65413, 65414, 65415, 65416},
			{   28,   249,  1015,  4084, 65417, 65418, 65419, 65420, 65421, 65422},
			{   58,   503,  4085, 65423, 65424, 65425, 65426, 65427, 65428, 65429},
			{   59,  1016, 65430, 65431, 65432, 65433, 65434, 65435, 65436, 65437},
			{  122,  2039, 65438, 65439, 65440, 65441, 65442, 65443, 65444, 65445},
			{  123,  4086, 65446, 65447, 65448, 65449, 65450, 65451, 65452, 65453},
			{  250,  4087, 65454, 65455, 65456, 65457, 65458, 65459, 65460, 65461},
			{  504, 32704, 65462, 65463, 65464, 65465, 65466, 65467, 65468, 65469},
			{  505, 65470, 65471, 65472, 65473, 65474, 65475, 65476, 65477, 65478},
			{  506, 65479, 65480, 65481, 65482, 65483, 65484, 65485, 65486, 65487},
			{ 1017, 65488, 65489, 65490, 65491, 65492, 65493, 65494, 65495, 65496},
			{ 1018, 65497, 65498, 65499, 65500, 65501, 65502, 65503, 65504, 65505},
			{ 2040, 65506, 65507, 65508, 65509, 65510, 65511, 65512, 65513, 65514},
			{65515, 65516, 65517, 65518, 65519, 65520, 65521, 65522, 65523, 65524},
			{65525, 65526, 65527, 65528, 65529, 65530, 65531, 65532, 65533, 65534}
		}
	};
	
	/*
	 * the size to use when creating and expanding the buffer
	 */
	private static final int BUFFER_SIZE = 1024;
	
	/*
	 * the buffer where the bits will be written
	 */
	private static byte[] writingBuffer;
	
	/*
	 * the buffer where the bits will be read
	 */
	private static byte[] readingBuffer;
	
	/*
	 * the number of bits left in the current byte of the reading or writting
	 * buffer
	 */
	private static int bitsLeftInByte;
	
	/*
	 * the index in the reading or writting buffer
	 */
	private static int currentByteInBuffer;
	
	/*
	 * the number of bits in one byte
	 */
	private static final int BITS_IN_BYTE = 8;
	
	/**
	 * Prepare new buffer to write output, and reset current position in buffer
	 * and amount of bits left to read in current byte.
	 */
	private static void flushBuffers() {
		/* create new storage space */
		writingBuffer = new byte[BUFFER_SIZE];
		
		/* nothing is written yet */
		bitsLeftInByte = BITS_IN_BYTE;
		
		/* start at first byte */
		currentByteInBuffer = 0;
	}
	
	/**
	 * Extend the buffer by adding more space to write bits.
	 */
	private static void expandBuffer() {
		/* add one buffer size to the current buffer size */
		byte[] temp = new byte[writingBuffer.length + BUFFER_SIZE];
		
		/* copy values */
		System.arraycopy(writingBuffer,0,temp,0,writingBuffer.length);
		
		/* point to new bigger buffer */
		writingBuffer = temp;
	}
	
	/**
	 * Decrement the bitsLeftInByte counter by one. If the counter reaches 0,
	 * the counter is reset to BITS_IN_BYTE, the currentByteInBuffer index is
	 * incremented by one. If the currentByteInBuffer index reaches the end of
	 * the buffer, the function returns FALSE, telling the calling function
	 * that no more bits can be read from the reading buffer.
	 * 
	 * @return
	 */
	private static boolean decrementBitsToRead() {
		/* on less bit to read in current byte */
		bitsLeftInByte--;
		
		/* if byte is completly read, go to next one */
		if(bitsLeftInByte == 0) {
			currentByteInBuffer++;
			/* if the buffer's end is reached, exit prematurely */
			if(currentByteInBuffer == readingBuffer.length) {
				/* no more data can be read from the buffer */
				return false;
			}
			
			/* reset the number of bits that can be read in current byte */
			bitsLeftInByte = BITS_IN_BYTE;
		}
		
		/* more data can be read from the buffer */
		return true;
	}
	
	/**
	 * Decrements the bitsLeftInByte by one. If the counter reaches 0, the
	 * counter is reset to BITS_IN_BYTE and the currentByteInBuffer index is
	 * incremented by one. If the index reaches the end of the writing buffer,
	 * the buffer is expanded in order to give more space to write bits.
	 *
	 */
	private static void decrementBitsToWrite() {
		/* one less space where to write in current byte */
		bitsLeftInByte--;
		
		/* check to see if space is left in current byte */
		if(bitsLeftInByte <= 0) {
			/* change byte index */
			currentByteInBuffer++;
			
			/* check to see if buffer limit was reached */
			if(currentByteInBuffer == writingBuffer.length) {
				/* add more space in buffer */
				expandBuffer();
			}
			
			/* reset space counter in current byte */
			bitsLeftInByte = BITS_IN_BYTE;
		}
	}
	
	/**
	 * Give the pointer to the buffer to be written in a file
	 * 
	 * @return
	 */
	public static byte[] getBitstream() {
		return writingBuffer;
	}
	
	/**
	 * Give the number of bytes used in the buffer.
	 * 
	 * @return
	 */
	public static int getNumberOfBytesUsed() {
		return currentByteInBuffer;
	}
	
	/**
	 * Load the bitstream from a read file. The writing buffer will be flushed,
	 * the bitsLeftInByte counter will be reset to BITS_IN_BYTE and the
	 * currentByteInBuffer will be reset to 0 (first position in the reading
	 * buffer).
	 * 
	 * @param buffer
	 */
	public static void loadBitstream(byte[] buffer) {
		readingBuffer = buffer;
		
		/* reset global positions */
		flushBuffers();
	}
	
	/**
	 * Write an amount of bits to the writing buffer. The bits parameter is
	 * what will be written, and the length parameter specifies how many bits
	 * must be used to write the value.
	 * 
	 * While writing, if the buffer is filled, the buffer will be expanded to
	 * fit more bits.
	 * 
	 * @param bits
	 * @param length
	 */
	private static void write(int bits, int length) {
		/* make sure writting is possible */
		if(writingBuffer == null) {
			flushBuffers();
		}
		
		/* write bits in buffer */
		for(int i = length; i > 0; i--) {
			int bit = ((bits >> (i - 1)) & 0x01) << (bitsLeftInByte - 1);
			writingBuffer[currentByteInBuffer] |= bit;
			
			/* on less space in current byte to write bits */
			decrementBitsToWrite();
		}
	}
	
	/**
	 * Write the DPCM value as a SIZE/AMPLITUDE pair using the Huffman size
	 * table. The resulting bitstream will then be saved in a byte buffer,
	 * awaiting to be written in a file.
	 * 
	 * @param value
	 */
	public static void writeDC(int value) {		
		/* get the pair SIZE/AMPLITUDE from the value to write */
		int[] pair = getSizeAmplitudePair(value);
		
		/* store value in binary buffer */
		int binary = 0;
		binary = pair[1];
		binary |= HUFFMAN_DC[AMPLITUDE][pair[0]] << pair[0];
		
		/* write value to buffer */		
		write(binary, pair[0] + HUFFMAN_DC[SIZE][pair[0]]);
	}
	
	/**
	 * Read the buffer from the present point and build up the next Huffman
	 * code for the size of the amplitude. When the code is recognized, read
	 * the amount of bits according to the position of the Huffman size table
	 * where the code was recognized. The position and the built values can
	 * then be used to get the original value used to form the SIZE/AMPLITUDE
	 * pair just read.
	 * 
	 * If the end of the buffer is reached, something went wrong: prematurely
	 * exit the loop and return the maximum value and integer can hold.
	 * 
	 * @return
	 */
	public static int readDC() {		
		/* make sure we can read something */
		if(readingBuffer == null) {
			return 0xffffffff;
		}
		
		/* equivalent of the SIZE/AMPLITUDE pair */
		int value = 0;
		int bitsRead = 0;
		
		/* read until value is found of the end of the buffer is reached */
		while(currentByteInBuffer < readingBuffer.length) {
			/* read one bit */
			value |= (readingBuffer[currentByteInBuffer] >> (bitsLeftInByte - 1)) & 0x01;
			bitsRead++;
			
			/* one less bit to read in current byte */
			if(!decrementBitsToRead())
				return 0xffffffff;
			
			/* look for known value in Huffman size table */
			for(int i = 0; i < HUFFMAN_DC[AMPLITUDE].length; i++) {
				if(value == HUFFMAN_DC[AMPLITUDE][i] && 
						bitsRead == HUFFMAN_DC[SIZE][i]) {
					/* read the next number of bytes defined by i */
					
					int size = 0;
					for(int j = 0; j < i; j++) {
						/* 
						 * shift one space left
						 * do before reading, not to multiply value by two went
						 * last bit is read
						 */
						size <<= 1;
						
						/* read one bit */
						size |= (readingBuffer[currentByteInBuffer] >> (bitsLeftInByte - 1)) & 0x01;
						
						/* on less bit to read in current byte */
						if(!decrementBitsToRead())
							return 0xffffffff; /* reached the end of the buffer */
					}
					
					/* return the value associated to the SIZE/AMPLITUDE pair */
					return getValue(i,size);
				}
			}
			
			/* shift one space to the left */
			value <<= 1;
		}
		
		/* read the entire buffer and found nothing */
		return 0xffffffff;
	}
	
	/**
	 * The RUNLENGTH/VALUE pair is decomposed into a RUNLENGTH/SIZE/VALUE
	 * triplet. The Value part is encoded in binary format directly. The 
	 * RUNLENGTH/SIZE couple is encoded using the Huffman RUNLENGTH/SIZE table.
	 * The triplet is then written to the byte buffer using the number of bits
	 * needed to represent the value (to keep the "invisible" leeding zeros
	 * using the Integer.toBinaryString() method).
	 * 
	 * @param runlength
	 * @param value
	 */
	public static void writeAC(int runlength, int value) {
		/* check for EOB marker */
		if(/*runlength == 0 && */value == 0) {
			write(EOB, EOB_SIZE);
		} else { /* normal routine */
			/* get SIZE/AMPLITUDE pair from value */
			int pair[] = getSizeAmplitudePair(value);
			while(runlength >= 0) {
				if(runlength <= 15) {
					/* create bit value */
					int binary = pair[1];
					binary |= HUFFMAN_AC[AMPLITUDE][runlength][pair[0]-1] << pair[0];
					
					/* write the value to the buffer */
					write(binary, pair[0] + HUFFMAN_AC[SIZE][runlength][pair[0]-1]);
				} else {
					/* more to be written */
					write(ZRL, ZRL_SIZE);
				}				
				
				runlength -= 16;
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static int[] readAC() {
		/* make sure we can read something */
		if(readingBuffer == null) {
			return null;
		}
		
		/* value assembled with read bits */
		int value = (readingBuffer[currentByteInBuffer] >> (bitsLeftInByte - 1)) & 0x01;
		int bitsRead = 1; /* number of bits read to identify Huffman code */
		int zrlCount = 0; /* number of times ZRL code was encountered */
		
		/* one less bit to read in current byte */
		decrementBitsToRead();
		
		/* read until values are found or end of buffer is reached */
		while(currentByteInBuffer < readingBuffer.length) {
			/* shift bits one space to the left */
			value <<= 1;
			
			/* read one bit */
			value |= (readingBuffer[currentByteInBuffer] >> (bitsLeftInByte - 1)) & 0x01;
			bitsRead++;
			
			/* one less bit to read in current byte */
			decrementBitsToRead();
			
			/* check for ZRL marker */
			if(value == ZRL && bitsRead == ZRL_SIZE) {
				zrlCount++;
				value = (readingBuffer[currentByteInBuffer] >> (bitsLeftInByte - 1)) & 0x01;
				bitsRead = 1;
				decrementBitsToRead();
			} else if(value == EOB && bitsRead == EOB_SIZE) {	/* check for EOB marker */
				int[] pair = {0,0};
				return pair;
			} else {				
				/* check to see if value is known */
				for(int i = 0; i < HUFFMAN_AC[AMPLITUDE].length; i++) {
					for(int j = 0; j < HUFFMAN_AC[AMPLITUDE][0].length; j++) {
						if(value == HUFFMAN_AC[AMPLITUDE][i][j]) {
							/* found Huffman code */
							int runlength = i;
							int size = j + 1;
							
							/* read amplitude */
							int amplitude = 0;
							for(int k = 0; k < size; k++) {
								amplitude <<= 1;
								amplitude |= (readingBuffer[currentByteInBuffer] >> (bitsLeftInByte - 1)) & 0x01;
								/* one less bit to read in current byte */
								decrementBitsToRead();
							}
							
							/* store runlength/value pair */
							int[] pair = {runlength + zrlCount * 16, getValue(size, amplitude)};
							return pair;
						} else if(value < HUFFMAN_AC[AMPLITUDE][i][j]) {
							/* skip rest of list */
							j = HUFFMAN_AC[AMPLITUDE][0].length;
						}
					}
				}
			}
		}
		
		/* the end of the reading buffer was reached, return nothing */
		return null;
	}
	
	/**
	 * Builds the SIZE,AMPLITUDE pair based on the given value. The largest
	 * value that can be passed is 314 or -314 (max grey level value). From a
	 * DC point of view, it would be the difference between the maximum V value
	 * and the minimum V value. From an AC point of view, that value is not
	 * recheable: the maximum Y value is 255.
	 * 
	 * According to the SIZE/AMPLITUDE chart, the biggest size for these values
	 * is 10 (1100 binary wise). The min and max values for the interval are
	 * [-1023;1023] (1111111111 binary wise).
	 * 
	 * The return value will take the format xxxxxxxxxxxxxxxxxx SSSS AAAAAAAAAA
	 * where S are bits reserved for the size and A are bits reserved for the
	 * amplitude.
	 * 
	 * SIZE AMPLITUDE
	 *    1             -1,1
	 *    2    -3,      -2,2,         3
	 *    3    -7,...,  -4,4  ,...,   7
	 *    4   -15,...,  -8,8  ,...,  15
	 *    5   -31,..., -16,16 ,...,  31 
	 *    6   -63,..., -32,32 ,...,  63
	 *    7  -127,..., -64,64 ,..., 127
	 *    8  -255,...,-128,128,..., 255
	 *    9  -511,...,-256,256,..., 511
	 *   10 -1023,...,-512,512,...,1023
	 *  
	 * @param value
	 * @return
	 */
	private static int[] getSizeAmplitudePair(int value) {
		int pair[] = new int[2];
		
		/* return zeros (default initialization value) */
		if(value == 0) {
			return pair;
		}
		
		/* get the number of bytes to encode the value */
		pair[0] = Integer.toBinaryString(Math.abs(value)).length();
		
		/* get the decimal value amplitude to use */
		if(value < 0) {
			pair[1] = 2*(int)Math.pow(2,pair[0]-1)-1-Math.abs(value);
		} else {
			pair[1] = value;
		}
		
		/* return values */
		return pair;
	}
	
	/**
	 * Retreive the value associated with the given SIZE/AMPLITUDE pair. To
	 * detect if the amplitude represents a positive of negative value, the
	 * minimum value that uses the number of bits (size parameter) is
	 * evaluated. If amplitude is smaller than that value, the amplitude will
	 * have to go through one's complement in order to get the negative value
	 * associated with the amplitude using the given number of bits (size).
	 * 
	 * @param size
	 * @param amplitude
	 * @return
	 */
	private static int getValue(int size, int amplitude) {
		//System.out.println("size/amplitude = " + size + "/" + amplitude);
		/* get the smallest value that needs the 'size' number of bits */
		int min = (int)Math.pow(2, size - 1);
		
		/* if amplitude is smaller than min, the value is negavite */
		if(amplitude < min) {
			/* use one's complement to retreive the correct value */
			return -(2*min-1-Math.abs(amplitude));
		} else {
			/* the amplitude is the value */
			return amplitude;
		}
	}
}
