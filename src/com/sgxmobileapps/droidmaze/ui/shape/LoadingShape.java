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
package com.sgxmobileapps.droidmaze.ui.shape;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.primitive.BaseRectangle;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.font.FontManager;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.Callback;


/**
 * Simple shape 
 * @author Massimo Gaddini
 *
 */
public class LoadingShape extends BaseRectangle implements ComplexShape {
	
	/**
	 * Texture region for the background image
	 */
	private TextureRegion mBackgroundTextureRegion;
	
	/**
	 * 
	 */
	private Sprite mBackgroundSprite;
	

	/**
	 * Creates a LoadingShape class
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 */
	public LoadingShape(float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight);
	}

	/* (non-Javadoc)
     * @see com.sgxmobileapps.droidmaze.ui.shape.ComplexShape#enable(org.anddev.andengine.engine.Engine)
     */
    @Override
    public void enable(Engine engine) {
    	setVisible(true);
    }

    /* (non-Javadoc)
     * @see com.sgxmobileapps.droidmaze.ui.shape.ComplexShape#init(boolean, org.anddev.andengine.util.Callback, org.anddev.andengine.util.Callback)
     */
    @Override
    public void init(boolean visible, Callback<Boolean> callback,
            Callback<Exception> exceptionCallback) {
    	
    	setVisible(visible);
    	
    	/* transparent white */
        setColor(1.0f, 1.0f, 1.0f, 0.0f);
        
    	mBackgroundSprite = new Sprite(getX(), getY(), getWidthScaled(), getHeightScaled(), mBackgroundTextureRegion);
    	
    	attachChild(mBackgroundSprite);
        
        if (callback != null)
            callback.onCallback(true);
    }

    /* (non-Javadoc)
     * @see com.sgxmobileapps.droidmaze.ui.shape.ComplexShape#loadResources(org.anddev.andengine.opengl.texture.TextureManager, org.anddev.andengine.opengl.font.FontManager, android.content.Context)
     */
    @Override
    public void loadResources(TextureManager textureManager, FontManager fontManager, Context ctx) {
        AssetBitmapTextureAtlasSource backgroundTextureAtlasSource = new AssetBitmapTextureAtlasSource(ctx, "gfx/splash_screen.png");
        BitmapTextureAtlas backgroundTextureAtlas = BitmapTextureAtlasFactory.createForTextureAtlasSourceSize(BitmapTextureFormat.RGBA_8888, backgroundTextureAtlasSource, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(backgroundTextureAtlas, backgroundTextureAtlasSource, 0, 0);

        textureManager.loadTexture(backgroundTextureAtlas);
    }
    
    /* (non-Javadoc)
     * @see org.anddev.andengine.entity.shape.Shape#doDraw(javax.microedition.khronos.opengles.GL10, org.anddev.andengine.engine.camera.Camera)
     */
    @Override
    protected void doDraw(GL10 pGL, Camera pCamera) {
    }

}
