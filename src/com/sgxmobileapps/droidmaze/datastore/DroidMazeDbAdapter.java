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
import android.util.Log;


/**
 * This class contains utility methods and classes for the application datastore.
 * 
 * @author Massimo Gaddini
 * 
 */
public class DroidMazeDbAdapter {

    /**
     * Database open/upgrade helper
     * 
     */
    private DroidMazeDbAdapter.DbHelper mDbHelper;
    /**
     * The database instance
     * 
     */
    private SQLiteDatabase mDb;
    private final static String SQL_GAMEPLAYERPROFILE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+ DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TABLE_NAME +" ("+
    DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_ID_COL +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_PROFILEID_COL +" TEXT NOT NULL, "+
    DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_LEVEL_COL +" INTEGER, "+
    DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TOTALTIME_COL +" INTEGER, "+
    DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_CURRENT_COL +" INTEGER"+", "+
    "UNIQUE ("+ DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_PROFILEID_COL +")"+" );";
    private final static String SQL_GAMEPLAYERPROFILE_DROP_TABLE = "DROP TABLE IF EXISTS "+ DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TABLE_NAME +";";

    /**
     * Initializes the data store
     * 
     * @param context
     *     the context
     */
    public DroidMazeDbAdapter(Context context) {
        mDbHelper = new DroidMazeDbAdapter.DbHelper(context, DroidMazeDbMetadata.DATABASE_NAME, null, DroidMazeDbMetadata.DATABASE_VERSION, this);
    }

    /**
     * Opens a connection with the underlying data base
     * 
     * @return
     *     the Datastore instance
     * @throws SQLException
     *     if error occurred
     */
    public DroidMazeDbAdapter open()
        throws SQLException
    {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Closes the connection with the underlying data base
     * 
     */
    public void close() {
        if (mDb!= null) {
            mDb.close();
        }
    }

    /**
     * Called from Datastore during the upgrading phase of the data base schema. Derived classes can override this method if they need to do some work during the upgrade process
     * 
     * @param db
     *     the SQLiteDatabase
     * @param newVersion
     *     schema's new version
     * @param oldVersion
     *     schema's old version
     * @return
     *     if the method return false the old schema will be destroyed and the new schema will be created. If return true the upgrade process terminates
     */
    public Boolean onDatastoreUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        return false;
    }

