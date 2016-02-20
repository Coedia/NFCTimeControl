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

package es.oneoctopus.nfctimecontrol.data;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PlacesDAO {
    private String table;
    private Context context;
    private PlacesSql sql;
    private SQLiteDatabase db;


    public PlacesDAO(Context context) {
        this.context = context;
        this.table = "places";
        this.sql = new PlacesSql(context, "places", null, 1);
        this.db = this.sql.getWritableDatabase();

    }

    public List<String> getPlaces(){
        Cursor cursor = db.rawQuery("SELECT DISTINCT placename FROM places", null);
        List<String> result = new ArrayList<>();
        while (cursor.moveToNext())
            result.add(cursor.getString(cursor.getColumnIndex("placename")));
        cursor.close();
        return result;
    }

    public boolean isFirsTime (String name){
        Cursor cursor = db.rawQuery("SELECT * FROM PLACES WHERE placename ='"+ name + "'", null);
        int size = cursor.getCount();
        cursor.close();
        return size == 0;
    }


}
