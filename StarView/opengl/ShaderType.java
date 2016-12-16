package jlabs.fepp.StarView.opengl;


import android.opengl.GLES20;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * Created by Kashyap Sharma on 01/02/15.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({GLES20.GL_VERTEX_SHADER, GLES20.GL_FRAGMENT_SHADER})
public @interface ShaderType {
}
