package org.cowboycoders.lampalarm;

public class CowboyMessageParser {
	
	// String buffer for incoming messages
    private StringBuffer mInStringBuffer;
    private boolean imageWaiting;
	
	public CowboyMessageParser()
	{
		// Initialize the buffer for incoming messages
		mInStringBuffer = new StringBuffer("");
	}
	
	public void append(String message) 
	{
		mInStringBuffer.append(message);
	}
	
	public void reset()
	{
		mInStringBuffer.setLength(0);
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
			return mInStringBuffer;
		}
		else {
			return null;
		}
	}

}