    private ContentValues getContentValues(com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile gamePlayerProfile) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_PROFILEID_COL, gamePlayerProfile.getProfileId());
        contentValues.put(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_LEVEL_COL, gamePlayerProfile.getLevel());
        contentValues.put(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TOTALTIME_COL, gamePlayerProfile.getTotalTime());
        contentValues.put(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_CURRENT_COL, gamePlayerProfile.isCurrent());
        return contentValues;
    }

    private com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile fillFromCursorGamePlayerProfile(Cursor cursor) {
        com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile gamePlayerProfile = new com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile();
        gamePlayerProfile.setProfileId(cursor.getString(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_PROFILEID_IDX));
        gamePlayerProfile.setLevel(((int) cursor.getLong(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_LEVEL_IDX)));
        gamePlayerProfile.setTotalTime(cursor.getLong(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TOTALTIME_IDX));
        gamePlayerProfile.setCurrent((cursor.getLong(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_CURRENT_IDX)> 0));
        return gamePlayerProfile;
    }

    /**
     * Inserts a GamePlayerProfile to database
     * 
     * @param gamePlayerProfile
     *     The GamePlayerProfile to insert
     * @return
     *     the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long addGamePlayerProfile(com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile gamePlayerProfile) {
        return mDb.insert(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TABLE_NAME, null, getContentValues(gamePlayerProfile));
    }

    /**
     * Updates a GamePlayerProfile in database
     * 
     * @param id
     *     The id of the GamePlayerProfile to update
     * @param gamePlayerProfile
     *     The GamePlayerProfile to update
     * @return
     *     the number of rows affected (1 or 0)
     */
    public long updateGamePlayerProfile(long id, com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile gamePlayerProfile) {
        String where = DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_ID_COL +" = "+ id;
        return mDb.update(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TABLE_NAME, getContentValues(gamePlayerProfile), where, null);
    }

    /**
     * Updates a GamePlayerProfile in database
     * 
     * @param gamePlayerProfile
     *     The GamePlayerProfile to update
     * @return
     *     the number of rows affected (1 or 0)
     */
    public long updateGamePlayerProfile(com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile gamePlayerProfile) {
        String where = DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_PROFILEID_COL +" = '"+ gamePlayerProfile.getProfileId()+"'";
        return mDb.update(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TABLE_NAME, getContentValues(gamePlayerProfile), where, null);
    }

    /**
     * Deletes a GamePlayerProfile from database
     * 
     * @param id
     *     The id of the GamePlayerProfile to delete
     * @return
     *     the number of rows affected (1 or 0)
     */
    public long deleteGamePlayerProfile(long id) {
        String where = DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_ID_COL +" = "+ id;
        return mDb.delete(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TABLE_NAME, where, null);
    }

    /**
     * Deletes a GamePlayerProfile from database
     * 
     * @param gamePlayerProfile
     *     The GamePlayerProfile to delete
     * @return
     *     the number of rows affected (1 or 0)
     */
    public long deleteGamePlayerProfile(com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile gamePlayerProfile) {
        String where = DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_PROFILEID_COL +" = '"+ gamePlayerProfile.getProfileId()+"'";
        return mDb.delete(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TABLE_NAME, where, null);
    }

    /**
     * Deletes all GamePlayerProfile from database
     * 
     * @return
     *     the number of rows deleted
     */
    public long deleteAllGamePlayerProfile() {
        return mDb.delete(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TABLE_NAME, "1", null);
    }

    /**
     * Reads a GamePlayerProfile from database with given id
     * 
     * @param id
     *     The id of the GamePlayerProfile to read
     * @return
     *     the entity read or null if one entity with the specified id doesn't exist
     */
    public com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile getGamePlayerProfile(long id) {
        String where = DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_ID_COL +" = "+ id;
        String[] cols = new String[] {DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_ID_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_PROFILEID_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_LEVEL_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TOTALTIME_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_CURRENT_COL };
        Cursor cursor;
        cursor = mDb.query(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TABLE_NAME, cols, where, null, null, null, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_DEFAULT_ORDER);
        if (cursor == null) {
            return null;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile res;
        res = fillFromCursorGamePlayerProfile(cursor);
        cursor.close();
        return res;
    }

    /**
     * Reads a GamePlayerProfile from database with given key fields
     * 
     * @param profileId
     *     The ProfileId field
     * @return
     *     the entity read or null if one entity with the specified key doesn't exist
     */
    public com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile getGamePlayerProfile(String profileId) {
        String where = DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_PROFILEID_COL +" = '"+ profileId +"'";
        String[] cols = new String[] {DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_ID_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_PROFILEID_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_LEVEL_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TOTALTIME_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_CURRENT_COL };
        Cursor cursor;
        cursor = mDb.query(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TABLE_NAME, cols, where, null, null, null, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_DEFAULT_ORDER);
        if (cursor == null) {
            return null;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile res;
        res = fillFromCursorGamePlayerProfile(cursor);
        cursor.close();
        return res;
    }

    /**
     * Returns a cursor for GamePlayerProfile
     * 
     * @return
     *     the cursor
     */
    public Cursor getCursorGamePlayerProfile() {
        String[] cols = new String[] {DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_ID_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_PROFILEID_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_LEVEL_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TOTALTIME_COL, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_CURRENT_COL };
        return mDb.query(DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_TABLE_NAME, cols, null, null, null, null, DroidMazeDbMetadata.GamePlayerProfile.GAMEPLAYERPROFILE_DEFAULT_ORDER);
    }

    /**
     * Returns the array of GamePlayerProfile fetched from database
     * 
     * @return
     *     the array of GamePlayerProfile
     */
    public com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile[] getAllGamePlayerProfile() {
        Cursor cursor = getCursorGamePlayerProfile();
        if (cursor == null) {
            return new com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile[ 0 ] ;
        }
        com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile[] entities = new com.sgxmobileapps.droidmaze.game.model.GamePlayerProfile[cursor.getCount()] ;
        if (cursor.moveToFirst()) {
            do {
                entities[cursor.getPosition()] = fillFromCursorGamePlayerProfile(cursor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return entities;
    }

    private static class DbHelper
        extends SQLiteOpenHelper
    {

        private DroidMazeDbAdapter mDatastore;

        public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DroidMazeDbAdapter datastore) {
            super(context, name, factory, version);
            mDatastore = datastore;
        }

        /**
         * Called when no database exists in disk and the helper class needs to create a new one
         * 
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_GAMEPLAYERPROFILE_CREATE_TABLE);
        }

        /**
         * Called when there is a database version mismatch meaning that the version of the database on disk needs to be upgraded to the current version
         * 
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("DroidMazeDbAdapter", "Upgrading from version "+ oldVersion +" to "+ newVersion +", which will destroy all old data");
            if (mDatastore.onDatastoreUpgrade(db, oldVersion, newVersion)) {
                return ;
            }
            // Upgrade the existing database to conform to the new version. Multiple previous versions can be handled by comparing _oldVersion and _newVersion values
            db.execSQL(SQL_GAMEPLAYERPROFILE_DROP_TABLE);
            // The simplest case is to drop the old table and create a new one.
            // Create a new one.
            onCreate(db);
        }

    }

}
