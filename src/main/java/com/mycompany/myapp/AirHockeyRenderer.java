package com.mycompany.myapp;

import android.content.*;
import android.opengl.*;
import com.mycompany.myapp.util.*;
import java.nio.*;
import javax.microedition.khronos.opengles.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import static android.opengl.GLES20.*;
import android.opengl.GLSurfaceView.Renderer;


public class AirHockeyRenderer implements Renderer
{
	private static final int POSITION_COMPONENT_COUNT=2;
	private static final int BYTES_PER_FLOAT=4;
	private final FloatBuffer vertexData;
	private final Context context;
	private int program;
	private static final String U_COLOR="u_Color";
	private int uColorLocation;
	private static final String A_POSITION="a_Position";
	private int aPositionLocation;
	
	public AirHockeyRenderer(Context context)
	{
		this.context=context;
		
		float[] tableVerticesWithTriangles=
		{
			//heart
			//Triangle 1
		   0f,-0.2f,
		   0f,0.3f,
		   0.08f,0.43f,
			
			//Triangle 2
	       0f,-0.2f,
			0f,0.3f,
		   -0.08f,0.43f,
			
			//Triangle 3
		   0f,-0.2f,
			0.08f,0.43f,
			0.27f,0.55f,
			
			//Triangle 4
		   0f,-0.2f,
			-0.08f,0.43f,
			-0.27f,0.55f,
			

			//Triangle 7
			0f,-0.2f,
			0.48f,0.55f,
			0.62f,0.44f,

			//Triangle 8
			0f,-0.2f,
			-0.48f,0.55f,
			-0.62f,0.44f,	
			
			//Triangle 5
			0.48f, 0.55f,
			0f,-0.2f,
			0.27f,0.55f,

			//Triangle 6
			-0.48f,0.55f,
			0f,-0.2f,
			-0.27f, 0.55f,

		    //Triangle 9
			0.73f,0.28f,
			0f,-0.2f,
			0.62f,0.44f,

			//Triangle 10
			-0.73f,0.28f,
			0f,-0.2f,
			-0.62f,0.44f,


			//Triangle 11
			0.73f,0.28f,
			0f,-0.2f,
			0.73f,0f,

			//Triangle 12
			-0.73f,0.28f,
			0f,-0.2f,
			-0.73f,0f,

			//Triangle 13
			0.73f,0f,
			0f,-0.2f,
			0.64f,-0.2f,

			//Triangle 14
			-0.73f,0f,
			0f,-0.2f,
			-0.64f,-0.2f,

	

			//Triangle 15
			-0.64f,-0.2f,
			0.64f,-0.2f,
			0f,-0.7f,
			
			//S 
			//Triangle 1
			-0.12f,0.05f,
			-0.06f,0.05f,
			-0.12f,0.1f,

			//Triangle 2
			-0.06f,0.05f,
			-0.12f,0.1f,
			-0.06f,0.1f,
			
			//Triangle 3
			-0.06f,0.1f,
			-0.12f,0.15f,
			-0.12f,0.1f,
			
			//Triangle 4
			-0.12f,0.15f,
			-0.12f,0.1f,
			-0.22f,0.1f,
			
			//Triangle 5
			-0.12f,0.15f,
			-0.22f,0.1f,
			-0.22f,0.15f,
			
			//Triangle 6
			-0.22f,0.15f,
			-0.22f,0.02f,
			-0.27f,0.1f,
			
			//Triangle 7
			-0.22f,0.02f,
			-0.27f,0.1f,
			-0.27f,0.02f,
			
			//Triangle 8
			-0.27f,0.02f,
			-0.22f,-0.02f,
			-0.12f,0.02f,
			
			//Triangle 9
			-0.12f,0.02f,
			-0.12f,-0.02f,
			-0.22f,-0.02f,
			
			//Triangle 10
			-0.12f,-0.02f,
			-0.06f,-0.02f,
			-0.12f,0.02f,
			
            //Triangle 11
			-0.06f,-0.02f,
			-0.12f,-0.02f, 
			-0.06f,-0.12f,
		
			//Triangle 12
			-0.12f,-0.02f,
			-0.06f,-0.12f,
		    -0.12f,-0.12f,
			
			//Triangle 13
			-0.12f,-0.12f,
			-0.06f,-0.12f,
			-0.12f,-0.17f,
			
			//Triangle 14
			-0.12f,-0.17f,
			-0.12f,-0.12f,
			-0.22f,-0.17f,
			
			//Triangle 15
			-0.22f,-0.17f,
			-0.22f,-0.12f,
			-0.12f,-0.12f,
			
			//Triangle 16
			-0.27f,-0.12f,
			-0.22f,-0.12f,
			-0.22f,-0.17f,
			
			//Triangle 17 
			-0.27f,-0.12f,
			-0.27f,-0.07f,
			-0.22f,-0.12f,
			
			
			
					
					};
		vertexData=ByteBuffer
	    .allocateDirect(tableVerticesWithTriangles.length*BYTES_PER_FLOAT)
	    .order(ByteOrder.nativeOrder())
	    .asFloatBuffer();
	    vertexData.put(tableVerticesWithTriangles);
	}
		
    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Set the background clear color to red. The first component is
        // red, the second is green, the third is blue, and the last
        // component is alpha, which we don't use in this lesson.
        glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
		
		String vertexShaderSource= TextResourceReader
		.readTextFileFromResource(context,R.raw.simple_vertex_shader);
		
		String fragmentShaderSource = TextResourceReader
		.readTextFileFromResource(context,R.raw.simple_fragment_shader);
		
		int vertexShader=ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader=ShaderHelper.compileFragmentShader(fragmentShaderSource);
		
		program=ShaderHelper.linkProgram(vertexShader,fragmentShader);
		
		if(LoggerConfig.ON)
		{
			ShaderHelper.validateProgram(program);
		}
		
		glUseProgram(program);
		
		uColorLocation=glGetUniformLocation(program,U_COLOR);
		
		aPositionLocation=glGetAttribLocation(program,A_POSITION);
		
		vertexData.position(0);
		
		glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GL_FLOAT,false,0,vertexData);
		
		glEnableVertexAttribArray(aPositionLocation);
		
    }

    /**
     * onSurfaceChanged is called whenever the surface has changed. This is
     * called at least once when the surface is initialized. Keep in mind that
     * Android normally restarts an Activity on rotation, and in that case, the
     * renderer will be destroyed and a new one createdk.
     * 
     * @param width
     *            The new width, in pixels.
     * @param height
     *            The new height, in pixels.
     */
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);
    }

    /**
     * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
     * this is done at the refresh rate of the screen.
     */
    
	@Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
		
        glClear(GL_COLOR_BUFFER_BIT);
		
		glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,0,30);
		glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,30,15);
		glUniform4f(uColorLocation,1.0f,1.0f,0.0f,1.0f);
		glDrawArrays(GL_TRIANGLES,45,53);
		
		
		
	
    }
}

