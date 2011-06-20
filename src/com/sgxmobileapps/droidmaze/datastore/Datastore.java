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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.sgxmobileapps.droidmaze.game.GameProfile;
import static com.sgxmobileapps.droidmaze.datastore.DroidMazeMetadata.GameProfile.*;

/**
 * This class contains utility methods and classes for the application datastore.
 *  
 * @author Massimo Gaddini
 * Jun 11, 2011
 */
public abstract class Datastore{
    
    private static final String TAG = "DroidMazeDatastore";
    
    private static class DbHelper extends SQLiteOpenHelper {
        
        private Datastore mDatastore;
        
        public DbHelper(Context context, String name, CursorFactory factory, int version, Datastore datastore) {
            super(context, name, factory, version);
            mDatastore = datastore;
        }

        // Called when no database exists in disk and the helper class needs
        // to create a new one. 
        @Override
        public void onCreate(SQLiteDatabase db) {      
            db.execSQL(SQL_GAMEPROFILE_CREATE_TABLE);
        }

        // Called when there is a database version mismatch meaning that the version
        // of the database on disk needs to be upgraded to the current version.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Log the version upgrade.
            Log.w(TAG, "Upgrading from version " + 
                        oldVersion + " to " +
                        newVersion + ", which will destroy all old data");
            
            if (mDatastore.onDatastoreUpgrade(db, oldVersion, newVersion))
                return;
            
            // Upgrade the existing database to conform to the new version. Multiple 
            // previous versions can be handled by comparing _oldVersion and _newVersion
            // values.

            // The simplest case is to drop the old table and create a new one.
            db.execSQL(SQL_GAMEPROFILE_DROP_TABLE);
            
