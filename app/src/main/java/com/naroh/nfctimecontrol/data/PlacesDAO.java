/*
 * Copyright (c) 2016. OneOctopus www.oneoctopus.es
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.naroh.nfctimecontrol.data;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.List;

import com.naroh.nfctimecontrol.models.Check;

public class PlacesDAO {
    private String table;
    private Context context;
    private PlacesSql sql;
    private SQLiteDatabase db;


    public PlacesDAO(Context context) {
        this.context = context;
        this.table = "places";
        this.sql = new PlacesSql(context, "places", null, 10);
        this.db = this.sql.getWritableDatabase();
    }

    public List<String> getPlaceNames(){
        Cursor cursor = db.rawQuery("SELECT DISTINCT placename FROM places", null);
        List<String> result = new ArrayList<>();
        while (cursor.moveToNext())
            result.add(cursor.getString(cursor.getColumnIndex("placename")));
        cursor.close();
        return result;
    }
    public int getPlacesCount(){
        String sql = "SELECT DISTINCT placename FROM places;";
        SQLiteStatement statement = db.compileStatement(sql);
        Cursor cursor = db.query(true, "places", new String[] {"placename"}, "", null, null, null, null, null);
        int size = cursor.getCount();
        cursor.close();
        return size;
    }

    public boolean isEmpty(){
        Cursor cursor  = db.rawQuery("SELECT * FROM places", null);
        int size = cursor.getCount();
        cursor.close();
        return size == 0;
    }

    public long getVisitsCount(String place){
        String sql = "SELECT COUNT(*) FROM places WHERE placename = ?;";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, place);
        return statement.simpleQueryForLong();
    }

    public void check(DateTime date, String place) {
        Cursor cursor = db.rawQuery("SELECT * FROM places WHERE placename = '" + place + "'  ORDER BY id DESC", null);
        if (cursor.getCount() > 0) {
            // Get if there is neccesary to open a checkin or just close it
            boolean openCheck = false;

            // If there is a checkout date stored in the db, register the checkout
            cursor.moveToFirst();
            if(cursor.getString(cursor.getColumnIndex("checkout")) == null) {
                // No checkout registry, open checkin
                openCheck = true;
            }

            if (!openCheck){
                cursor.close();
                registerCheckIn(date, place);
            } else
                registerCheckOut(date, place, cursor);
        }else {
            cursor.close();
            registerCheckIn(date, place);
        }
    }

    private void registerCheckIn(DateTime date, String place) {
        String sql = "INSERT INTO places (placename, checkin) VALUES (?, ?);";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, place);
        statement.bindString(2, date.toString());
        statement.executeInsert();
    }

    private void registerCheckOut(DateTime date, String place, Cursor cursor) {
        cursor.moveToFirst();
        DateTime checkinDate = new DateTime(cursor.getString(cursor.getColumnIndex("checkin")));
        Minutes minutes = Minutes.minutesBetween(checkinDate, date);
        String sql = ("UPDATE places SET checkout = ? WHERE placename = ?;");
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, date.toString());
        statement.bindString(2, place);
        statement.executeUpdateDelete();
        cursor.close();
    }

    public boolean isCheckOpen(String place) {
        String[] columnsToReturn = { "id", "placename", "checkin", "checkout" };
        String [] selectionCriteria = {place};
        Cursor cursor = db.query("places", columnsToReturn, "placename=?", selectionCriteria, null, null, "id DESC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String checkOutDate = cursor.getString(cursor.getColumnIndex("checkout"));
            cursor.close();
            return checkOutDate == null;
        } else{
            cursor.close();
            return false;
        }
    }

    public List<String> getOpenChecks(){
        String[] columnsToReturn = { "placename" };
        Cursor cursor = db.query("places", columnsToReturn, "checkout is null or checkout = ?", new String[] {}, null, null, null);
        if(cursor.getCount() < 1) {
            cursor.close();
            return null;
        }else{
            List<String> result = new ArrayList<>();
            while (cursor.moveToNext())
                result.add(cursor.getString(cursor.getColumnIndex("placename")));
            cursor.close();
            return result;
        }
    }

    public long getTimeInOpenCheck(String place) throws SQLException{
        String[] columnsToReturn = { "checkin" };
        String[] selectionCriteria = { place };
        Cursor cursor = db.query("places", columnsToReturn, "placename=?", selectionCriteria, null, null, "id DESC");
        if(cursor.getColumnCount() < 1){
            cursor.close();
            return 0;
        } else {
            cursor.moveToFirst();
            DateTime checkin = DateTime.parse(cursor.getString(cursor.getColumnIndex("checkin")));
            cursor.close();
            Minutes minutesBetween = Minutes.minutesBetween(checkin, DateTime.now());
            return minutesBetween.getMinutes();
        }
    }

    public List<Check> getChecksIn(String place){
        List<Check> result = new ArrayList<>();

        String[] columnsToReturn = { "checkin", "checkout" };
        String[] selectionCriteria = { place };

        Cursor cursor = db.query("places", columnsToReturn, "placename = ?", selectionCriteria, null, null, "id DESC");

        while(cursor.moveToNext()) {
            DateTime checkin = DateTime.parse(cursor.getString(cursor.getColumnIndex("checkin")));
            DateTime checkout = DateTime.parse(cursor.getString(cursor.getColumnIndex("checkout")));
            result.add(new Check(place, checkin, checkout, Minutes.minutesBetween(checkin, checkout).getMinutes()));
        }

        cursor.close();

        return result;
    }

    public void delete(String place){
        String sql = "DELETE FROM places WHERE placename = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, place);
        statement.execute();
    }
}
