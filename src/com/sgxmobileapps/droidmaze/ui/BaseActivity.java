/**
 * Copyright 2011 Massimo Gaddini
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  
 */
package com.sgxmobileapps.droidmaze.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.ui.activity.BaseGameActivity;

/**
 * @author Massimo Gaddini
 *
 */
public abstract class BaseActivity extends BaseGameActivity {
    private static final float SCORE_BAR_HEIGHT_PERC = 0.1f;
    private static final float AD_BAR_HEIGHT_PERC = 0.1f;

    private Camera             mCamera;
    private int                mCameraWidth;
    private int                mCameraHeight;
    private int                mScoreBarWidth;
    private int                mScoreBarHeight;
    private int                mAdBarWidth;
    private int                mAdBarHeight;
    private int                mMazeAreaWidth;
    private int                mMazeAreaHeight;
    
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        computeDimension();
        super.onCreate(pSavedInstanceState);
    }
    
    public Engine onLoadEngine() {
        mCamera = new Camera(0, 0, mCameraWidth, mCameraHeight);
        return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
                new RatioResolutionPolicy(getCameraWidth(), getCameraHeight()), getCamera()));
    }
    
    protected void launch(Class<?> target){
        Intent i = new Intent(this, target);
        startActivity(i);
    }
    
    
    /**
     * @return the camera
     */
    protected Camera getCamera() {
        return mCamera;
    }

    
    /**
     * @return the cameraWidth
     */
    protected int getCameraWidth() {
        return mCameraWidth;
    }

    
    /**
     * @return the cameraHeight
     */
    protected int getCameraHeight() {
        return mCameraHeight;
    }

    
    /**
     * @return the scoreBarWidth
     */
    protected int getScoreBarWidth() {
        return mScoreBarWidth;
    }

    
    /**
     * @return the scoreBarHeight
     */
    protected int getScoreBarHeight() {
        return mScoreBarHeight;
    }

    
    /**
     * @return the adBarWidth
     */
    protected int getAdBarWidth() {
        return mAdBarWidth;
    }

    
    /**
     * @return the adBarHeight
     */
    protected int getAdBarHeight() {
        return mAdBarHeight;
    }

    
    /**
     * @return the mazeAreaWidth
     */
    protected int getMazeAreaWidth() {
        return mMazeAreaWidth;
    }

    
    /**
     * @return the mazeAreaHeight
     */
    protected int getMazeAreaHeight() {
        return mMazeAreaHeight;
    }

    private void computeDimension() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mCameraHeight = dm.heightPixels;
        mCameraWidth = dm.widthPixels;

        mScoreBarHeight = (int) ( mCameraHeight * SCORE_BAR_HEIGHT_PERC );
        mScoreBarWidth = mCameraWidth;
        
        mAdBarHeight = (int) ( mCameraHeight * AD_BAR_HEIGHT_PERC );
        mAdBarWidth = mCameraWidth;

        mMazeAreaHeight = mCameraHeight - mScoreBarHeight - mAdBarHeight;
        mMazeAreaWidth = mCameraWidth;
    }
    
    
}
