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

import android.app.Activity;
import android.util.DisplayMetrics;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.ui.activity.BaseSplashActivity;


/**
 * @author Massimo Gaddini
 *
 */
public class MainSplashActivity extends BaseSplashActivity {

    private int                mCameraWidth;
    private int                mCameraHeight;

    
    /* (non-Javadoc)
     * @see org.anddev.andengine.ui.activity.BaseSplashActivity#getFollowUpActivity()
     */
    @Override
    protected Class<? extends Activity> getFollowUpActivity() {
        return MainMenuActivity.class;
    }

    /* (non-Javadoc)
     * @see org.anddev.andengine.ui.activity.BaseSplashActivity#getScreenOrientation()
     */
    @Override
    protected ScreenOrientation getScreenOrientation() {
        return ScreenOrientation.PORTRAIT;
    }

    /* (non-Javadoc)
     * @see org.anddev.andengine.ui.activity.BaseSplashActivity#getSplashDuration()
     */
    @Override
    protected float getSplashDuration() {
        return 5;
    }

    /* (non-Javadoc)
     * @see org.anddev.andengine.ui.activity.BaseSplashActivity#onGetSplashTextureSource()
     */
    @Override
    protected IBitmapTextureAtlasSource onGetSplashTextureAtlasSource() {
        return new AssetBitmapTextureAtlasSource(this, "gfx/splash_screen.png");
    }

    /* (non-Javadoc)
     * @see org.anddev.andengine.ui.activity.BaseSplashActivity#getSplashCamera(int, int)
     */
    @Override
    protected Camera getSplashCamera(int pSplashwidth, int pSplashHeight) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        mCameraHeight = dm.heightPixels;
        mCameraWidth = dm.widthPixels;
        
        return new Camera(0, 0, mCameraWidth, mCameraHeight);
    }

    /* (non-Javadoc)
     * @see org.anddev.andengine.ui.activity.BaseSplashActivity#getSplashResolutionPolicy(int, int)
     */
    @Override
    protected IResolutionPolicy getSplashResolutionPolicy(int pSplashwidth, int pSplashHeight) {
        return new RatioResolutionPolicy(mCameraWidth, mCameraHeight);
    }
}
