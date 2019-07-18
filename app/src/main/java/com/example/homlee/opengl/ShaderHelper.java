package com.example.homlee.opengl;

import android.opengl.GLES20;
import android.util.Log;

import com.example.homlee.utils.IConfig;

/**
 * Created by homlee on 2018/6/12.
 */

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";
    private ShaderHelper(){}

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderId = GLES20.glCreateShader(type);
        if (shaderId == 0) {
            Log.e(TAG, "compileShader: could not create new shader.");
            return 0;
        }

        GLES20.glShaderSource(shaderId, shaderCode);
        GLES20.glCompileShader(shaderId);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        if (IConfig.DEBUG) {
            Log.i(TAG, "compileShader: compile result = " + GLES20.glGetShaderInfoLog(shaderId));
        }

        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderId);
            Log.e(TAG, "compileShader: Compilation of shader failed.");
            return 0;
        }

        return shaderId;
    }

    public static int linkProgram(int vertexShader, int fragmentShader) {
        final int programId = GLES20.glCreateProgram();

        if (programId == 0) {
            Log.e(TAG, "linkProgram: Could not create new program.");
            return 0;
        }

        GLES20.glAttachShader(programId, vertexShader);
        GLES20.glAttachShader(programId, fragmentShader);

        GLES20.glLinkProgram(programId);

        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0);

        if (IConfig.DEBUG) {
            Log.i(TAG, "linkProgram: Result of linking program : " + GLES20.glGetProgramInfoLog(programId));
        }

        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programId);
            Log.e(TAG, "linkProgram: Linking of program failed.");
            return 0;
        }

        return programId;
    }

    public static boolean validateProgram(int programId) {
        GLES20.glValidateProgram(programId);
        final int[] validateStatus = new int[1];

        GLES20.glGetProgramiv(programId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.i(TAG, "validateProgram: Results of validating program is " + GLES20.glGetProgramInfoLog(programId));
        return validateStatus[0] != 0;
    }

}
