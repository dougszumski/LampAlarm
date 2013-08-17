package org.cowboycoders.lampalarm.touchableCube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

public class RotatableObjectRenderer implements GLSurfaceView.Renderer {

	/**
	 * These values are used to rotate the image by a certain value
	 */
	private float xRot;
	private float yRot;
	private final Drawable object;

	public RotatableObjectRenderer(Drawable object) {
		this.object = object;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);

		/*
		 * Set our projection matrix. This doesn't have to be done each time we
		 * draw, but usually a new projection needs to be set when the viewport
		 * is resized.
		 */

		final float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);

		// Read the colour at specified target
		// TODO
		// gl.glReadPixels(x, y, width, height, format, type, pixels)
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		/*
		 * By default, OpenGL enables features that improve quality but reduce
		 * performance. One might want to tweak that especially on software
		 * renderer.
		 */
		gl.glDisable(GL10.GL_DITHER);

		/*
		 * Some one-time OpenGL initialization can be made here probably based
		 * on features of this particular context
		 */
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		gl.glClearColor(1, 1, 1, 1);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	/**
	 * @param mAngleX
	 *            - Angle to increment x position by.
	 */
	public void incrAngleX(float mAngleX) {
		this.xRot += mAngleX;
	}

	/**
	 * @param mAngley
	 *            - Angle to increment y position by.
	 */
	public void incrAngleY(float mAngleY) {
		this.yRot += mAngleY;
	}

	public float getxRot() {
		return xRot;
	}

	public float getyRot() {
		return yRot;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		/*
		 * Usually, the first thing one might want to do is to clear the screen.
		 * The most efficient way of doing this is to use glClear().
		 */

		// Set background as black
		gl.glClearColor(0, 0, 0, 1.0f);

		// Clear the screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		/*
		 * Now we're ready to draw some 3D objects
		 */

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -3.0f);
		gl.glRotatef(this.getxRot(), 0, 1, 0);
		gl.glRotatef(this.getyRot(), 1, 0, 0);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		object.draw(gl);
	}

}
