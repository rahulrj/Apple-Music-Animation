package com.haydntrigg.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class Square {

    public static Square DrawSquare = null;
    public static void InitSquare()
    {
        DrawSquare = new Square();
    }

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 2;
    static float squareCoords[] = {
            -0.5f, -0.5f,     // bottom left
            0.5f, -0.5f,      // bottom right
            0.5f,  0.5f,      // top right
            -0.5f,  0.5f};    // top left


    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    public static Vector4f Color = new Vector4f();


    private final String vertexShaderCode =
            "attribute vec2 vPosition;" +
                    "varying vec2 TexCoord;" +
                    "uniform mat4 vMatrix;" +
                    "void main() {" +
                    "  gl_Position = vMatrix * vec4(vPosition,0,1);" +
                    "  TexCoord = vPosition.st + 0.5;" +
                    "  TexCoord.t = 1.0 - TexCoord.t;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D vTexture;" +
                    "varying vec2 TexCoord;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(vTexture,TexCoord.st).rgba * vColor;" +
                    "  gl_FragColor *= gl_FragColor.a;" +
                    "}";

    int mProgram;
    int mPositionHandle,mColorHandle,mMatrixHandle,mTextureHandle;
    static final int vertexStride = COORDS_PER_VERTEX * 4;
    static final int vertexCount = 4;

    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4); // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2); // (# of coordinate values * 2 bytes per short)
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // get handle to fragment shader's vMatrix member
        mMatrixHandle = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        // get handle to fragment shader's vTexture member
        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "vTexture");
        GLES20.glUniform1i(mTextureHandle,0);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public void draw(Matrix4f matrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        float color[] = { Color.x,Color.y,Color.z,Color.w };
        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Set color for drawing the triangle
        float f[] = new float[16];
        matrix.get(f,0);
        GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,f,0);

        // Draw the triangle
        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_SHORT, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
