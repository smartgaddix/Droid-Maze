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

import android.content.Context;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.opengl.font.FontManager;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Callback;


/**
 * Interface for complex shape ui components.
 * @author Massimo Gaddini
 *
 */
public interface ComplexShape {
    /**
     * Loads resources (texture, fonts) needed by complex shape. Typically called in {@link BaseGameActivity#onLoadResources} 
     * @param textureManager the texture manager of the application
     * @param fontManager the font manager of the application
     * @param ctx the Context
     */
    void loadResources(TextureManager textureManager, FontManager fontManager, Context ctx);
    
    /**
     * Initializes the complex shape sub components
     * 
     * @param visibile Initial visibility of the shape
     */
    /**
     * Initializes the complex shape sub components and call the callback when finished (if specified)
     * @param visible Initial visibility of the shape
     * @param callback The callback to be called whehn finished. The argument is a boolean indicating the result of initialization
     * @param exceptionCallback The callback to be call if exception occurred. The argument is the exception.
     */
    public void init(boolean visible, final Callback<Boolean> callback, final Callback<Exception> exceptionCallback);
    
    /**
     * Enables update handler and so on
     * @param engine
     */
    void enable(Engine engine);
}
