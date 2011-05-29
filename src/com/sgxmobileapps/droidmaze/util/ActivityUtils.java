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
package com.sgxmobileapps.droidmaze.util;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;


/**
 * Utilities for activities management
 * @author Massimo Gaddini
 *
 */
public class ActivityUtils {

    /**
     * Starts an Activity 
     * @param ctx The context starting the activity
     * @param target The activity's class to start
     */
    public static void launchActivity(Context ctx, Class<? extends Activity> target){
        Intent i = new Intent(ctx, target);
        ctx.startActivity(i);
    }
}
