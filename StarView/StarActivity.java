package jlabs.fepp.StarView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import jlabs.fepp.StarView.opengl.ParticleSystemRenderer;


public class StarActivity extends GLSurfaceView {

    private ActivityManager activityManager;
    private ConfigurationInfo configurationInfo;
    private boolean supportsEs2;

    public StarActivity(Context context) {
        super(context);

        if(!isInEditMode()) {

            activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            configurationInfo = activityManager.getDeviceConfigurationInfo();
            supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

            if (supportsEs2) {
                // Request an OpenGL ES 2.0 compatible context.
                this.setEGLContextClientVersion(2);

                // Set the renderer to our demo renderer, defined below.
                ParticleSystemRenderer mRenderer = new ParticleSystemRenderer(this);
                this.setRenderer(mRenderer);
                this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
            } else {
                if(!isInEditMode()) throw new UnsupportedOperationException();
            }

        }

    }

    public StarActivity(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode()) {

            activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            configurationInfo = activityManager.getDeviceConfigurationInfo();
            supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

            if (supportsEs2) {
                // Request an OpenGL ES 2.0 compatible context.
                this.setEGLContextClientVersion(2);

                // Set the renderer to our demo renderer, defined below.
                ParticleSystemRenderer mRenderer = new ParticleSystemRenderer(this);
                this.setRenderer(mRenderer);
                this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
            } else {
                if(!isInEditMode()) throw new UnsupportedOperationException();
            }

        }

    }


}
