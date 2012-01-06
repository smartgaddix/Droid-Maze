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
    private final static String SQL_GAMEPROFILE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+ DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TABLE_NAME +" ("+
    DroidMazeDbMetadata.GameProfile.GAMEPROFILE_ID_COL +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    DroidMazeDbMetadata.GameProfile.GAMEPROFILE_PROFILEID_COL +" TEXT NOT NULL UNIQUE, "+
    DroidMazeDbMetadata.GameProfile.GAMEPROFILE_LEVEL_COL +" INTEGER, "+
    DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TOTALTIME_COL +" INTEGER"+", "+
    "UNIQUE ("+ DroidMazeDbMetadata.GameProfile.GAMEPROFILE_PROFILEID_COL +")"+" );";
    private final static String SQL_GAMEPROFILE_DROP_TABLE = "DROP TABLE IF EXISTS "+ DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TABLE_NAME +";";

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

    private ContentValues getContentValues(com.sgxmobileapps.droidmaze.game.GameProfile gameProfile) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_PROFILEID_COL, gameProfile.getProfileId());
        contentValues.put(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_LEVEL_COL, gameProfile.getLevel());
        contentValues.put(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TOTALTIME_COL, gameProfile.getTotalTime());
        return contentValues;
    }

    private com.sgxmobileapps.droidmaze.game.GameProfile fillFromCursorGameProfile(Cursor cursor) {
        com.sgxmobileapps.droidmaze.game.GameProfile gameProfile = new com.sgxmobileapps.droidmaze.game.GameProfile();
        gameProfile.setProfileId(cursor.getString(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_PROFILEID_IDX));
        gameProfile.setLevel(((int) cursor.getLong(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_LEVEL_IDX)));
        gameProfile.setTotalTime(cursor.getLong(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TOTALTIME_IDX));
        return gameProfile;
    }

    /**
     * Inserts a GameProfile to database
     * 
     * @param gameProfile
     *     The GameProfile to insert
     * @return
     *     the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long addGameProfile(com.sgxmobileapps.droidmaze.game.GameProfile gameProfile) {
        return mDb.insert(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TABLE_NAME, null, getContentValues(gameProfile));
    }

    /**
     * Updates a GameProfile in database
     * 
     * @param id
     *     The id of the GameProfile to update
     * @param gameProfile
     *     The GameProfile to update
     * @return
     *     the number of rows affected (1 or 0)
     */
    public long updateGameProfile(long id, com.sgxmobileapps.droidmaze.game.GameProfile gameProfile) {
        String where = DroidMazeDbMetadata.GameProfile.GAMEPROFILE_ID_COL +" = "+ id;
        return mDb.update(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TABLE_NAME, getContentValues(gameProfile), where, null);
    }

    /**
     * Updates a GameProfile in database
     * 
     * @param gameProfile
     *     The GameProfile to update
     * @return
     *     the number of rows affected (1 or 0)
     */
    public long updateGameProfile(com.sgxmobileapps.droidmaze.game.GameProfile gameProfile) {
        String where = DroidMazeDbMetadata.GameProfile.GAMEPROFILE_PROFILEID_COL +" = '"+ gameProfile.getProfileId()+"'";
        return mDb.update(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TABLE_NAME, getContentValues(gameProfile), where, null);
    }

    /**
     * Deletes a GameProfile from database
     * 
     * @param id
     *     The id of the GameProfile to delete
     * @return
     *     the number of rows affected (1 or 0)
     */
    public long deleteGameProfile(long id) {
        String where = DroidMazeDbMetadata.GameProfile.GAMEPROFILE_ID_COL +" = "+ id;
        return mDb.delete(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TABLE_NAME, where, null);
    }

    /**
     * Deletes a GameProfile from database
     * 
     * @param gameProfile
     *     The GameProfile to delete
     * @return
     *     the number of rows affected (1 or 0)
     */
    public long deleteGameProfile(com.sgxmobileapps.droidmaze.game.GameProfile gameProfile) {
        String where = DroidMazeDbMetadata.GameProfile.GAMEPROFILE_PROFILEID_COL +" = '"+ gameProfile.getProfileId()+"'";
        return mDb.delete(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TABLE_NAME, where, null);
    }

    /**
     * Deletes all GameProfile from database
     * 
     * @return
     *     the number of rows deleted
     */
    public long deleteAllGameProfile() {
        return mDb.delete(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TABLE_NAME, "1", null);
    }

    /**
     * Reads a GameProfile from database with given id
     * 
     * @param id
     *     The id of the GameProfile to read
     * @return
     *     the entity read or null if one entity with the specified id doesn't exist
     */
    public com.sgxmobileapps.droidmaze.game.GameProfile getGameProfile(long id) {
        String where = DroidMazeDbMetadata.GameProfile.GAMEPROFILE_ID_COL +" = "+ id;
        String[] cols = new String[] {DroidMazeDbMetadata.GameProfile.GAMEPROFILE_ID_COL, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_PROFILEID_COL, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_LEVEL_COL, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TOTALTIME_COL };
        Cursor cursor;
        cursor = mDb.query(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TABLE_NAME, cols, where, null, null, null, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_DEFAULT_ORDER);
        if (cursor == null) {
            return null;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        com.sgxmobileapps.droidmaze.game.GameProfile res;
        res = fillFromCursorGameProfile(cursor);
        cursor.close();
        return res;
    }

    /**
     * Reads a GameProfile from database with given key fields
     * 
     * @param profileId
     *     The ProfileId field
     * @return
     *     the entity read or null if one entity with the specified key doesn't exist
     */
    public com.sgxmobileapps.droidmaze.game.GameProfile getGameProfile(String profileId) {
        String where = DroidMazeDbMetadata.GameProfile.GAMEPROFILE_PROFILEID_COL +" = '"+ profileId +"'";
        String[] cols = new String[] {DroidMazeDbMetadata.GameProfile.GAMEPROFILE_ID_COL, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_PROFILEID_COL, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_LEVEL_COL, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TOTALTIME_COL };
        Cursor cursor;
        cursor = mDb.query(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TABLE_NAME, cols, where, null, null, null, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_DEFAULT_ORDER);
        if (cursor == null) {
            return null;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        com.sgxmobileapps.droidmaze.game.GameProfile res;
        res = fillFromCursorGameProfile(cursor);
        cursor.close();
        return res;
    }

    /**
     * Returns a cursor for GameProfile
     * 
     * @return
     *     the cursor
     */
    public Cursor getCursorGameProfile() {
        String[] cols = new String[] {DroidMazeDbMetadata.GameProfile.GAMEPROFILE_ID_COL, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_PROFILEID_COL, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_LEVEL_COL, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TOTALTIME_COL };
        return mDb.query(DroidMazeDbMetadata.GameProfile.GAMEPROFILE_TABLE_NAME, cols, null, null, null, null, DroidMazeDbMetadata.GameProfile.GAMEPROFILE_DEFAULT_ORDER);
    }

    /**
     * Returns the array of GameProfile fetched from database
     * 
     * @return
     *     the array of GameProfile
     */
    public com.sgxmobileapps.droidmaze.game.GameProfile[] getAllGameProfile() {
        Cursor cursor = getCursorGameProfile();
        if (cursor == null) {
            return new com.sgxmobileapps.droidmaze.game.GameProfile[ 0 ] ;
        }
        com.sgxmobileapps.droidmaze.game.GameProfile[] entities = new com.sgxmobileapps.droidmaze.game.GameProfile[cursor.getCount()] ;
        if (cursor.moveToFirst()) {
            do {
                entities[cursor.getPosition()] = fillFromCursorGameProfile(cursor);
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
            db.execSQL(SQL_GAMEPROFILE_CREATE_TABLE);
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
            db.execSQL(SQL_GAMEPROFILE_DROP_TABLE);
            // The simplest case is to drop the old table and create a new one.
            // Create a new one.
            onCreate(db);
        }

    }

}
