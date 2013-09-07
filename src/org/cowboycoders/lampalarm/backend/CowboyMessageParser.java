package org.cowboycoders.lampalarm.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class CowboyMessageParser implements Serializable {

	private static final long serialVersionUID = -5706094036232329515L;

	// Debug
	private static final String TAG = "MessageParser";
	private static final boolean D = true;

	private static final char END_FRAME_MARKER = ':';

	// String buffer for incoming messages
	private final StringBuffer mFrameBuffer;
	private boolean imageWaiting;

	public CowboyMessageParser() {
		// Initialize the buffer for incoming messages
		mFrameBuffer = new StringBuffer("");
	}

	/**
	 * Append 
	 * 
	 * @param message - Incoming message
	 * @param messageLength
	 * @return
	 */
	public List<Integer> append(byte[] message, int messageLength) {
		
		//Convert message to a String
		final String stringMessage = new String(message, 0, messageLength);
		List<Integer> frame = new ArrayList<Integer>();
		boolean frameReady = false;

		// Extract the message
		for (int i = 0; i < stringMessage.length(); ++i) {
			final char c = stringMessage.charAt(i);
			if (c == END_FRAME_MARKER) {
				if (D) {
					Log.i(TAG, "Frame detected, previous frame was: "
							+ mFrameBuffer.length() + " bytes long");
				}
				// All messages should be multiples of 2 since they are in ASCII hex
				// This serves as a crude check for corruption
				// TODO: Remove when proper protocol is used.
				if (mFrameBuffer.length() % 2 == 0) {
					frame = assembleFrame();
					frameReady = true;
				}
				resetFrameBuffer();
			} else {
				mFrameBuffer.append(c);
			}
		}

		if (frameReady) {
			frameReady = false;
			return frame;
		} else {
			return null;
		}
	}

	public List<Integer> assembleFrame() {
		
		final List<Integer> buffer = new ArrayList<Integer>();
		
		for (int i = 0; i < mFrameBuffer.length(); i += 2) {
			// Convert string to int 2 chars at a time
			try {
				final Integer tmp = Integer.parseInt(
						mFrameBuffer.substring(i, i + 2), 16);
				buffer.add(tmp);
				if (D) {
					Log.i(TAG, "Chars: " + mFrameBuffer.substring(i, i + 2)
							+ " result: " + tmp);
				}
			} catch (final NumberFormatException e) {
				if (D) {
					Log.e(TAG, "Number format exception");
				}
				buffer.add(0x00);
			}

		}
		return buffer;
	}

	public void resetFrameBuffer() {
		mFrameBuffer.setLength(0);
	}

	public byte[] echoBack(byte[] data) {
		for (final byte b : data) {
			System.out.println(b);
		}
		return data;

	}

	public StringBuffer getFrame() {

		if (imageWaiting) {
			return mFrameBuffer;
		} else {
			return null;
		}
	}

}
