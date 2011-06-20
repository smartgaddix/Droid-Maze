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
package com.sgxmobileapps.droidmaze.game;


/**
 * This class contains information about current game profile.
 * 
 * @author Massimo Gaddini
 * Jun 13, 2011
 */
public class GameProfile {
    
    private String mProfileId;
    private int mLevel;
    private long mTotalTime;
    
    /**
     * 
     */
    public GameProfile() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * @return the profileId
     */
    public String getProfileId() {
        return mProfileId;
    }

    /**
     * @param profileId the profileId to set
     */
    public void setProfileId(String profileId) {
        mProfileId = profileId;
    }
    
    /**
     * @return the level
     */
    public int getLevel() {
        return mLevel;
    }
    
    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        mLevel = level;
    }
    
    /**
     * @return the totalTime
     */
    public long getTotalTime() {
        return mTotalTime;
    }
    
    /**
     * @param totalTime the totalTime to set
     */
    public void setTotalTime(long totalTime) {
        mTotalTime = totalTime;
    }
}
