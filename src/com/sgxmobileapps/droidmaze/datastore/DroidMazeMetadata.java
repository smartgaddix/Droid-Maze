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
package com.sgxmobileapps.droidmaze.datastore;

import android.provider.BaseColumns;


/**
 * This class defines some metadata costants used by datastore. Contains 
 * table names and column names.
 * 
 * @author Massimo Gaddini
 */
public class DroidMazeMetadata {
    static final String  DATABASE_NAME       =   "droidmaze.db";
    static final int     DATABASE_VERSION    =   1;
   
    public static final class GameProfile implements BaseColumns {
        public static final String GAMEPROFILE_TABLE_NAME = "GAMEPROFILE";
        
        public static final String GAMEPROFILE_ID_COL = BaseColumns._ID;
        protected static final int GAMEPROFILE_ID_IDX = 0;
        public static final String GAMEPROFILE_PROFILEID_COL = "ProfileId";
        protected static final int GAMEPROFILE_PROFILEID_IDX = 1;
        public static final String GAMEPROFILE_LEVEL_COL = "Level";
        protected static final int GAMEPROFILE_LEVEL_IDX = 2;
        public static final String GAMEPROFILE_TOTALTIME_COL = "TotalTime";
        protected static final int GAMEPROFILE_TOTALTIME_IDX = 3;
        
    }
}
