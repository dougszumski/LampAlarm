package org.cowboycoders.lampalarm.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class CowboyMessageParser implements Serializable {

	private static final long serialVersionUID = -5706094036232329515L;

	// Debug
	private static final String TAG = "MessageParser";
	private static final boolean D = false;

	private static final CharSequence END_FRAME_MARKER = ":";

	// String buffer for incoming messages
	private final StringBuffer mFrameBuffer;
	private boolean imageWaiting;

	public CowboyMessageParser() {
		// Initialize the buffer for incoming messages
		mFrameBuffer = new StringBuffer("");
	}

	public List<Integer> append(byte[] message, int messageLength) {
		final String stringMessage = new String(message, 0, messageLength);
		List<Integer> frame = new ArrayList<Integer>();
		boolean frameReady = false;

		final StringBuffer ackPacket = new StringBuffer("");

		for (int i = 0; i < stringMessage.length(); ++i) {
			final char c = stringMessage.charAt(i);
			if (c == ':') {
				if (D) {
					Log.i(TAG, "Frame detected, previous frame was: "
							+ mFrameBuffer.length() + " bytes long");
				}
				if (D) {
					Log.i(TAG, ": is " + String.format("%x", (int) c));
				}
				frame = assembleFrame();
				frameReady = true;
				resetFrameBuffer();
				if (c == '#') {
					ackPacket.setLength(0);
				}

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
		// int[] buffer = new int[mFrameBuffer.length()];
		final List<Integer> buffer = new ArrayList<Integer>();
		final String tempByte;
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