            // Create a new one.
            onCreate(db);
        }
    }
    
    
    // Context of the application using the database.
    private final Context mContext;
    
    // Database open/upgrade helper
    private DbHelper mDbHelper;
    
    /**
     * The database instance
     */
    protected SQLiteDatabase mDb;
    
    /**
     * Initializes the data store.
     * 
     * @param context the context
     */
    public Datastore(Context context) {
        mContext = context;
        mDbHelper = new DbHelper(mContext, DroidMazeMetadata.DATABASE_NAME, null, DroidMazeMetadata.DATABASE_VERSION, this);
    }
    
    /**
     * Opens a connection with the underlying data base
     * @return the Datastore instance
     * @throws SQLException if error occurred
     */
    public Datastore open() throws SQLException { 
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
                                                     
    /**
     * Closes the connection with the underlying data base
     */
    public void close() {
        mDb.close();
    }
    
    /**
     * Called from Datastore during the upgrading phase of the data base schema.
     * Derived classes can override this method if they need to do some work 
     * during the upgrade process 
     * @param db the SQLiteDatabase
     * @param oldVersion schema's old version
     * @param newVersion schema's new version
     * @return if the method return false the old schema will be destroyed and
     * the new schema will be created. If return true the upgrade process terminates
     */
    public boolean onDatastoreUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        return false;
    }
    
    /***************************************************************************/
	/*                          TABLES DROP/CREATION                           */
    /***************************************************************************/    

	// GameProfile table creation 
	private static final String SQL_GAMEPROFILE_CREATE_TABLE = 
	    "CREATE TABLE IF NOT EXISTS " + GAMEPROFILE_TABLE_NAME + " (" + 
		 GAMEPROFILE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		 GAMEPROFILE_PROFILEID_COL + " TEXT NOT NULL UNIQUE, " +
		 GAMEPROFILE_LEVEL_COL + " INTEGER, " + 
		 GAMEPROFILE_TOTALTIME_COL + " INTEGER " + 
		 ");";
	
	//GameProfile Drop table
	private static final String SQL_GAMEPROFILE_DROP_TABLE = 
        "DROP TABLE IF EXISTS " + GAMEPROFILE_TABLE_NAME + ";";

	/***************************************************************************/
    /*                            HELPER FUNCTIONS                             */
    /***************************************************************************/ 
		
	/********************** GameProfile helper functions ***********************/

	private ContentValues getContentValuesFromGameProfile(GameProfile gameProfile){
        ContentValues contentValues = new ContentValues();
        contentValues.put(GAMEPROFILE_PROFILEID_COL, gameProfile.getProfileId());
        contentValues.put(GAMEPROFILE_LEVEL_COL, gameProfile.getLevel());
        contentValues.put(GAMEPROFILE_TOTALTIME_COL, gameProfile.getTotalTime());
        return contentValues;
    }
    
    private GameProfile fillGameProfileFromCursor(Cursor cursor){
        GameProfile gameProfile = new GameProfile();
        gameProfile.setProfileId(cursor.getString(GAMEPROFILE_PROFILEID_IDX));
        gameProfile.setLevel(cursor.getInt(GAMEPROFILE_LEVEL_IDX));
        gameProfile.setTotalTime(cursor.getLong(GAMEPROFILE_TOTALTIME_IDX));
        return gameProfile;
    }
    
    public long addGameProfile(GameProfile gameProfile) {
        return mDb.insert(GAMEPROFILE_TABLE_NAME, null, getContentValuesFromGameProfile(gameProfile));
    }
    
    public long updateGameProfile(GameProfile gameProfile) {
        String where = GAMEPROFILE_PROFILEID_COL + " = '" + gameProfile.getProfileId() + "'";
        return mDb.update(GAMEPROFILE_TABLE_NAME, getContentValuesFromGameProfile(gameProfile), where, null);
    }

	public long updateGameProfile(long id, GameProfile gameProfile) {
		String where = GAMEPROFILE_ID_COL + " = " + id;
		return mDb.update(GAMEPROFILE_TABLE_NAME, getContentValuesFromGameProfile(gameProfile), where, null);
	}

	public boolean removeGameProfile(long id) {
	    String where = GAMEPROFILE_ID_COL + " = " + id;
		return mDb.delete(GAMEPROFILE_TABLE_NAME, where, null) > 0;
	}

	public boolean removeAllGameProfile() {
		return mDb.delete(GAMEPROFILE_TABLE_NAME, null, null) > 0;
	}
	
	public GameProfile getGameProfile(long id) {
        Cursor res = mDb.query(GAMEPROFILE_TABLE_NAME, 
                new String[] {
                GAMEPROFILE_PROFILEID_COL,
                GAMEPROFILE_LEVEL_COL,
                GAMEPROFILE_TOTALTIME_COL}, 
                GAMEPROFILE_ID_COL + " = " + id, 
                null, null, null, null);
                
        if(res == null){
            return null;
        }
        
        res.moveToFirst();
        
        GameProfile gameProfile = fillGameProfileFromCursor(res);
        res.close();
        
        return gameProfile;
    }
	
	public GameProfile getGameProfile(String profileId) {
        Cursor res = mDb.query(GAMEPROFILE_TABLE_NAME, 
                new String[] {
                GAMEPROFILE_PROFILEID_COL,
                GAMEPROFILE_LEVEL_COL,
                GAMEPROFILE_TOTALTIME_COL}, 
                GAMEPROFILE_PROFILEID_COL + " = '" + profileId + "'", 
                null, null, null, null);
        
        if(res == null){
            return null;
        }
        
        if (!res.moveToFirst()){
            res.close();
            return null;
        }
        
        GameProfile gameProfile = fillGameProfileFromCursor(res);
        res.close();
        
        return gameProfile;
    }

	public Cursor getAllGameProfileCursor() {
		return mDb.query(GAMEPROFILE_TABLE_NAME, 
		        new String[] {
                GAMEPROFILE_PROFILEID_COL,
                GAMEPROFILE_LEVEL_COL,
                GAMEPROFILE_TOTALTIME_COL}, 
                null, null, null, null, null);
	}
	
	public GameProfile[] getAllGameProfile() {
	    GameProfile[] profiles = null;
        Cursor cursor = getAllGameProfileCursor();

        if (cursor == null) {
            return new GameProfile[0];
        }

        profiles = new GameProfile[cursor.getCount()];

        if (cursor.moveToFirst()) {
            do {
                profiles[cursor.getPosition()] = fillGameProfileFromCursor(cursor);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return profiles;
    }
	
	/***************************************************************************/
}