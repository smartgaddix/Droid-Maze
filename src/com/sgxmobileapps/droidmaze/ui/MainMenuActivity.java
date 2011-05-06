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


import com.sgxmobileapps.droidmaze.ui.animator.MultiSlideMenuAnimator;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.modifier.ease.EaseBackOut;

import javax.microedition.khronos.opengles.GL10;


/**
 * @author Massimo Gaddini
 *
 */
public class MainMenuActivity extends BaseActivity implements MenuScene.IOnMenuItemClickListener {
    
    protected static final int MENU_START = 0;
    protected static final int MENU_QUIT = MENU_START + 1;

    protected Texture mMenuTexture;
    protected TextureRegion mMenuStartTextureRegion;
    protected TextureRegion mMenuQuitTextureRegion;
//    protected TextureRegion mBackgroundTextureRegion;


    /* (non-Javadoc)
     * @see org.anddev.andengine.ui.IGameInterface#onLoadComplete()
     */
    public void onLoadComplete() {
    }

    /* (non-Javadoc)
     * @see org.anddev.andengine.ui.IGameInterface#onLoadResources()
     */
    public void onLoadResources() {
        mMenuTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mMenuStartTextureRegion = TextureRegionFactory.createFromAsset(mMenuTexture, this, "gfx/menu_start.png", 0, 0);
        mMenuQuitTextureRegion = TextureRegionFactory.createFromAsset(mMenuTexture, this, "gfx/menu_exit.png", 0, 50);
        mEngine.getTextureManager().loadTexture(mMenuTexture);
        
//        ITextureSource textureSource = new AssetTextureSource(this, "gfx/menu_background.jpg");
//        final Texture backgroundTexture = TextureFactory.createForTextureSourceSize(textureSource, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//        mBackgroundTextureRegion = TextureRegionFactory.createFromSource(backgroundTexture, textureSource, 0, 0);
//        mEngine.getTextureManager().loadTexture(backgroundTexture);
    }

    /* (non-Javadoc)
     * @see org.anddev.andengine.ui.IGameInterface#onLoadScene()
     */
    public Scene onLoadScene() {
        Scene mainScene = new Scene(1);
        
        MenuScene menuScene = new MenuScene(getCamera());
        
        IMenuItem startMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_START, mMenuStartTextureRegion), (float) 1.2, 1);
        startMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(startMenuItem);

        IMenuItem quitMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_QUIT, mMenuQuitTextureRegion), (float) 1.2, 1);
        quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(quitMenuItem);
        
//        Sprite backgroundSprite = new Sprite(getCamera().getMinX(), getCamera().getMinY(), getCamera().getWidth(), getCamera().getHeight(), mBackgroundTextureRegion);
//        menuScene.setBackground(new SpriteBackground(backgroundSprite));
        
        MultiSlideMenuAnimator menuAnimator = new MultiSlideMenuAnimator(20, EaseBackOut.getInstance());
        menuAnimator.setDuration(5);
        menuScene.setMenuAnimator(menuAnimator);
        menuScene.buildAnimations();

        menuScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
        menuScene.setOnMenuItemClickListener(this);

        mainScene.setChildScene(menuScene, false, true, true);        
        return mainScene;
    }

    /* (non-Javadoc)
     * @see org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener#onMenuItemClicked(org.anddev.andengine.entity.scene.menu.MenuScene, org.anddev.andengine.entity.scene.menu.item.IMenuItem, float, float)
     */
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
            float pMenuItemLocalX, float pMenuItemLocalY) {
        
        switch(pMenuItem.getID()) {
        case MENU_START:
            launch(MazeActivity.class);
            return true;
        case MENU_QUIT:        
            finish();
            return true;
        default:
            return false;
        }
    }
}
