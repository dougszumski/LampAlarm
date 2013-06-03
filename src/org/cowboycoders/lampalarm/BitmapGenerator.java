package org.cowboycoders.lampalarm;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class BitmapGenerator {
	
	//Debug
	private static final String TAG = "Bitmap Generator";
	private static final boolean D = true;
	
	//Image dimension
	private final int xPixels;
	private final int yPixels;
	
	Bitmap bitmap;
	Canvas canvas;
	Paint paint;
	
	public BitmapGenerator(int xPixels, int yPixels){
		//Set the image dimensions
		this.xPixels = xPixels;
		this.yPixels = yPixels;
		
		//Create bitmap
		bitmap = Bitmap.createBitmap(xPixels, yPixels, Bitmap.Config.ARGB_8888);
		
		//Create a canvas to draw on the bitmap
		canvas = new Canvas(bitmap);
	
		//Set the drawing colour
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
	    paint.setColor(Color.BLACK);
	}
	
	public Bitmap byteArrayToBitmap(List<Integer> frame)
	{
		int count = 0;
		int index = 0;
		int data = 0;
		int len = frame.size()-1;
		// Subtle bug here: we are going over the end of the array
		for (int u8Page = 0; u8Page < 8; ++u8Page) { 
			for (int u8Column=0; u8Column<128; ++u8Column) {
				if(count == 0) {
					data = frame.get(index);
					//System.out.println(data + "," + index);
					if (index < len) index++;
					if(data == frame.get(index)) {
						if (index < len) index++;
						//System.out.println("Match: " + data+ " repeating: " + frame.get(index));
						// If data is repeated write it count times
						count = frame.get(index);
						if (index < len) index++;
					}
					else {
						// Else only write it once
						count = 1;
					}
				}
			
				for (int i = 7; i>=0; i--)
				{
					if ((data & (1 << i)) != 0)
					{
						paint.setColor(Color.WHITE);
					} else 
					{
						paint.setColor(Color.BLACK);
					}
					canvas.drawPoint(u8Column, u8Page*8 + i, paint); 
				}
				
				// Decrease number of times to write the byte
			    count--;
			}
		}

	    return bitmap;
	}
	
	public void printArray(int[] input) 
	{
		for (int i = 0; i < input.length; ++i)
		{
			Log.e(TAG, String.valueOf(input[i]));
		}
		
	}
	
	public void main()
	{
		int[] cowboy = {
				0x0,0x0,0x3c,0xa0,0x0,0x20,0x40,0x90,0x20,0x40,0xa0,0x0,0xf0,0x0,0xf0,0x80,0x0,0x0,0x62,0x50,0x28,0x80,0x68,0x10,0xc0,0x10,0x20,0xc0,0x20,0x80,0x20,0x40,0x90,0x0,0x70,0xf5,0x4a,0xf0,0xc4,0xb9,0xc4,0x73,0xcc,0xf1,0xce,0xf1,0x6e,0xb1,0x7e,0xb9,0x30,0xc0,0x38,0xc8,0x30,0xcc,0x38,0xc4,0xbc,0x52,0xae,0xf2,0xe,0x3a,0x8,0x0,0x0,0x55,0x1,0x0,0xc3,0x20,0xd,0xd2,0x24,0x82,0x2d,0x42,0xdc,0x0,0xfb,0x4,0xf2,0xd,0xf0,0xd,0xf2,0x5,0xfa,0x4,0xfa,0x0,0xff,0x80,0x7d,0xc2,0x3d,0xe0,0x5f,0xd0,0x3f,0xf0,0xcf,0x38,0xf3,0x60,0xc1,0x80,0x0,0x0,0x56,0xd0,0x74,0x89,0xf6,0x8,0xf3,0x4c,0xb2,0x4c,0xf3,0x8c,0x73,0x8c,0x73,0xac,0xd3,0x2c,0x93,0xc,0x3,0x44,0xc3,0xe1,0xf6,0xe1,0xef,0xd0,0xbf,0x42,0xfd,0xaf,0x10,0x4f,0x5,0x3,0xc4,0xf3,0xe1,0xf7,0xe2,0xef,0x1e,0xf2,0x7c,0xc0,0x0,0x0,0x4c,0x31,0x2,0x39,0x27,0x1c,0xb,0x74,0xcf,0x3b,0xec,0x93,0xfe,0x29,0xd7,0x7c,0xc3,0xbe,0xc9,0x3e,0xe1,0x5f,0xb4,0x6b,0xd6,0x28,0xf4,0x8b,0x70,0xcf,0xb7,0xcf,0x2f,0xff,0xc7,0x3a,0xd7,0xfc,0x2b,0xd2,0xfc,0x21,0xf4,0x4b,0xf7,0xcf,0x3f,0xe7,0x7f,0xd3,0xfd,0x2f,0xfb,0x64,0x0,0x0,0x4c,0x20,0xd4,0xf8,0xfc,0xf8,0xad,0xf3,0x3f,0xec,0xd3,0xbe,0x6b,0xf6,0xcd,0x3e,0xf1,0xce,0xb9,0x6d,0xdb,0xb2,0xed,0x33,0xed,0xb6,0x6b,0xd4,0xaf,0x72,0xcd,0xb7,0xda,0x25,0xff,0xca,0x35,0xef,0x5a,0xf5,0xcf,0x3b,0xf4,0x4f,0xfd,0x53,0xfd,0x57,0xfe,0x55,0xff,0xff,0x2,0x0,0x0,0x4e,0x1,0x1,0x2,0x3,0x83,0x2,0x15,0x7f,0xd7,0x7c,0xdf,0xf3,0xbc,0xcf,0xfb,0xb6,0xef,0x3c,0xcb,0x27,0xc4,0x8f,0x5b,0xb4,0xef,0xbb,0xee,0xb5,0xff,0x4a,0xff,0xda,0xb7,0xfc,0x8f,0x53,0xfc,0x6f,0xdb,0xf6,0xbd,0x77,0xad,0x4f,0xd3,0xf4,0xad,0xd3,0xfd,0xbf,0xb,0x80,0x0,0x0,0x52,0x1,0x4,0x1,0x1,0x2,0x2,0x3,0x6,0x3,0x6,0x1,0x2,0x5,0x1,0x7,0x0,0x3,0x5,0x3,0x4,0x3,0x2,0x7,0x5,0x7,0x9,0x2,0x3,0x4,0x3,0x5,0x2,0x1,0x7,0x0,0x5,0x3,0x7,0x4,0x3,0x3,0x2,0x2,0x1,0x0,0x2,0x0,0x2};
		
		printArray(cowboy);
		
	}
	

}
