
package com.sgxmobileapps.droidmaze.ui.shape;

import android.content.Context;

import org.anddev.andengine.opengl.font.FontManager;
import org.anddev.andengine.opengl.texture.TextureManager;


public interface ComplexShape {
    
    void loadResources(TextureManager textureManager, FontManager fontManager, Context ctx);
    
    void initShape();
    
    
}
