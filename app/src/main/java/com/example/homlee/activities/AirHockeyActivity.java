package com.example.homlee.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Toast;

import com.example.homlee.R;
import com.example.homlee.Utils.IConfig;
import com.example.homlee.Utils.ResourcesReader;
import com.example.homlee.opengl.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by homlee on 2018/6/8.
 */

public class AirHockeyActivity extends BaseActivity {

    private GLSurfaceView mGLSurfaceView;
    private boolean rendererSet = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);
        if (supportsEs2()) {
            mGLSurfaceView.setEGLContextClientVersion(2);
            mGLSurfaceView.setRenderer(new AirHockeyRenderer(this));
            rendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0", Toast.LENGTH_SHORT).show();
            return;
        }

        setContentView(mGLSurfaceView);

    }

    protected boolean supportsEs2() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configInfo = activityManager.getDeviceConfigurationInfo();
        return configInfo.reqGlEsVersion >= 0x20000;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (rendererSet) {
            mGLSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (rendererSet) {
            mGLSurfaceView.onPause();
        }
    }

    class AirHockeyRenderer implements GLSurfaceView.Renderer {


        private static final int POSITION_COMPONENT_COUNT = 2;
        private static final int COLOR_COMPONENT_COUNT = 3;
        private static final int BYTES_PER_FLOAT = 4;
        private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

        //private static final String U_COLOR = "u_Color";
        private static final String A_COLOR = "a_Color";
        private static final String A_POSITION = "a_Position";
        //private int uColorLocation;
        private int aPositionLocation;
        private int aColorLocation;

        private final FloatBuffer vertexData;
        private Context mContext;
        public AirHockeyRenderer(Context context) {
            mContext = context;

            float[] tableVerticesWithTriangles = {
                    //Order of coordinates: x, y, r, g, b
                    //Triangles Fan
                    0f, 0f, 1f, 1f, 1f,
                    -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                    0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                    0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                    -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                    -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

                    //Line 1
                    -0.5f, 0f, 1f, 0f, 0f,
                    0.5f, 0f, 1f, 0f, 0f,

                    //Mallets
                    0f, -0.25f, 0f, 0f, 1f,
                    0f, 0.25f, 1f, 0f, 0f
            };

            vertexData = ByteBuffer
                    .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            vertexData.put(tableVerticesWithTriangles);
        }

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

            String vertexShaderSource = ResourcesReader.readText(mContext, R.raw.simple_vertex_shader);
            String fragmentShaderSource = ResourcesReader.readText(mContext, R.raw.simple_fragment_shader);

            int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
            int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

            int program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
            if (IConfig.debug) {
                ShaderHelper.validateProgram(program);
            }

            GLES20.glUseProgram(program);
            //uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
            aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
            aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);

            vertexData.position(0);
            GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData);
            GLES20.glEnableVertexAttribArray(aPositionLocation);

            vertexData.position(POSITION_COMPONENT_COUNT);
            GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData);
            GLES20.glEnableVertexAttribArray(aColorLocation);

        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            //GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

            //GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

            //GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

            //GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
        }
    }
}
