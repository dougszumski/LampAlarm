package org.cowboycoders.lampalarm;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class CowboyMessageParser {
	
	//Debug
	private static final String TAG = "MessageParser";
	private static final boolean D = false;

	// String buffer for incoming messages
    private StringBuffer mFrameBuffer;
    private boolean imageWaiting;
	
	public CowboyMessageParser()
	{
		// Initialize the buffer for incoming messages
		mFrameBuffer = new StringBuffer("");
	}
	
	public List<Integer> append(byte[] message, int messageLength) 
	{
		String stringMessage = new String(message, 0, messageLength);
		List<Integer> frame = new ArrayList<Integer>();
		boolean frameReady = false;
		
		for (int i = 0; i < stringMessage.length(); ++i){
		    char c = stringMessage.charAt(i);        
		    if (c == ':') {
		    	if(D) Log.i(TAG, "Frame detected, previous frame was: " +mFrameBuffer.length() + " bytes long");
		    	if(D) Log.i(TAG, ": is " + String.format("%x", (int)c));
		    	frame = assembleFrame();
		    	frameReady = true;
		    	resetFrameBuffer();
		    } else {
		    	mFrameBuffer.append(c);
		    }
		}
		
		if(frameReady) {
			frameReady = false;
			return frame;
		} else {
			return null;
		}
	}
	
	public List<Integer> assembleFrame(){
		//int[] buffer = new int[mFrameBuffer.length()];
		List<Integer> buffer = new ArrayList<Integer>();
		String tempByte;
 		for (int i = 0; i < mFrameBuffer.length(); i+=2){
 			//Convert string to int 2 chars at a time
 			Integer tmp = Integer.parseInt(mFrameBuffer.substring(i, i+2), 16);
			buffer.add(tmp);
			if(D) Log.i(TAG, "Chars: " + mFrameBuffer.substring(i, i+2) + " result: " + tmp);
		}
		return buffer;
	}
	
	public void resetFrameBuffer()
	{
		mFrameBuffer.setLength(0);
	}
	
	public byte[] echoBack(byte[] data)
	{
		for (byte b : data)
		{
			System.out.println(b);
		}
		return data;
		
	}
	
	public StringBuffer getFrame() {
		
		
		if (imageWaiting) {
			return mFrameBuffer;
		}
		else {
			return null;
		}
	}

}
